package telran.java2022.security.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import telran.java2022.login.dao.UserRepository;
import telran.java2022.login.model.UserAccount;
import telran.java2022.security.context.SecurityContext;
import telran.java2022.security.context.User;
import telran.java2022.security.service.SessionService;

@Component
@RequiredArgsConstructor
@Order(10)
@Builder
public class AuthenticationFilter implements Filter {

	final UserRepository userRepository;
	final SecurityContext context;
	final SessionService sessionService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			String sessionId = request.getSession().getId();

//			UserAccount userAccount = sessionService.getUser(sessionId);
//			if(userAccount == null) {

			String token = request.getHeader("Authorization");
			if (token != null) {

				String[] credentials = null;

				try {
					credentials = getCredentialFromToken(token);
				} catch (Exception e) {
					response.sendError(401, "Invalid token");
				}

				UserAccount userAccount = userRepository.findById(credentials[0]).orElse(null);
//							userAccount = userRepository.findById(credentials[0]).orElse(null);

//				boolean checkAuth = user.getPassword().equals(credentials[1]);
//				boolean checkRole = user.getRoles().contains("USER");
				if (token == null || !BCrypt.checkpw(credentials[1], userAccount.getPassword())) {
					response.sendError(401, "Wrong credentials");
					return;
				}

				sessionService.addUser(sessionId, userAccount);
			}
			UserAccount userAccount = sessionService.getUser(sessionId);
			
//			}

			request = new WrappedRequest(request, userAccount.getLogin());
			User user = User.builder().userName(userAccount.getLogin()).password(userAccount.getPassword())
					.roles(userAccount.getRoles()).build();
			context.addUser(user);
		}
		chain.doFilter(request, response);
	}

	private String[] getCredentialFromToken(String token) {
		String[] basicAuthStrings = token.split(" ");
		String decode = new String(Base64.getDecoder().decode(basicAuthStrings[1]));
		String[] credentials = decode.split(":");
		return credentials;
	}

	private boolean checkEndPoint(String method, String servletPath) {

		boolean searhingForEveryone = (("GET".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method))
				&& (servletPath.matches("/forum/posts/\\w+/?") || (servletPath.matches("/forum/posts/author/\\w+/?"))));
		boolean newPost = "POST".equalsIgnoreCase(method) && servletPath.matches("/account/register/?");
		return !searhingForEveryone && !newPost;
	}

	private class WrappedRequest extends HttpServletRequestWrapper {
		String login;

		public WrappedRequest(HttpServletRequest request, String login) {
			super(request);
			this.login = login;
		}

		@Override
		public Principal getUserPrincipal() {
			return () -> login;
		}
	}
}
