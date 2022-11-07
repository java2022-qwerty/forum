package telran.java2022.post.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 4387793719137314638L;

	public  UserNotFoundException(String login) {
		super(" User with login = " + login + " not found");

	}
}
