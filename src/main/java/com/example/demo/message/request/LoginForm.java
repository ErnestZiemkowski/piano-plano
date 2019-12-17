package com.example.demo.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginForm {

	@NotBlank
	@Size(min = 3, max = 50)
	private String username;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	public LoginForm() {}

	private LoginForm(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public static LoginForm createLoginForm(String username, String password) {
		return new LoginForm(username, password);
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}
}