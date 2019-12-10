package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.message.request.SettingsRequest;
import com.example.demo.model.Settings;
import com.example.demo.services.SettingsService;

@CrossOrigin(origins =  "*", maxAge = 3600)
@RestController
@RequestMapping("/api/settings")
public class SettingsController {
	
	@Autowired
	private SettingsService settingsService;
	
	@GetMapping()
	@PreAuthorize("hasRole('USER')")
	public Settings getSettingsOfLoggedUser() {
		return settingsService.getSettingsOfLoggedUser();
	}

	@PostMapping()
	@PreAuthorize("hasRole('USER')")
	public Settings updateSettingsOfLoggedUser(@RequestBody SettingsRequest settingsRequest) {
		return settingsService.updateSettingsOfLoggedUser(settingsRequest);
	}
}