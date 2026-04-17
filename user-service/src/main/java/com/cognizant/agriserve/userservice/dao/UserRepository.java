package com.cognizant.agriserve.userservice.dao;

import com.cognizant.agriserve.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    List<User> findByRole(User.Role role);

    List<User> findByStatus(String status);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
