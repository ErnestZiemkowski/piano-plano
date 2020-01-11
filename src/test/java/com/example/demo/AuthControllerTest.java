package com.example.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.demo.exception.AppException;
import com.example.demo.message.request.LoginForm;
import com.example.demo.message.request.SignUpForm;
import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;  
	
	@Autowired
	private RoleRepository roleRepository;
	
	private static boolean initialized = false;

	@Before
	public void init() {
		if (!initialized) {
			// given
		    Role roleUser = roleRepository
		    		.findByName(RoleName.ROLE_USER)
		    		.orElseThrow(() -> new AppException("User Role not set."));

			User user = User.builder()
					.username("admin123")
					.password(new BCryptPasswordEncoder().encode("admin123"))
					.addRole(roleUser)
					.email("admin123@demo.com")
					.build();

			roleRepository.save(roleUser);
			userRepository.save(user);
			initialized = true;
		}
	}
	
	@Test
	public void signInValidUserTest() throws Exception {		
		// given
		Gson gson  = new Gson();
		LoginForm loginRequest = LoginForm.createLoginForm("admin123", "admin123");
		String jsonUser = gson.toJson(loginRequest);
		
		// when + then		
		mockMvc
			.perform(post("/api/auth/signin")
			.content(jsonUser)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").isNotEmpty())
			.andExpect(jsonPath("$.type").value("Bearer"));		
	}
	
	@Test
	public void signInAuthorizedUserInvalidPasswordTest() throws Exception {
		// given
		Gson gson = new Gson();
		LoginForm loginRequest = LoginForm.createLoginForm("adam", "badpassword");
		String jsonUser = gson.toJson(loginRequest);
		
		// when + then
		mockMvc
			.perform(post("/api/auth/signin")
			.content(jsonUser)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void signInUnauthenticatedUserInvalidPasswordTest() throws Exception {
		// given
		Gson gson = new Gson();
		LoginForm loginRequest = LoginForm.createLoginForm("nonexistinguser", "badpassword");
		String jsonUser = gson.toJson(loginRequest);
		
		// when + then
		mockMvc
			.perform(post("/api/auth/signin")
			.content(jsonUser)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
	}	
	
	@Test 
	public void signUpNonExistingUserTest() throws Exception {
		// given
		Gson gson = new Gson();
		SignUpForm registerRequest = SignUpForm.createSignUpForm("nonexistinguser", "nonexistinguser@demo.com", "nonexistinguser");
		String jsonUser = gson.toJson(registerRequest);

		// when + then
		mockMvc
			.perform(post("/api/auth/signup")
			.content(jsonUser)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("User registered successfully!"));
	}

	@Test 
	public void signUpExistingUserTest() throws Exception {
		// given
		Gson gson = new Gson();
		SignUpForm registerRequest = SignUpForm.createSignUpForm("admin123", "admin123@demo.com", "admin123");
		String jsonUser = gson.toJson(registerRequest);

		// when + then
		mockMvc
			.perform(post("/api/auth/signup")
			.content(jsonUser)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Fail -> Username is already taken!"));
	}
	
	@Test
	@WithMockUser("admin123")
	public void getCurrentLoggedInUserTest() throws Exception {
		// when + then
		mockMvc
			.perform(get("/api/auth/user/me")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("admin123"))
			.andExpect(jsonPath("$.email").value("admin123@demo.com"));
	}
}