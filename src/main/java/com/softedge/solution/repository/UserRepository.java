package com.softedge.solution.repository;

import com.softedge.solution.repomodels.UserRegistration;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<UserRegistration, Long> {

    UserRegistration findByUsername(String username);
}
