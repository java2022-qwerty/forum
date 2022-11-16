package telran.java2022;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;
import telran.java2022.login.dao.UserRepository;

@SpringBootApplication
@RequiredArgsConstructor
public class ForumServiceApplication  {

final	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(ForumServiceApplication.class, args);
	}


}
