package com.group9.weatherstats.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.group9.weatherstats.model.User;
import com.group9.weatherstats.repository.UserRepository;



@Service
public class UserPrincipalDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	 
	public UserPrincipalDetailsService(UserRepository userRepository) {
	     this.userRepository = userRepository;
	 }
	    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(username);
		UserPrincipal userPrincipal = new UserPrincipal(user);
		return userPrincipal;
	}

}
