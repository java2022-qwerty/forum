package telran.java2022.login.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java2022.login.dto.AddRoleDto;
import telran.java2022.login.dto.CreateUserDto;
import telran.java2022.login.dto.LoginAndChangePassDto;
import telran.java2022.login.dto.UpdateNameDto;
import telran.java2022.login.dto.UserDto;
import telran.java2022.login.service.LoginService;

@RestController
@RequiredArgsConstructor
public class LoginController {
	
	final LoginService loginService;
	
	@PostMapping("/register")
	public UserDto addUser(@RequestBody CreateUserDto CreateUserDto) {
		return loginService.addNewUser(CreateUserDto);
	}
	@PostMapping("/login")
	public UserDto addUser(@RequestBody LoginAndChangePassDto loginAndChangePassDto) {
		return loginService.loginUser(loginAndChangePassDto);
	}
	
	@DeleteMapping("/user/{user}")
	public UserDto removeUser(@PathVariable String user) {
		return loginService.removeUser(user);
	}
	
	@PutMapping("/user/{user}")
	public UserDto updateUser(@PathVariable String user, @RequestBody UpdateNameDto updateNameDto) {
		return loginService.updateUser(user, updateNameDto);
	}
	@PutMapping("/user/{user}/role/{role}")
	public AddRoleDto addRole(@PathVariable String user, @PathVariable String role) {
		return loginService.addRole(user, role);
	}
	
	@DeleteMapping("/user/{user}/role/{role}")
	public AddRoleDto removeRole(@PathVariable String user, @PathVariable String role) {
		return loginService.removeRole(user, role);
	}
	
	@PutMapping("/user/password")
	public void updatePassword(@RequestBody LoginAndChangePassDto loginAndChangePassDto) {
		 loginService.updatePassword(loginAndChangePassDto);
	}
	
	
}
