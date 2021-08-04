package com.microservice.usersdemo.repository;

import com.microservice.usersdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
