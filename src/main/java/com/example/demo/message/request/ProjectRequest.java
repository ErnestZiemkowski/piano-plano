package com.example.demo.message.request;

public class ProjectRequest {

	private String name;
	
	private String description;
	
	private Long friendToAddId;
	
	public ProjectRequest() {}

	public ProjectRequest(String name, String description) {
		this.name = name;
		this.description = description;
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

	public Long getFriendToAddId() {
		return friendToAddId;
	}

	public void setFriendToAddId(Long friendToAddId) {
		this.friendToAddId = friendToAddId;
	}

}