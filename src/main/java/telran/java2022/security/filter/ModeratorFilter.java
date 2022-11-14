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
import telran.java2022.post.dao.PostRepository;
import telran.java2022.post.model.Post;
import telran.java2022.security.context.SecurityContext;
import telran.java2022.security.context.User;

@Component
@Order(20)
@RequiredArgsConstructor
public class ModeratorFilter implements Filter {

//	final UserRepository userRepository;
	final PostRepository postRepository;
	final SecurityContext context;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();

		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			Principal principal = request.getUserPrincipal();
			String[] arr = path.split("/");
			String postId = arr[arr.length - 1];
			Post post = postRepository.findById(postId).orElse(null);
			if (post == null) {
				response.sendError(404, "post id = " + postId + " not found");
				return;
			}
//			UserAccount user = userRepository.findById(request.getUserPrincipal().getName()).get();
			User user = context.getUser(request.getUserPrincipal().getName());

			if (!(user.getRoles().contains("MODERATOR".toUpperCase())
					|| principal.getName().equals(post.getAuthor()))) {
				response.sendError(403, "You don`t have permission to do it, only MODERATOR or author");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String servletPath) {
		return (servletPath.matches("/forum/post/\\w+/?") && "DELETE".equalsIgnoreCase(method));
	}
}
