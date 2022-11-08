package telran.java2022.login.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java2022.post.model.User;

public interface UserRepository extends CrudRepository<User, String> {

}
