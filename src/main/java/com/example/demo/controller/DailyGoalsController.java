package com.example.demo.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.message.request.DailyGoalRequest;
import com.example.demo.model.DailyGoal;
import com.example.demo.services.DailyGoalsService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/daily-goals")
public class DailyGoalsController {

	@Autowired
	private DailyGoalsService dailyGoalsService;
	
	@GetMapping
	@PreAuthorize("hasRole('USER')")
	public Set<DailyGoal> getActiveDailyGoals() {
		return dailyGoalsService.getActiveDailyGoals();
	}
	
	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public String toggleSettingCardAsDailyGoal(@RequestBody DailyGoalRequest request) {
		dailyGoalsService.setCardAsDailyGoal(request.getCardId());

		return "Card has been set as daily goal";
	}
}