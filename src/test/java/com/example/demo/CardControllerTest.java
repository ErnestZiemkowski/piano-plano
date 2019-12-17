package com.example.demo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.message.request.CardRequest;
import com.example.demo.model.Card;
import com.example.demo.model.KanbanCategory;
import com.example.demo.model.Project;
import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.User;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.KanbanCategoryRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CardControllerTest {

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

	@Autowired
	private CardRepository cardRepository;
	
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
			
			Card cardOne = Card.builder()
					.creator(user)
					.title("Card One")
					.description("Card one description")
					.position(0)
					.kanbanCategory(categoryToDo)
					.build();

			Card cardTwo = Card.builder()
					.creator(user)
					.title("Card Two")
					.description("Card two description")
					.position(1)
					.kanbanCategory(categoryToDo)
					.build();

			
			roleRepository.save(roleUser);
			userRepository.save(user);
			kanbanCategoryRepository.saveAll(Arrays.asList(categoryToDo, categoryInProgress, categoryDone));
			cardRepository.saveAll(Arrays.asList(cardOne, cardTwo));
			projectRepository.save(projectOne);
			initialized = true;
		}
	}
	
	@Test
	@WithMockUser("adam123")
	public void deleteCardTest() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders
			.delete("/api/cards/{id}", 2)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.message").value("Card with id of 2 deleted successfully"));
	}

	
	@Test
	@WithMockUser("adam123")
	public void createCardTest() throws Exception {
		// given
		Gson gson = new Gson();
		CardRequest card = CardRequest.builder()
				.title("Test Card title")
				.description("Test Card description")
				.position(1)
				.projectId((long) 1)
				.build();
				
		String jsonCard = gson.toJson(card);
		
		// when + then
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
		// given
		Gson gson = new Gson();
		CardRequest card = CardRequest.builder()
				.title("Test Card UPDATED")
				.description("Test Card UPDATED")
				.position(1)
				.projectId((long) 1)
				.build();

		String jsonCard = gson.toJson(card);

		// when + then
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