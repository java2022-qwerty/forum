package telran.java2022.login.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.login.dto.AddRoleDto;
import telran.java2022.login.dto.CreateUserDto;
import telran.java2022.login.dto.LoginAndChangePassDto;
import telran.java2022.login.dto.UpdateNameDto;
import telran.java2022.login.dto.UserDto;
import telran.java2022.post.dao.PostRepository;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

	final PostRepository postRepository;
	final ModelMapper modelMapper;
	
	@Override
	public UserDto addNewUser(CreateUserDto createUserDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto loginUser(LoginAndChangePassDto loginAndChangePassDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto removeUser(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDto updateUser(String user, UpdateNameDto updateNameDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AddRoleDto addRole(String user, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AddRoleDto removeRole(String user, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePassword(LoginAndChangePassDto loginAndChangePassDto) {
		// TODO Auto-generated method stub
		
	}

}
