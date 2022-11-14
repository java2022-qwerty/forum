package telran.java2022.login.service;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.login.dao.UserRepository;
import telran.java2022.login.dto.AddRoleDto;
import telran.java2022.login.dto.CreateUserDto;
import telran.java2022.login.dto.UpdateNameDto;
import telran.java2022.login.dto.UserDto;
import telran.java2022.login.dto.exception.UserAlreadyExistException;
import telran.java2022.login.dto.exception.UserNotFoundException;
import telran.java2022.login.model.UserAccount;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

	final UserRepository userRepository;
	final ModelMapper modelMapper;

	@Override
	public UserDto addNewUser(CreateUserDto createUserDto) {
		boolean userExist = userRepository.findById(createUserDto.getLogin()).isEmpty();
		if (!userExist) {
			throw new UserAlreadyExistException(createUserDto.getLogin());
		}
		UserAccount user = modelMapper.map(createUserDto, UserAccount.class);
		String password = BCrypt.hashpw(createUserDto.getPassword(), BCrypt.gensalt());
		user.setPassword(password);
		user.addRole("USER");
		user = userRepository.save(user);

		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto loginUser(String login) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		userRepository.delete(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UpdateNameDto updateNameDto) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		String firstName = updateNameDto.getFirstName();
		if (firstName != null) {
			user.setFirstName(updateNameDto.getFirstName());
		}
		String lastName = updateNameDto.getLastName();
		if (lastName != null) {
			user.setLastName(lastName);
		}
		userRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public AddRoleDto addRole(String login, String role) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		user.addRole(role.toUpperCase());
		userRepository.save(user);
		return modelMapper.map(user, AddRoleDto.class);
	}

	@Override
	public AddRoleDto removeRole(String login, String role) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		user.removeRole(role);
		userRepository.save(user);
		return modelMapper.map(user, AddRoleDto.class);
	}

	@Override
	public void updatePassword(String login, String newPassword) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		user.setPassword(password);
		userRepository.save(user);
	}

}
