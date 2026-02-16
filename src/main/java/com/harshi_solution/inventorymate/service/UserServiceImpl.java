package com.harshi_solution.inventorymate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harshi_solution.inventorymate.entities.User;
import com.harshi_solution.inventorymate.repo.UserRepository;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public boolean login(String username, String password) {
		boolean result =userRepository.findByEmail(username).getPassword().equals(password);
		return result;
	}

	@Override
	public User saveUser(User user) {
        user.setEmail(user.getEmail());
        user.setLastName(user.getLastName());
        user.setFirstName(user.getFirstName());
        user.setPassword(user.getPassword());
        // Save the user
        return userRepository.save(user);
	}

}

