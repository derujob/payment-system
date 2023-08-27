package edu.nutech.tht.repo;

import edu.nutech.tht.model.UserTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserTransactionRepo extends CrudRepository<UserTransaction, Long> {
    List<UserTransaction> findByUsers_Email(String email);
}
