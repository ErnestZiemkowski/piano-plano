package com.example.demo.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@DynamicUpdate
@Table(name="kanban_categories")
public class KanbanCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Size(min = 2, max = 30)
	private String title;
	
	@Min(value = 0)
	private int position;

	@OneToMany(
		fetch = FetchType.EAGER,
		mappedBy = "kanbanCategory",
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	@JsonManagedReference
	private List<Card> cards = new ArrayList<>();
	
	@ManyToOne()
	@JoinColumn(name = "project_id")
	@JsonBackReference
	private Project project;
	
	public KanbanCategory() {}

	public KanbanCategory(String title, int position) {
		this.title = title;
		this.position = position;
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
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	public void addCard(Card card) {
		this.cards.add(card);
		card.setKanbanCategory(this);
	}

	public void removeCard(Card card) {
		this.cards.remove(card);
		card.setKanbanCategory(null);
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
}
