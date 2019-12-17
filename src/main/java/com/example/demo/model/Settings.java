package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "settings")
public class Settings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	String backgroundImageUrl = "background-image-one";
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;
	
	public Settings() {}

	private Settings(User user) {
		this.user = user;
	}

	private Settings(User user, String backgroundImageUrl) {
		this.user = user;
		this.backgroundImageUrl = backgroundImageUrl;
	}
	
	public static Settings createSettings() {
		return new Settings();
	}

	public static Settings createSettings(User user) {
		return new Settings(user);
	}

	public static Settings createSettings(User user, String backgroundImageUrl) {
		return new Settings(user, backgroundImageUrl);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBackgroundImageUrl() {
		return backgroundImageUrl;
	}

	public void setBackgroundImageUrl(String backgroundImageUrl) {
		this.backgroundImageUrl = backgroundImageUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}