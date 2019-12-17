package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AppException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.message.request.ProjectRequest;
import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;

	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}	
	
	public Project createProject(ProjectRequest projectRequest) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User Role not set."));

		Project project = Project.builder()
				.name(projectRequest.getName())
				.description(projectRequest.getDescription())
				.creator(loggedUser)
				.build();
		
		return projectRepository.save(project);
	}
	
	public Project updateProject(ProjectRequest projectRequest, Long id) {
		Project project = projectRepository
					.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
		
		if (projectRequest.getName() != null) project.setName(projectRequest.getName());
		if (projectRequest.getDescription() != null) project.setDescription(projectRequest.getDescription());
		
		return projectRepository.save(project);
	}

	public void deleteProjectById(Long id) {
		projectRepository.deleteById(id);
	}
}
