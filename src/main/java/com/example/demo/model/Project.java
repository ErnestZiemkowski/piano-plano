package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@DynamicUpdate
@Table(name = "projects")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
	private LocalDateTime createDateTime;
	
	@Size(min = 5, max = 75)
	private String name;
	
	@Size(max = 500)
	private String description;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User creator;

	@OneToMany(
		fetch = FetchType.EAGER,
		mappedBy = "project", 
		cascade = CascadeType.ALL, 
		orphanRemoval = true)
	@JsonManagedReference
	private List<KanbanCategory> kanbanCategories = new ArrayList<>();
	
	public Project() {}

	public Project(String name, String description, User creator) {
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

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
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

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public List<KanbanCategory> getKanbanCategories() {
		return kanbanCategories;
	}

	public void addKanbanCategory(KanbanCategory kanbanCategory) {
		this.kanbanCategories.add(kanbanCategory);
		kanbanCategory.setProject(this);
	}

	public void removeKanbanCategory(KanbanCategory kanbanCategory) {
		this.kanbanCategories.remove(kanbanCategory);
		kanbanCategory.setProject(null);
	}
	
}
