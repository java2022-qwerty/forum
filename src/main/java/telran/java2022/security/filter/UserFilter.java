package telran.java2022.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java2022.login.dao.UserRepository;
import telran.java2022.login.model.User;

@Component
@Order(40)
@RequiredArgsConstructor
public class UserFilter implements Filter {

	final UserRepository userRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			User user = userRepository.findById(request.getUserPrincipal().getName()).get();
			if (!user.getRoles().contains("USER".toUpperCase())) {
				response.sendError(403, "You don`t have permission to do it, only USER");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String servletPath) {
		boolean login = servletPath.matches("/account/login/?") && "POST".equalsIgnoreCase(method);
		boolean delUser = servletPath.matches("/account/user/\\w+/?") && "DELETE".equalsIgnoreCase(method);
		boolean updateUser = servletPath.matches("/account/user/\\w+/?") && "PUT".equalsIgnoreCase(method);
		boolean updatePassUser = servletPath.matches("/account/password/?") && "PUT".equalsIgnoreCase(method);

		boolean newPost = servletPath.matches("/forum/post/\\w+/?") && "POST".equalsIgnoreCase(method);
		boolean getPost = servletPath.matches("/forum/post/\\w+/?") && "GET".equalsIgnoreCase(method);
		boolean delPost = servletPath.matches("/forum/post/\\w+/?") && "DEL".equalsIgnoreCase(method);
		boolean updatePost = servletPath.matches("/forum/post/\\w+/?") && "PUT".equalsIgnoreCase(method);
		boolean addLikePost = servletPath.matches("/forum/post/\\w+/like/?") && "PUT".equalsIgnoreCase(method);
		boolean addCommentPost = servletPath.matches("/forum/post/\\w+/comment/\\w+/?")
				&& "PUT".equalsIgnoreCase(method);

		return login || delUser || updateUser || updatePassUser || newPost || getPost || delPost || updatePost
				|| addLikePost || addCommentPost;
	}
}
