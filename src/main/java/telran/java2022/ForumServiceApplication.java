package telran.java2022;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;
import telran.java2022.login.dao.UserRepository;
import telran.java2022.login.model.UserAccount;

@SpringBootApplication
@RequiredArgsConstructor
public class ForumServiceApplication implements CommandLineRunner {

final	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(ForumServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!userRepository.existsById("admin")) {
			String password = BCrypt.hashpw("admin", BCrypt.gensalt());
			UserAccount user = new UserAccount("admin", password, "", "");
			user.addRole("USER");
			user.addRole("MODERATOR");
			user.addRole("ADMINISTRATOR");

			userRepository.save(user);
		}
	}

}
