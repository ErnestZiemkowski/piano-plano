package com.example.demo.message.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpForm {

	@NotBlank
	@Size(min = 3, max = 50)	
	private String username;

	@NotBlank
	@Size(max = 60)
	@Email
	private String email;

	@NotBlank
	@Size(min = 6, max = 40)	
	private String password;

	private SignUpForm(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	public static SignUpForm createSignUpForm(String username, String email, String password) {
		return new SignUpForm(username, email, password);
	}
	
	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}