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

import com.example.demo.message.request.SettingsRequest;
import com.google.gson.Gson;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SettingsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser("adam123")
	public void getSettingsByUserIdTest() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders
			.get("/api/settings")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.backgroundImageUrl").value("background-1.png"));	
	}
	
	@Test
	@WithMockUser("adam123")
	public void updateSettingsOfLoggedUserTest() throws Exception {
		Gson gson = new Gson();
		SettingsRequest settings = new SettingsRequest("background-image-three");
		String jsonSettings = gson.toJson(settings);
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.post("/api/settings")
			.content(jsonSettings)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.backgroundImageUrl").value("background-image-three"));
	}
}
