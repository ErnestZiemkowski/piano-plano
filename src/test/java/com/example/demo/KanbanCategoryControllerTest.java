package com.example.demo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

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

import com.example.demo.message.request.KanbanCategoryRequest;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KanbanCategoryControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser("adam123")
	public void getAllProjectKanbanCategoriesTest() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.get("/api/projects/{projectId}/kanban-categories", 1)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].id").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].title").value("ToDo"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[1].id").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[1].title").value("InProgress"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[2].id").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[2].title").value("Q&A"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[3].id").exists())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[3].title").value("Done"));

	}
	
	@Test
	@WithMockUser("adam123")
	public void deleteProjectTest() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders
			.delete("/api/kanban-category/{id}", 3)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.message").value("Kanban Category with id of 3 deleted successfully"));
	}
	
	@Test
	@WithMockUser("adam123")
	public void createKanbanCategoryTest() throws Exception {
		Gson gson = new Gson();
		KanbanCategoryRequest kanbanCategory = new KanbanCategoryRequest(null, "Kanban Category Test", (long) 1, 0, null, null);
		String jsonKanbanCategory = gson.toJson(kanbanCategory);
	
		mockMvc
			.perform(MockMvcRequestBuilders
			.post("/api/kanban-category")
			.content(jsonKanbanCategory)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.title").value("Kanban Category Test"));
	}
	
	@Test
	@WithMockUser("adam123")
	public void updateKanbanCategoryTest() throws Exception {
		Gson gson = new Gson();
		KanbanCategoryRequest kanbanCategory = new KanbanCategoryRequest(null, "Kanban Category Updated Test", (long) 1, 0, null, null);
		String jsonKanbanCategory = gson.toJson(kanbanCategory);

		mockMvc
			.perform(MockMvcRequestBuilders
			.put("/api/kanban-category/{id}", 1)
			.content(jsonKanbanCategory)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.title").value("Kanban Category Updated Test"));
	}
	
	@Test
	@WithMockUser("adam123")
	public void setKanbanCategoryPositionTest() throws Exception {		
		Gson gson = new Gson();
		KanbanCategoryRequest kanbanCategoryPositionThree = new KanbanCategoryRequest((long) 4, null, (long) 1, 3, null, null);
		KanbanCategoryRequest kanbanCategoryPositionTwo = new KanbanCategoryRequest((long) 3, null, (long) 1, 2, null, null);
		KanbanCategoryRequest kanbanCategoryPositionZero = new KanbanCategoryRequest((long) 1, null, (long) 1, 0, null, null);
		KanbanCategoryRequest kanbanCategoryPositionOne = new KanbanCategoryRequest((long) 2, null, (long) 1, 1, null, null);
		String jsonKanbanCategories = gson.toJson(Arrays.asList(kanbanCategoryPositionThree, kanbanCategoryPositionTwo, kanbanCategoryPositionZero, kanbanCategoryPositionOne));
		
		mockMvc
			.perform(MockMvcRequestBuilders
			.post("/api/kanban-category/rearange-position")
			.content(jsonKanbanCategories)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].id").value("4"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].title").value("Done"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[0].position").value("0"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[1].id").value("3"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[1].title").value("Q&A"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[1].position").value("1"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[2].id").value("1"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[2].title").value("ToDo"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[2].position").value("2"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[3].id").value("2"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[3].title").value("InProgress"))
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$[3].position").value("3"));
	}
	
}