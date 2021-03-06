package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AppException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.message.request.CardRequest;
import com.example.demo.model.Card;
import com.example.demo.model.KanbanCategory;
import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.KanbanCategoryRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CardService {

	@Autowired
	private CardRepository cardRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private KanbanCategoryRepository kanbanCategoryRepository;
	
	public void deleteCardById(Long id) {
		Card card = cardRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Card", "id", id));		
		card.setKanbanCategory(null);
		card.setDailyGoal(null);
		cardRepository.deleteById(id);
	}
	
	public Card createCard(CardRequest cardRequest) {		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User does not exists."));
		
		Project project = projectRepository
				.findById(cardRequest.getProjectId())
				.orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardRequest.getProjectId()));

		KanbanCategory kanbanCategory = project
				.getKanbanCategories()
				.get(0);
				
		int cardsInKanbanCategory = kanbanCategory.getCards().size();
		
		Card card = Card.builder()
				.title(cardRequest.getTitle())
				.description(cardRequest.getDescription())
				.position(cardsInKanbanCategory + 1)
				.creator(loggedUser)
				.kanbanCategory(kanbanCategory)
				.build();
		
		return cardRepository.save(card);
	}
	
	public Card updateCard(CardRequest cardRequest, Long id) {
		Card card = cardRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Card", "id", id));

		if (cardRequest.getTitle() != null) card.setTitle(cardRequest.getTitle());
		if (cardRequest.getDescription() != null) card.setDescription(cardRequest.getDescription());
		if (cardRequest.getKanbanCategoryId() != null) {
			KanbanCategory kanbanCategory = kanbanCategoryRepository
					.findById(cardRequest.getKanbanCategoryId())
					.orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardRequest.getKanbanCategoryId()));
		
			List<KanbanCategory> kanbanCategories = card.getKanbanCategory().getProject().getKanbanCategories();
			handleSettingCardAsDone(kanbanCategories, kanbanCategory, card);
			
			card.setKanbanCategory(kanbanCategory);
		}
		
		if (cardRequest.getAssignee() != null) handleAssigningUserToCard(cardRequest, card);
						
		return cardRepository.save(card);
	}
	
	private void handleAssigningUserToCard(CardRequest cardRequest, Card card) {
		if (cardRequest.getAssignee().equals("unassigned")) {
			card.setAssignee(null);
		} else {
			User assignee = userRepository
					.findByUsername(cardRequest.getAssignee())
					.orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardRequest.getAssignee()));
			card.setAssignee(assignee);	
		}			
	}
	
	private void handleSettingCardAsDone(List<KanbanCategory> kanbanCategories, KanbanCategory kanbanCategory, Card card) {
		if(kanbanCategories.get(kanbanCategories.size() - 1) == kanbanCategory) {
			card.setDone(true);
		} else {
			card.setDone(false);
		}	
	}
	
}