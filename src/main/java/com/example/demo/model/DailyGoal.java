package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@DynamicUpdate
@Table(name = "daily_goals")
public class DailyGoal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	private Date expiresAt;
	
	@OneToOne(
		fetch = FetchType.EAGER,
		cascade = CascadeType.ALL
	)
	@JoinColumn(name = "card_id")
	@JsonManagedReference
	private Card card;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public DailyGoal() {}
	
	private DailyGoal(Card card, User user) {
		this.card = card;
		this.user = user;
	}
	
	public static DailyGoal createDailyGoal(Card card, User user) {
		return new DailyGoal(card, user);
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

	public Date getExpiresAt() {
		return expiresAt;
	}

	@SuppressWarnings("deprecation")
	public void setExpiresAt() {
		Date now = new Date();
		this.expiresAt = new Date(now.getYear(), now.getMonth(), now.getDate(), 24, 59, 59);
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}