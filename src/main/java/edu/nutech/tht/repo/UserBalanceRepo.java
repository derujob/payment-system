package edu.nutech.tht.repo;

import edu.nutech.tht.model.UserBalance;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserBalanceRepo extends CrudRepository<UserBalance, Long> {
    List<UserBalance> findByUsers_Email(String email);
//    public UserBalance saveUserBalance(UserBalance userBalance) throws SQLException;
}
