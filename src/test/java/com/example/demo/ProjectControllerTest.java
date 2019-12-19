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

import com.example.demo.message.request.ProjectRequest;
import com.example.demo.model.Project;
import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.google.gson.Gson;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ProjectControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProjectRepository projectRepository;

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
			User adam = User.builder()
					.username("adam123")
					.password(new BCryptPasswordEncoder().encode("adam123"))
					.addRole(roleUser)
					.email("adam123@demo.com")
					.build();
			
			User eve = User.builder()
					.username("eve69")
					.password(new BCryptPasswordEncoder().encode("eve$69"))
					.addRole(roleUser)
					.email("eve69@demo.com")
					.build();
					
			Project projectOne = Project.builder()
					.name("Project test one")
					.description("test")
					.creator(adam)
					.build();

			Project projectTwo = Project.builder()
					.name("Project test two")
					.description("test")
					.creator(eve)
					.build();

			Project projectThree = Project.builder()
					.name("Project test three")
					.description("test")
					.creator(eve)
					.addMember(adam)
					.build();

			roleRepository.save(roleUser);
			userRepository.saveAll(Arrays.asList(adam, eve));
			projectRepository.saveAll(Arrays.asList(projectOne, projectTwo, projectThree));		
			initialized = true;
		}
	}

	@Test
	@WithMockUser("adam123")
	public void getAllProjectsTest() throws Exception {	
		// when + then
		mockMvc
			.perform(get("/api/projects")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name").value("Project test one"))
			.andExpect(jsonPath("$[0].description").exists())
			.andExpect(jsonPath("$[0].creator").exists())
			.andExpect(jsonPath("$[1].name").value("Project Create Test"))
			.andExpect(jsonPath("$[1].description").exists())
			.andExpect(jsonPath("$[1].creator").exists());
	}
	
	@Test
	@WithMockUser("adam123")
	public void deleteProjectTest() throws Exception {		
		// when + then
		mockMvc
			.perform(delete("/api/projects/{id}", 2)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("Project with id of 2 deleted successfully"));
	}

	@Test
	@WithMockUser("adam123")
	public void createProjectTest() throws Exception {
		// given
		Gson gson = new Gson();
		ProjectRequest project = new ProjectRequest("Project Create Test", "This is create test description");
		String jsonProject = gson.toJson(project);
		
		// when + then
		mockMvc
			.perform(post("/api/projects")
			.content(jsonProject)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Project Create Test"))
			.andExpect(jsonPath("$.description").value("This is create test description"));
	}

	@Test
	@WithMockUser("adam123")
	public void updateUserTest() throws Exception {
		// given
		Gson gson = new Gson();
		ProjectRequest project = new ProjectRequest(null, "This is updated description");
		String jsonProject = gson.toJson(project);

		// when + then		
		mockMvc
			.perform(put("/api/projects/{id}", 1)
			.content(jsonProject)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.description").value("This is updated description"));
	}	
}
