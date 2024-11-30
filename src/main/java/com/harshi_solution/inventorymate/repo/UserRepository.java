package com.harshi_solution.inventorymate.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harshi_solution.inventorymate.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

}
