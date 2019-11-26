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

import com.example.demo.message.request.ProjectRequest;
import com.google.gson.Gson;

import jdk.net.SocketFlow.Status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser("adam123")
	public void getAllProjectsTest() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.get("/api/projects")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].id").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].startDate").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].name").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].description").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].creator").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[1].id").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[1].startDate").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[1].name").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[1].description").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[1].creator").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[2].id").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[2].startDate").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[2].name").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[2].description").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[2].creator").exists());

	}
	
	@Test
	@WithMockUser("adam123")
	public void deleteProjectTest() throws Exception {
	
		mockMvc
			.perform(MockMvcRequestBuilders
			.delete("/api/projects/{id}", 6)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.message").value("Project with id of 6 deleted successfully"));

	}

	@Test
	@WithMockUser("adam123")
	public void createProjectTest() throws Exception {
	
		Gson gson = new Gson();
		ProjectRequest project = new ProjectRequest("Project Test", "This is test description");
		String jsonProject = gson.toJson(project);
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.post("/api/projects")
			.content(jsonProject)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.name").value("Project Test"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.description").value("This is test description"));
		
	}

	@Test
	@WithMockUser("adam123")
	public void updateUserTest() throws Exception {
		
		Gson gson = new Gson();
		ProjectRequest project = new ProjectRequest(null, "This is updated description");
		String jsonProject = gson.toJson(project);
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.put("/api/projects/{id}", 1)
			.content(jsonProject)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.description").value("This is updated description"));
		
	}	
	
	
}
