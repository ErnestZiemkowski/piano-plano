package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import com.example.demo.message.request.LoginForm;
import com.example.demo.message.request.SignUpForm;
import com.example.demo.model.RoleName;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void signInValidUserTest() throws Exception {
		
		Gson gson  = new Gson();
		LoginForm loginRequest = new LoginForm("admin123", "admin123");
		String jsonUser = gson.toJson(loginRequest);
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.post("/api/auth/signin")
			.content(jsonUser)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty())
			.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Bearer"));		
	}
	
	@Test
	public void signInAuthorizedUserInvalidPasswordTest() throws Exception {
		
		Gson gson = new Gson();
		LoginForm loginRequest = new LoginForm("adam", "badpassword");
		String jsonUser = gson.toJson(loginRequest);
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.post("/api/auth/signin")
			.content(jsonUser)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void signInUnauthenticatedUserInvalidPasswordTest() throws Exception {
		
		Gson gson = new Gson();
		LoginForm loginRequest = new LoginForm("nonexistinguser", "badpassword");
		String jsonUser = gson.toJson(loginRequest);
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.post("/api/auth/signin")
			.content(jsonUser)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());

	}	
	
	@Test 
	public void signUpNonExistingUserTest() throws Exception {
		
		Gson gson = new Gson();
		SignUpForm registerRequest = new SignUpForm("nonexistinguser", "nonexistinguser@demo.com", "nonexistinguser");
		String jsonUser = gson.toJson(registerRequest);

		mockMvc
			.perform(MockMvcRequestBuilders
			.post("/api/auth/signup")
			.content(jsonUser)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully!"));

	}

	@Test 
	public void signUpExistingUserTest() throws Exception {
		
		Gson gson = new Gson();
		SignUpForm registerRequest = new SignUpForm("adam123", "adam@demo.com", "adam123");
		String jsonUser = gson.toJson(registerRequest);

		mockMvc
			.perform(MockMvcRequestBuilders
			.post("/api/auth/signup")
			.content(jsonUser)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Fail -> Username is already taken!"));

	}
	
	@Test
	@WithMockUser("adam123")
	public void getCurrentLoggedInUserTest() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.get("/api/auth/user/me")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.username").value("adam123"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.email").value("adam@demo.com"));
		
	}
	
	
}
