package com.example.demo.services;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
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
	
	public static <T> Set<T> mergeSet(Set<T> a, Set<T> b) {
		Set<T> mergeSet = new HashSet<T>();
		mergeSet.addAll(a);
		mergeSet.addAll(b);
		
		return mergeSet;
	}

	public Set<Project> getProjectsOfLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User Role not set."));
		
		return mergeSet(loggedUser.getCreatedProjects(), loggedUser.getParticipatingProjects());
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
	
	@Transactional
	@Modifying(clearAutomatically = true)
	public Project addOrRemoveFriendToProject(ProjectRequest projectRequest, Long id) {
		Project project = projectRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

		User memberOfProject = projectRepository
				.getMemberOfProject(projectRequest.getFriendToAddId(), id);

		if(memberOfProject == null) {	
			User friendToAdd = userRepository
					.findById(projectRequest.getFriendToAddId())
					.orElseThrow(() -> new ResourceNotFoundException("User", "id", projectRequest.getFriendToAddId()));
			project.addMember(friendToAdd);
			projectRepository.save(project);
			
		} else {
			System.out.println(memberOfProject.getEmail());
			project.removeMember(memberOfProject);
			projectRepository.save(project);
		}

		return project;
	}
}
