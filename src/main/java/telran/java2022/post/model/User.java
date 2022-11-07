package telran.java2022.post.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = "login")
@Document(collection = "Users")
public class User {
	@Id
	@Setter
	String login;
	@Setter
	String password;
	@Setter
	String firstName;
	@Setter
	String lastName;
	Set<String> roles;
	
	public User(String login, String password, String firstName, String lastName) {
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public boolean addRole(String role) {
		return roles.add(role);
	}
	public boolean removeRole(String role) {
		return roles.remove(role);
	}
	
}
