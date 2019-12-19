package com.example.demo.controller;

import java.util.Set;

import javax.validation.Valid;

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

import com.example.demo.message.request.ProjectRequest;
import com.example.demo.message.response.ResponseMessage;
import com.example.demo.model.Project;
import com.example.demo.services.ProjectService;

@CrossOrigin(origins =  "*", maxAge = 3600)
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@GetMapping()
	@PreAuthorize("hasRole('USER')")
	public Set<Project> getProjects() {
		return projectService.getProjectsOfLoggedUser();
	}	
	
	@PostMapping()
	@PreAuthorize("hasRole('USER')")
	public Project createProject(@Valid @RequestBody ProjectRequest projectRequest) {
		return projectService.createProject(projectRequest);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public Project updateProject(@RequestBody ProjectRequest projectRequest, @PathVariable Long id) {
		return projectService.updateProject(projectRequest, id);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseMessage deleteProjectById(@PathVariable Long id) {
		projectService.deleteProjectById(id);
		String responseMessage = String.format("Project with id of %d deleted successfully", id);
		
		return new ResponseMessage(responseMessage);
	}
}
