package com.example.demo.message.response;

import java.time.LocalDateTime;

public class ProjectResponse {

	private Long id;
	
	private LocalDateTime startDate;
	
	private String name;
	
	private String description;
	
	private UserProfile creator;

	public ProjectResponse() {}
	
	public ProjectResponse(Long id, LocalDateTime startDate, String name, String description, UserProfile creator) {
		this.id = id;
		this.startDate = startDate;
		this.name = name;
		this.description = description;
		this.creator = creator;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserProfile getCreator() {
		return creator;
	}

	public void setCreator(UserProfile creator) {
		this.creator = creator;
	}

	
}
