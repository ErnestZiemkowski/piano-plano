package com.example.demo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;

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

import com.example.demo.message.request.KanbanCategoryRequest;
import com.example.demo.model.KanbanCategory;
import com.example.demo.model.Project;
import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.User;
import com.example.demo.repository.KanbanCategoryRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class KanbanCategoryControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private KanbanCategoryRepository kanbanCategoryRepository;

	private static boolean initialized = false;

	@Before
	public void init() {
		if (!initialized) {
			// given
			Role roleUser = Role.createRole(RoleName.ROLE_USER);
			User user = User.builder()
					.username("adam123")
					.password(new BCryptPasswordEncoder().encode("adam123"))
					.addRole(roleUser)
					.email("adam123@demo.com")
					.build();

			KanbanCategory categoryToDo = KanbanCategory.createKanbanCategory("To Do", 1);
			KanbanCategory categoryInProgress = KanbanCategory.createKanbanCategory("In Progress", 2);
			KanbanCategory categoryDone = KanbanCategory.createKanbanCategory("Done", 3);

			Project projectOne = Project.builder()
					.name("Project test one")
					.description("test")
					.creator(user)
					.addKanbanCategory(categoryToDo)
					.addKanbanCategory(categoryInProgress)
					.addKanbanCategory(categoryDone)
					.build();

			categoryToDo.setProject(projectOne);
			categoryInProgress.setProject(projectOne);
			categoryDone.setProject(projectOne);
			
			roleRepository.save(roleUser);
			userRepository.save(user);
			kanbanCategoryRepository.saveAll(Arrays.asList(categoryToDo, categoryInProgress, categoryDone));
			projectRepository.save(projectOne);
			initialized = true;
		}
	}

	@Test
	@WithMockUser("adam123")
	public void getAllProjectKanbanCategoriesTest() throws Exception {
		// when + test
		mockMvc
			.perform(get("/api/projects/{projectId}/kanban-categories", 1)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(1))
			.andExpect(jsonPath("$[0].title").value("To Do"))
			.andExpect(jsonPath("$[1].id").value(2))
			.andExpect(jsonPath("$[1].title").value("In Progress"))
			.andExpect(jsonPath("$[2].id").value(3))
			.andExpect(jsonPath("$[2].title").value("Done"));
	}
	
	@Test
	@WithMockUser("adam123")
	public void deleteKanbanCategoryTest() throws Exception {
		// when + test
		mockMvc
			.perform(delete("/api/kanban-category/{id}", 4)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("Kanban Category with id of 4 deleted successfully"));
	}
	
	@Test
	@WithMockUser("adam123")
	public void createKanbanCategoryTest() throws Exception {
		// given
		Gson gson = new Gson();
		KanbanCategoryRequest kanbanCategory = KanbanCategoryRequest.builder()
				.title("Kanban Category Test")
				.projectId((long) 1)
				.position(0)
				.build();
		
		String jsonKanbanCategory = gson.toJson(kanbanCategory);

		// when + test
		mockMvc
			.perform(post("/api/kanban-category")
			.content(jsonKanbanCategory)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Kanban Category Test"));
	}
	
	@Test
	@WithMockUser("adam123")
	public void updateKanbanCategoryTest() throws Exception {
		// given
		Gson gson = new Gson();
		KanbanCategoryRequest kanbanCategory = KanbanCategoryRequest.builder()
				.title("Kanban Category Updated Test")
				.projectId((long) 1)
				.position(0)
				.build();
		
		String jsonKanbanCategory = gson.toJson(kanbanCategory);

		// when + test
		mockMvc
			.perform(put("/api/kanban-category/{id}", 3)
			.content(jsonKanbanCategory)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Kanban Category Updated Test"));
	}
	
	@Test
	@WithMockUser("adam123")
	public void setKanbanCategoryPositionTest() throws Exception {		
		// given
		Gson gson = new Gson();
		KanbanCategoryRequest kanbanCategoryPositionZero = KanbanCategoryRequest.builder()
				.id((long) 2)
				.projectId((long) 1)
				.position(0)
				.build();

		KanbanCategoryRequest kanbanCategoryPositionOne = KanbanCategoryRequest.builder()
				.id((long) 1)
				.projectId((long) 1)
				.position(1)
				.build();

		
		String jsonKanbanCategories = gson.toJson(Arrays.asList(kanbanCategoryPositionZero, kanbanCategoryPositionOne));
		
		// when + test
		mockMvc
			.perform(post("/api/kanban-category/rearange-position")
			.content(jsonKanbanCategories)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(2))
			.andExpect(jsonPath("$[0].title").value("In Progress"))
			.andExpect(jsonPath("$[0].position").value(0))
			.andExpect(jsonPath("$[1].id").value(1))
			.andExpect(jsonPath("$[1].title").value("To Do"))
			.andExpect(jsonPath("$[1].position").value(1));
	}
}