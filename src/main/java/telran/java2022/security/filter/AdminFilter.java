package telran.java2022.security.filter;

import java.io.IOException;
import java.security.Principal;

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
@Order(30)
@RequiredArgsConstructor
public class AdminFilter implements Filter {

//	final UserRepository userRepository;
	final SecurityContext context;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			Principal principal = request.getUserPrincipal();
//			UserAccount user = userRepository.findById(request.getUserPrincipal().getName()).get();
			User user = context.getUser(request.getUserPrincipal().getName());
			System.out.println(user.getRoles());
			String[] arr = path.split("/");
			String userName = arr[arr.length - 1];
			if (!(user.getRoles().contains("ADMINISTRATOR".toUpperCase()) || userName.equals(principal.getName()))) {
				response.sendError(403, "You don`t have permission to do it, only ADMINISTRATOR or Owner");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String servletPath) {
		return (servletPath.matches("/account/user/\\w+/role/\\w+/?")
				|| (servletPath.matches("/account/user/\\w+/?") && "DELETE".equalsIgnoreCase(method)));
	}

}
