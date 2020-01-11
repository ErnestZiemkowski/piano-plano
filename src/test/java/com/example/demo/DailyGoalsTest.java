package com.example.demo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

import com.example.demo.exception.AppException;
import com.example.demo.message.request.DailyGoalRequest;
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
public class DailyGoalsTest {

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
		    Role roleUser = roleRepository
		    		.findByName(RoleName.ROLE_USER)
		    		.orElseThrow(() -> new AppException("User Role not set."));
		    
			User user = User.builder()
					.username("adam123")
					.password(new BCryptPasswordEncoder().encode("adam123"))
					.addRole(roleUser)
					.email("adam123@demo.com")
					.build();

			KanbanCategory categoryToDo = KanbanCategory.createKanbanCategory("To Do", 1);

			Project projectOne = Project.builder()
					.name("Project test one")
					.description("test")
					.creator(user)
					.addKanbanCategory(categoryToDo)
					.build();

			categoryToDo.setProject(projectOne);
			
			Card cardOne = Card.builder()
					.creator(user)
					.title("Card One")
					.description("Card one description")
					.position(0)
					.kanbanCategory(categoryToDo)
					.build();
			
			roleRepository.save(roleUser);
			userRepository.save(user);
			kanbanCategoryRepository.save(categoryToDo);
			cardRepository.save(cardOne);
			projectRepository.save(projectOne);
			initialized = true;
		}
	}
	
	@Test
	@WithMockUser("adam123")
	public void getActiveDailyGoalsTest() throws Exception {
		// when + then
		mockMvc
			.perform(get("/api/daily-goals")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(1)));
	}

	@Test
	@WithMockUser("adam123")
	public void toggleSettingCardAsDailyGoal() throws Exception {
		// given
		Gson gson = new Gson();
		DailyGoalRequest request = new DailyGoalRequest();
		request.setCardId((long) 1);
		String jsonDailyGoal = gson.toJson(request);
		
		// when + then
		mockMvc
			.perform(post("/api/daily-goals")
			.content(jsonDailyGoal)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
	}
}