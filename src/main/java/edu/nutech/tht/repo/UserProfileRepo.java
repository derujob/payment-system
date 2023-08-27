package edu.nutech.tht.repo;

import edu.nutech.tht.model.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserProfileRepo extends CrudRepository<UserProfile, Long> {
    List<UserProfile> findByEmail(String email);
}
