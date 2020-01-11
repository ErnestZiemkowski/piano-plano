package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.message.request.CardRequest;
import com.example.demo.message.request.KanbanCategoryRequest;
import com.example.demo.model.Card;
import com.example.demo.model.KanbanCategory;
import com.example.demo.model.Project;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.KanbanCategoryRepository;
import com.example.demo.repository.ProjectRepository;

@Service
public class KanbanCategoryService {

	@Autowired
	private KanbanCategoryRepository kanbanCategoryRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private CardRepository cardRepository;
	
	public List<KanbanCategory> getKanbanCategoriesByProjectId(Long projectId) {
		return kanbanCategoryRepository.findKanbanCategoriesByProjectId(projectId);
	}
	
	public KanbanCategory createKanbanCategory(KanbanCategoryRequest kanbanCategoryRequest) {
		Project project = projectRepository.findById(kanbanCategoryRequest.getProjectId())
				.orElseThrow(() -> new ResourceNotFoundException("Project", "id", kanbanCategoryRequest.getProjectId()));		
		
		KanbanCategory kanbanCategory = KanbanCategory.createKanbanCategory(
				kanbanCategoryRequest.getTitle(), 
				kanbanCategoryRequest.getPosition(),
				project);
		
		return kanbanCategoryRepository.save(kanbanCategory);
	}
	
	public KanbanCategory updateKanbanCategory(KanbanCategoryRequest kanbanCategoryRequest, Long kanbanId) {
		KanbanCategory kanbanCategory = kanbanCategoryRepository.findById(kanbanId) 
				.orElseThrow(() -> new ResourceNotFoundException("Project", "id", kanbanCategoryRequest.getProjectId()));		

		
		if (kanbanCategoryRequest.getTitle() != null) kanbanCategory.setTitle(kanbanCategoryRequest.getTitle());
		
		return kanbanCategoryRepository.save(kanbanCategory);
	}
	
	public void deleteKanbanCategoryById(Long id) {
		KanbanCategory kanbanCategory = kanbanCategoryRepository.findById(id) 
				.orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));		
		kanbanCategory.setProject(null);
		kanbanCategoryRepository.deleteById(id);
	}
	
	public ArrayList<KanbanCategory> setKanbanCategoriesPosition(ArrayList<KanbanCategoryRequest> kanbanCategoriesRequests) {
		ArrayList<KanbanCategory> kanbanCategories = new ArrayList<>(); 
		for(int i = 0; i < kanbanCategoriesRequests.size(); i++) {
			KanbanCategoryRequest kanbanCategoryRequest = kanbanCategoriesRequests.get(i);
			KanbanCategory kanbanCategory = kanbanCategoryRepository.findById(kanbanCategoryRequest.getId()) 
					.orElseThrow(() -> new ResourceNotFoundException("Kanban Category", "id", kanbanCategoryRequest.getId()));
			kanbanCategory.setPosition(i);
			kanbanCategoryRepository.save(kanbanCategory);
			
			for(int j = 0; j < kanbanCategoryRequest.getCards().size(); j++) {
				CardRequest cardRequest = kanbanCategoryRequest.getCards().get(j);
				Card card = cardRepository.findById(cardRequest.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardRequest.getId()));
				card.setPosition(j);
				card.setKanbanCategory(kanbanCategory);
				
				handleSettingCardAsDone(kanbanCategoryRequest, card, j);
				
				cardRepository.save(card);
			}
			
			kanbanCategories.add(kanbanCategory);
		}
		
		return kanbanCategories;
	}
	
	private void handleSettingCardAsDone(KanbanCategoryRequest kanbanCategoryRequest, Card card, int j) {
		if (j == kanbanCategoryRequest.getCards().size() - 1) {
			card.setDone(true);
		} else {
			card.setDone(false);
		}		
	}
	
}
