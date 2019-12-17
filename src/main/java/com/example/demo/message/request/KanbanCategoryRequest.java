package com.example.demo.message.request;

import java.util.ArrayList;
import java.util.List;

public class KanbanCategoryRequest {
	
	private Long id;
	
	private String title;
		
	private Long projectId;

	private int position;

	private List<CardRequest> cards = new ArrayList<>();
	
	private CardRequest cardDragged;
	
	public KanbanCategoryRequest() {}

	public static final class Builder {
		private Long id;
		private String title;
		private Long projectId;
		private int position = -1;
		private List<CardRequest> cards = new ArrayList<>();
		private CardRequest cardDragged;
		
		public Builder id(Long id) {
			this.id = id;
			return this;
		}
		
		public Builder title(String title) {
			this.title = title;
			return this;
		}
		
		public Builder projectId(Long projectId) {
			this.projectId = projectId;
			return this;
		}
		
		public Builder position(int position) {
			this.position = position;
			return this;
		}
		
		public Builder addCard(CardRequest card) {
			this.cards.add(card);
			return this;
		}
		
		public Builder cardDragged(CardRequest cardRequest) {
			this.cardDragged = cardRequest;
			return this;
		}
		
		public KanbanCategoryRequest build() {
			if (projectId == 0) {
				throw new IllegalStateException("Project Id cannot be empty");
			}

			if (position == -1) {
				throw new IllegalStateException("Position cannot be empty");				
			}

			KanbanCategoryRequest request = new KanbanCategoryRequest();
			request.id = this.id;
			request.title = this.title;
			request.projectId = this.projectId;
			request.position = this.position;
			request.cards = this.cards;
			request.cardDragged = this.cardDragged;
			return request;
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Long getProjectId() {
		return projectId;
	}

	public int getPosition() {
		return position;
	}

	public List<CardRequest> getCards() {
		return cards;
	}
	
	public CardRequest getCardDragged() {
		return cardDragged;
	}	
}