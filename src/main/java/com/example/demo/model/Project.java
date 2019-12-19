package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	
	@ManyToOne(cascade = {
		CascadeType.PERSIST,
		CascadeType.MERGE
	})
	@JoinColumn(name = "user_id")
	@JsonManagedReference
	private User creator;

	@ManyToMany(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE})
	@JoinTable(
		name = "project_members",
		joinColumns = @JoinColumn(name = "project_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id"))
	@JsonManagedReference
	private Set<User> members;
	
	@OneToMany(
		fetch = FetchType.EAGER,
		mappedBy = "project", 
		cascade = CascadeType.ALL, 
		orphanRemoval = true)
	@JsonManagedReference
	private List<KanbanCategory> kanbanCategories;
	
	public Project() {}
	
	public static final class Builder {
		private String name;
		private String description;
		private User creator;
		private List<KanbanCategory> kanbanCategories = new ArrayList<>();
		private Set<User> members = new HashSet<>();
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder description(String description) {
			this.description = description;
			return this;
		}
		
		public Builder creator(User creator) {
			this.creator = creator;
			return this;
		}
		
		public Builder addKanbanCategory(KanbanCategory kanbanCategory) {
			this.kanbanCategories.add(kanbanCategory);
			return this;
		}
		
		public Builder addMember(User member) {
			this.members.add(member);
			return this;
		}
		
		public Project build() {
			if (name.isEmpty()) {
				throw new IllegalStateException("Title cannot be empty");
			}
			
			if (creator == null) {
				throw new IllegalStateException("Creator cannot be empty");
			}

			Project project = new Project();
			project.name = this.name;
			project.description = this.description;
			project.creator = this.creator;
			this.creator.addCreatedProject(project);
			project.kanbanCategories = this.kanbanCategories;
			project.members = this.members;
			return project;
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
	
	public void addMember(User member) {
		this.members.add(member);
		member.addCreatedProject(this);
	}

	public void removeMember(User member) {
		this.members.remove(member);
		member.addCreatedProject(null);
	}

}