package com.example.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.message.request.SettingsRequest;
import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.google.gson.Gson;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SettingsControllerTest {

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
			Role roleUser = Role.createRole(RoleName.ROLE_USER);
			User user = User.builder()
					.username("adam123")
					.password("adam123")
					.addRole(roleUser)
					.email("adam123@demo.com")
					.build();
			
			roleRepository.save(roleUser);
			userRepository.save(user);
			initialized = true;
		}
	}
	
	@Test
	@WithMockUser("adam123")
	public void getSettingsByUserIdTest() throws Exception {
		// when + then
		mockMvc
			.perform(get("/api/settings")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.backgroundImageUrl").value("background-image-three"));	
	}
	
	@Test
	@WithMockUser("adam123")
	public void updateSettingsOfLoggedUserTest() throws Exception {
		// given
		Gson gson = new Gson();
		SettingsRequest settings = new SettingsRequest("background-image-three");
		String jsonSettings = gson.toJson(settings);

		// when + then
		mockMvc
			.perform(post("/api/settings")
			.content(jsonSettings)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.backgroundImageUrl").value("background-image-three"));
	}
}