package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;

@Entity
@DynamicUpdate
@Table(name = "cards")
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
 	
	@Size(min = 5, max = 75)
	private String title;
	
  	@Size(max = 500)	
	private String description;
	
	private boolean isDone;
	
	@Min(value = 0)
	private int position;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User creator;
	
	@ManyToOne
	@JoinColumn(name = "kanban_category_id")
	@JsonBackReference
	private KanbanCategory kanbanCategory;
	
	@OneToOne(
		mappedBy = "card",
		cascade = CascadeType.ALL,
		fetch = FetchType.EAGER
	)
	@JsonBackReference
	private DailyGoal dailyGoal;
	
	@OneToMany(
		fetch = FetchType.EAGER,
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	private Set<Comment> comments;
	
	public Card() {}

	public static final class Builder {
		private String title;
		private String description;
		private boolean isDone = false;
		private Integer position;
		private User creator;
		private KanbanCategory kanbanCategory;
		private Set<Comment> comments = new HashSet<>();
		
		public Builder title(String name) {
			this.title = name;
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
		
		public Builder creator(User creator) {
			this.creator = creator;
			return this;
		}
		
		public Builder kanbanCategory(KanbanCategory kanbanCategory) {
			this.kanbanCategory = kanbanCategory;
			return this;
		}
		
		public Builder addComment(Comment comment) {
			this.comments.add(comment);
			return this;
		}
		
		public Card build() {
			if (title.isEmpty()) {
				throw new IllegalStateException("Title cannot be empty");
			}

			if (position.equals(null)) {
				throw new IllegalStateException("Password cannot be empty");
			}
			
			if (kanbanCategory.equals(null)) {
				throw new IllegalStateException("Kanban Category cannot be empty");
			}

			Card card = new Card();
			card.title = this.title;
			card.description = this.description;
			card.isDone = this.isDone;
			card.position = this.position;
			card.creator = this.creator;
			kanbanCategory.addCard(card);
			card.kanbanCategory = this.kanbanCategory;
			card.comments = this.comments;
			return card;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
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
	
	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
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
	
	public DailyGoal getDailyGoal() {
		return dailyGoal;
	}

	public void setDailyGoal(DailyGoal dailyGoal) {
		this.dailyGoal = dailyGoal;
	}
	
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public void removeComment(Comment comment) {
		this.comments.remove(comment);
	}

	@JsonGetter
	public String getCardCode() {
		String cardCode = kanbanCategory.getProject().getName().replaceAll("[AaEeIiOoUu ]", "");
		String number = String.valueOf(this.getId());
		cardCode = cardCode.substring(0, 3);
		cardCode = cardCode.concat("-");
		cardCode = cardCode.concat(number);
		cardCode = cardCode.toLowerCase();
		return cardCode;
	}
	
	@JsonGetter
	public boolean isDailyGoalSet() {
		return this.dailyGoal != null ? true : false;
	}
	
	@JsonGetter
	public String getKanbanCategoryTitle() {
		return kanbanCategory.getTitle();
	}

}