package edu.nutech.tht.repo;

import edu.nutech.tht.model.Service;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServiceRepo extends CrudRepository<Service, Long> {
    List<Service> findAll();
    List<Service> findByServiceCode(String serviceCode);
}
