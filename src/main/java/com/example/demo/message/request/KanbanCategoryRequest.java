package com.example.demo.message.request;

import java.util.ArrayList;
import java.util.List;

public class KanbanCategoryRequest {
	
	private Long id;
	
	private String title;
		
	private int position;

	private List<CardRequest> cards = new ArrayList<>();
	
	private Long projectId;
	
	private CardRequest cardDragged;

	public KanbanCategoryRequest() {}

	public KanbanCategoryRequest(Long id, String title, Long projectId, int position, List<CardRequest> cards, CardRequest cardDragged) {
		this.id = id;
		this.title = title;
		this.projectId = projectId;
		this.position = position;
		this.cards = cards;
		this.cardDragged = cardDragged;
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

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<CardRequest> getCards() {
		return cards;
	}

	public void setCards(List<CardRequest> cards) {
		this.cards = cards;
	}

	public CardRequest getCardDragged() {
		return cardDragged;
	}

	public void setCardDragged(CardRequest cardDragged) {
		this.cardDragged = cardDragged;
	}
	
}
