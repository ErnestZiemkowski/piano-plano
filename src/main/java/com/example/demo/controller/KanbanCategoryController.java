package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.message.request.KanbanCategoryRequest;
import com.example.demo.message.response.ResponseMessage;
import com.example.demo.model.KanbanCategory;
import com.example.demo.services.KanbanCategoryService;

@CrossOrigin(origins =  "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class KanbanCategoryController {
	
	@Autowired
	private KanbanCategoryService kanbanCategoryService;
	
	@GetMapping("/projects/{projectId}/kanban-categories")
	@PreAuthorize("hasRole('USER')")
	public List<KanbanCategory> getKanbanCategoriesByProjectId(@PathVariable Long projectId) {
		return kanbanCategoryService.getKanbanCategoriesByProjectId(projectId);
	}
	
	@PostMapping("/kanban-category")
	@PreAuthorize("hasRole('USER')")
	public KanbanCategory createKanbanCategory(@RequestBody KanbanCategoryRequest kanbanCategoryRequest) {
		return kanbanCategoryService.createKanbanCategory(kanbanCategoryRequest);
	}

	@PutMapping("/kanban-category/{id}")
	@PreAuthorize("hasRole('USER')")
	public KanbanCategory updateKanbanCategory(@RequestBody KanbanCategoryRequest kanbanCategoryRequest, @PathVariable Long id) {
		return kanbanCategoryService.updateKanbanCategory(kanbanCategoryRequest, id);
	}
	
	@DeleteMapping("/kanban-category/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseMessage deleteKanbanCategoryById(@PathVariable Long id) {
		kanbanCategoryService.deleteKanbanCategoryById(id);
		String responseMessage = String.format("Kanban Category with id of %d deleted successfully", id);
		
		return new ResponseMessage(responseMessage);
	}
	
	@PostMapping("/kanban-category/rearange-position")
	@PreAuthorize("hasRole('USER')")
	public ArrayList<KanbanCategory> setKanbanCategoriesPosition(@RequestBody ArrayList<KanbanCategoryRequest> kanbanCategoriesRequests) {
		return kanbanCategoryService.setKanbanCategoriesPosition(kanbanCategoriesRequests);
	}
	
}
