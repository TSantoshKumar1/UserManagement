package com.service.demo.repo;

import com.service.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User , Long> {

    Optional<User> findByName(String name);
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
