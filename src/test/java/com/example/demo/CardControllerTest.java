package com.example.demo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

import com.example.demo.message.request.CardRequest;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser("adam123")
	public void deleteCardTest() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders
			.delete("/api/cards/{id}", 7)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.message").value("Card with id of 7 deleted successfully"));
	}

	
	@Test
	@WithMockUser("adam123")
	public void createCardTest() throws Exception {
		Gson gson = new Gson();
		CardRequest card = new CardRequest(null, "Test Card title", "Test Card description", 1, (long) 1);
		String jsonCard = gson.toJson(card);
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.post("/api/cards")
			.content(jsonCard)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.id").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.createDateTime").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.title").value("Test Card title"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.description").value("Test Card description"));
	}
	
	@Test
	@WithMockUser("adam123")
	public void updateCardTest() throws Exception {
		Gson gson = new Gson();
		CardRequest card = new CardRequest(null, "Test Card UPDATED", "Test Card UPDATED", 1, (long) 1);
		String jsonCard = gson.toJson(card);

		mockMvc
			.perform(MockMvcRequestBuilders
			.put("/api/cards/{id}", 1)
			.content(jsonCard)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.id").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.createDateTime").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.title").value("Test Card UPDATED"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.description").value("Test Card UPDATED"));
	}
}