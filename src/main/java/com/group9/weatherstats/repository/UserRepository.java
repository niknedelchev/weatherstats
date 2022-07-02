package com.group9.weatherstats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group9.weatherstats.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
