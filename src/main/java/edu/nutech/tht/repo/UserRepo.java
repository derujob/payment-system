package edu.nutech.tht.repo;

import edu.nutech.tht.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Long> {
    List<User> findUserByEmailAndPassword(String email, String password);
    List<User> findUserByEmail(String email);
}
