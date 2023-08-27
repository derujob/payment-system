package edu.nutech.tht.repo;

import edu.nutech.tht.model.Banner;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface BannerRepo extends CrudRepository<Banner, Long> {
    List<Banner> findAll();
}
