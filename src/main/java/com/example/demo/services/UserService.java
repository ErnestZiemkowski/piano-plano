package com.example.demo.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AppException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public void removeFriendById(Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User Role not set."));
		
		User friendToRemove = userRepository.findById(id)
				.orElseThrow(() -> new AppException("User Role not set."));

		loggedUser.removeFriend(friendToRemove);
		userRepository.save(loggedUser);
	}
	
	public Set<User> getFriends() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User Role not set."));

		return loggedUser.getFriends();
	}
}