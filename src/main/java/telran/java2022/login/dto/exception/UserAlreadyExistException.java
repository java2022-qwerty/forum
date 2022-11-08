package telran.java2022.login.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAlreadyExistException extends RuntimeException{
	private static final long serialVersionUID = 4387793719137314638L;

	public  UserAlreadyExistException(String login) {
		super("User with login '" + login + "' already exist");

	}
}

