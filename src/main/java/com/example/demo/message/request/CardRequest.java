package com.example.demo.message.request;

public class CardRequest {

	private Long id;
	
	private String title;
	
	private String description;
	
	private int kanbanCategoryPosition;

	private Long projectId;
	
	public CardRequest() {}

	public CardRequest(Long id, String title, String description, int kanbanCategoryPosition, Long projectId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.kanbanCategoryPosition = kanbanCategoryPosition;
		this.projectId = projectId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getKanbanCategoryPosition() {
		return kanbanCategoryPosition;
	}

	public void setKanbanCategoryPosition(int kanbanCategoryPosition) {
		this.kanbanCategoryPosition = kanbanCategoryPosition;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

}
