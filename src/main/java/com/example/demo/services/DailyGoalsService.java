package com.example.demo.services;

import java.util.Date;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AppException;
import com.example.demo.model.Card;
import com.example.demo.model.DailyGoal;
import com.example.demo.model.User;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.DailyGoalsRepository;
import com.example.demo.repository.UserRepository;

@Service
public class DailyGoalsService {

	@Autowired
	private DailyGoalsRepository dailyGoalsRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CardRepository cardRepository;
	
	public Set<DailyGoal> getActiveDailyGoals() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User not found."));
		Date today = new Date();
		return dailyGoalsRepository.getActiveDailyGoalsByUserId(loggedUser.getId(), today);
	}
	
	@Transactional
	public void setCardAsDailyGoal(Long cardId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User not found."));
		
		Card card = cardRepository
				.findById(cardId)
				.orElseThrow(() -> new AppException("Card not found."));
		
		if (card.getDailyGoal() != null) {
			DailyGoal dailyGoal = card.getDailyGoal();
			card.setDailyGoal(null);
			dailyGoalsRepository.delete(dailyGoal);
			cardRepository.save(card);
		} else {
			DailyGoal dailyGoal = DailyGoal.createDailyGoal(card, loggedUser);
			dailyGoal.setExpiresAt();
			dailyGoalsRepository.save(dailyGoal);
			cardRepository.save(card);
		}
	}
}