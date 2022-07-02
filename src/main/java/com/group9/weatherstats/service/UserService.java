package com.group9.weatherstats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.group9.weatherstats.model.Role;
import com.group9.weatherstats.model.User;
import com.group9.weatherstats.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public User registerRegular(String firstName, String lastName, String username, String password) {

		User user = new User(firstName, lastName, username, passwordEncoder.encode(password), Role.REGULAR);
		this.userRepository.save(user);

		return user;
	}

	public void registerAdmin(String firstName, String lastName, String username, String password) {

		User user = new User(firstName, lastName, username, passwordEncoder.encode(password), Role.ADMIN);
		this.userRepository.save(user);

	}
	
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User currentUser = userRepository.findByUsername(userDetails.getUsername());
		
		return currentUser;
	}

}
