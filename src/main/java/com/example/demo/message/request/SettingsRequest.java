package com.example.demo.message.request;

public class SettingsRequest {

	private String backgroundImageUrl;
	
	public SettingsRequest() {}

	public SettingsRequest(String backgroundImageUrl) {
		this.backgroundImageUrl = backgroundImageUrl;
	}

	public String getBackgroundImageUrl() {
		return backgroundImageUrl;
	}

	public void setBackgroundImageUrl(String backgroundImageUrl) {
		this.backgroundImageUrl = backgroundImageUrl;
	}
}