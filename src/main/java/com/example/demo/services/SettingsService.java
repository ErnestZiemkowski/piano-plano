package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AppException;
import com.example.demo.message.request.SettingsRequest;
import com.example.demo.model.Settings;
import com.example.demo.model.User;
import com.example.demo.repository.SettingsRepository;
import com.example.demo.repository.UserRepository;

@Service
public class SettingsService {

	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private UserRepository userRepository;
	
	public Settings getSettingsOfLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User Role not set."));
		
		return settingsRepository.findSettingsByUserId(loggedUser.getId());
	}
	
	public Settings updateSettingsOfLoggedUser(SettingsRequest settingsRequest) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User Role not set."));

		
		settingsRepository.updateSettingsByUserId(settingsRequest.getBackgroundImageUrl(), loggedUser.getId());
		return settingsRepository.findSettingsByUserId(loggedUser.getId());
	}
}