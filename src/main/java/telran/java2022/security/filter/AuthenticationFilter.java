package telran.java2022.security.filter;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java2022.login.dao.UserRepository;
import telran.java2022.login.dto.exception.UserNotFoundException;
import telran.java2022.login.model.User;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

	final UserRepository userRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			String token = request.getHeader("Authorization");
			String[] credentials = getCredentialFromToken(token);
			User user = userRepository.findById(credentials[0]).orElseThrow(() -> new UserNotFoundException(credentials[0]));
			boolean checkAuth = user.getPassword().equals(credentials[1]);
			boolean checkRole = user.getRoles().contains("USER");
			if (token == null || !checkAuth || !checkRole) {
				response.sendError(401, "Wrong credentials");
				return;
			}
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
		return !("POST".equalsIgnoreCase(method) && servletPath.equals("/account/register"));
	}

}
