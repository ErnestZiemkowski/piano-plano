package com.example.demo.message.request;


public class CardRequest {

	private Long id;
	
	private String title;
	
	private String description;

	private boolean isDone;
	
	private int kanbanCategoryPosition;

	private Long projectId;
		
	public CardRequest() {}

	public static final class Builder {
		private Long id;
		private String title;
		private String description;
		private boolean isDone = false;
		private Integer position;
		private Long projectId;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}
		
		public Builder isDone() {
			this.isDone = true;
			return this;
		}
		
		public Builder position(Integer position) {
			this.position = position;
			return this;
		}		

		public Builder projectId(Long projectId) {
			this.projectId = projectId;
			return this;
		}
		
		public CardRequest build() {
			if (title.isEmpty()) {
				throw new IllegalStateException("Title cannot be empty");
			}

			if (position.equals(null)) {
				throw new IllegalStateException("Password cannot be empty");
			}
			
			if (projectId.equals(null)) {
				throw new IllegalStateException("Project Id cannot be empty");				
			}

			CardRequest request = new CardRequest();
			request.id = this.id;
			request.title = this.title;
			request.description = this.description;
			request.isDone = this.isDone;
			request.kanbanCategoryPosition = this.position;
			request.projectId = this.projectId;
			return request;
		}
	}
	
	public static Builder builder() {
		return new Builder();
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