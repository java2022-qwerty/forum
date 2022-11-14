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
import telran.java2022.security.context.SecurityContext;
import telran.java2022.security.context.User;

@Component
@Order(10)
@RequiredArgsConstructor
public class UserFilter implements Filter {

//	final UserRepository userRepository;
	final SecurityContext context;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();

		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			String name = request.getUserPrincipal().getName();
			String[] arr = path.split("/");
			String userName = arr[arr.length - 1];
			
//			UserAccount user = userRepository.findById(request.getUserPrincipal().getName()).get();
			User user = context.getUser(request.getUserPrincipal().getName());

			if(!userName.equals(name)){
				throw new Error("You don`t have permission to do it");
			}
			if (!user.getRoles().contains("USER".toUpperCase())) {
				response.sendError(403, "You don`t have permission to do it, only USER");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String servletPath) {

		boolean removeUser = servletPath.matches("/account/user/\\w+/?") && "DELETE".equalsIgnoreCase(method);
		boolean updateUser = servletPath.matches( "/account/user/\\w+/?") && "PUT".equalsIgnoreCase(method);
		boolean post = servletPath.matches("/forum/post/\\w+/?") && "Post".equalsIgnoreCase(method);
		boolean addCommentPost = servletPath.matches("/forum/post/\\w+/comment/\\w+/?");

		return removeUser || updateUser|| addCommentPost||post ;
	}
}
