package com.example.demo.model;

import java.util.HashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
		name = "users",
		uniqueConstraints = {
			@UniqueConstraint(columnNames = {
					"username"
			}),
			@UniqueConstraint(columnNames = {
					"email"
			})		
})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(min = 3, max = 50)
	private String username;

	@NaturalId
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(min = 6, max = 100)
	@JsonIgnore
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "user_roles",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
	@OneToOne(
		mappedBy = "user",
		cascade = CascadeType.ALL,
		fetch = FetchType.EAGER,
		optional = false
	)
	@JsonBackReference
	private Settings settings;
	
	@OneToMany(
		mappedBy = "creator",
		cascade = {
			CascadeType.PERSIST,
			CascadeType.REFRESH,
			CascadeType.MERGE,
			CascadeType.DETACH
		})
	private Set<Project> projects;
		
	public User() {}
	
	public static final class Builder {
		private String username;
		private String email;		
		private String password;
		private Set<Role> roles = new HashSet<>();
		private Settings settings = new Settings();
		private Set<Project> projects = new HashSet<>();
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder email(String email) {
			this.email =  email;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder addRole(Role role) {
			this.roles.add(role);
			return this;
		}
				
		public Builder addProject(Project project) {
			this.projects.add(project);
			return this;
		}
		
		public User build() {
			if(username.isEmpty()) {
				throw new IllegalStateException("Username cannot be empty");
			}

			if(email.isEmpty()) {
				throw new IllegalStateException("Username cannot be empty");
			}

			if(password.isEmpty()) {
				throw new IllegalStateException("Username cannot be empty");
			}

			User user = new User();
			user.username = this.username;
			user.email = this.email;
			user.password = this.password;
			user.roles = this.roles;
			this.settings.setUser(user);
			user.settings = this.settings;
			user.projects = this.projects;
			return user;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}

	public void removeRole(Role role) {
		this.roles.remove(role);
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
		settings.setUser(this);
	}
	
	public void addProject(Project project) {
		this.projects.add(project);
		project.setCreator(this);
	}
	
	public void removeProject(Project project) {
		this.projects.remove(project);
		project.setCreator(null);
	}
}