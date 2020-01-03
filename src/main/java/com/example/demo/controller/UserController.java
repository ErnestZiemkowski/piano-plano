package com.example.demo.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@DeleteMapping("/remove-friend/{id}")
	@PreAuthorize("hasRole('USER')")
	public String removeFriend(@PathVariable Long id) {
		userService.removeFriendById(id);
	
		return "Friend has been removed";
	}
	
	@GetMapping("/friends")
	@PreAuthorize("hasRole('USER')")
	public Set<User> getFriends() {
		return userService.getFriends();
	}
}