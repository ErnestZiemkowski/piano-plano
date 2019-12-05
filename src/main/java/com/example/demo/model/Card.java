package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@DynamicUpdate
@Table(name = "cards")
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
	private LocalDateTime createDateTime;
	
	@Size(min = 5, max = 50)
	private String title;
	
	@Size(min = 5, max = 150)	
	private String description;
	
	@Min(value = 0)
	private int position;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User creator;
	
	@ManyToOne
	@JoinColumn(name = "kanban_category_id")
	@JsonBackReference
	private KanbanCategory kanbanCategory;
	
	public Card() {}

	public Card(String title, String description, int position, User creator) {
		this.title = title;
		this.description = description;
		this.position = position;
		this.creator = creator;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
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
	
	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public KanbanCategory getKanbanCategory() {
		return kanbanCategory;
	}

	public void setKanbanCategory(KanbanCategory kanbanCategory) {
		this.kanbanCategory = kanbanCategory;
	}
	
}