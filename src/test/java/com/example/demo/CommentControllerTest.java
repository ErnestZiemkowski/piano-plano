package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import com.example.demo.message.request.CommentRequest;
import com.example.demo.model.Card;
import com.example.demo.model.CommentType;
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
public class CommentControllerTest {

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
	public void createCardCommentTest() throws Exception {
		// given 
		Gson gson = new Gson();
		CommentRequest comment = CommentRequest.createCommentRequest("This is test comment", CommentType.COMMENT_CARD, (long) 2);
		String jsonComment = gson.toJson(comment);
		
		// when + then
		mockMvc
			.perform(post("/api/comment")
			.content(jsonComment)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.content").value("This is test comment"))
			.andExpect(jsonPath("$.createdAt").exists())
			.andExpect(jsonPath("$.updatedAt").exists());
	}
	
	@Test
	@WithMockUser("adam123")
	public void createProjectCommentTest() throws Exception {
		// given 
		Gson gson = new Gson();
		CommentRequest comment = CommentRequest.createCommentRequest("This is test comment", CommentType.COMMENT_PROJECT, (long) 1);
		String jsonComment = gson.toJson(comment);
		
		// when + then
		mockMvc
			.perform(post("/api/comment")
			.content(jsonComment)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.content").value("This is test comment"))
			.andExpect(jsonPath("$.createdAt").exists())
			.andExpect(jsonPath("$.updatedAt").exists());
	}	
}