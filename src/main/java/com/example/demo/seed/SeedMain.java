package com.example.demo.seed;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Card;
import com.example.demo.model.KanbanCategory;
import com.example.demo.model.Project;
import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.Settings;
import com.example.demo.model.User;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Component
public class SeedMain {

	private static final Logger logger = LoggerFactory.getLogger(SeedMain.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository  roleRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private CardRepository cardRepository;
	
	private Role roleAdmin = new Role();
	private Role roleUser = new Role();
	
	private User admin = new User();
	private User userAdam = new User();
	
	private Settings settingsOne = new Settings();
	private Settings settingsTwo = new Settings();
	
	private Project projectOne = new Project();
	private Project projectTwo = new Project();
	private Project projectThree = new Project();
	private Project projectFour = new Project();
	private Project projectFive = new Project();
	private Project projectSix = new Project();
	
	private Card cardOne = new Card("Card One", "Description", 1, null);
	private Card cardTwo = new Card("Card Two", "Description", 2, null);
	private Card cardThree = new Card("Card Three", "Description", 3, null);
	
	private Card cardFour = new Card("Card Four", "Description", 1, null);
	private Card cardFive = new Card("Card Five", "Description", 2, null);

	private Card cardSix = new Card("Card Six", "Description", 1, null);
	private Card cardSeven = new Card("Card Seven", "Description", 2, null);

	
	private KanbanCategory categoryTodo = new KanbanCategory("ToDo", 4);
	private KanbanCategory categoryInProgress = new KanbanCategory("InProgress", 3);
	private KanbanCategory categoryQA = new KanbanCategory("Q&A", 2);
	private KanbanCategory categoryDone = new KanbanCategory("Done", 1);
	
	
	@EventListener
	public void seed(ContextRefreshedEvent event) {
		seedRolesTable();
		seedUsersTable();
		seedProjectsTable();
		seedCardsTable();
	}
	
	private void seedRolesTable() {
		List<Role> roles = roleRepository.findAll();
		
		if (roles.size() > 0 || roles != null) {
            logger.info("Users Seeding Not Required");
		} 
		roleAdmin.setName(RoleName.ROLE_ADMIN);
		roleUser.setName(RoleName.ROLE_USER);
	
		roleRepository.saveAll(Arrays.asList(roleAdmin, roleUser));
	}
	
	private void seedUsersTable() {
		List<User> users = userRepository.findAll();
		
		if (users.size() > 0 || users != null) {	
            logger.info("Users Seeding Not Required");
		} 
		admin.setUsername("admin123");
		admin.setEmail("admin@demo.com");
		admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
		admin.addRole(roleUser);
		admin.addRole(roleAdmin);
		settingsOne.setUser(admin);
		admin.setSettings(settingsOne);
		
		userAdam.setUsername("adam123");
		userAdam.setEmail("adam@demo.com");
		userAdam.setPassword(new BCryptPasswordEncoder().encode("adam123"));
		userAdam.addRole(roleUser);
		settingsTwo.setUser(userAdam);
		userAdam.setSettings(settingsTwo);
					
		userRepository.saveAll(Arrays.asList(admin, userAdam));
	}
	
	private void seedProjectsTable() {
		List<Project> projects = projectRepository.findAll();
		
		if (projects.size() > 0 || projects != null) {
			logger.info("Projects Seeding Not Required");
		}
		projectOne.setName("Project One");
		projectOne.setDescription("This is Project One");
		projectOne.setCreator(userAdam);
		projectOne.addKanbanCategory(categoryTodo);
		projectOne.addKanbanCategory(categoryInProgress);
		projectOne.addKanbanCategory(categoryQA);
		projectOne.addKanbanCategory(categoryDone);
		
		projectTwo.setName("Project Two");
		projectTwo.setDescription("This is Project Two");
		projectTwo.setCreator(userAdam);

		projectThree.setName("Project Three");
		projectThree.setDescription("This is Project Three");
		projectThree.setCreator(userAdam);

		projectFour.setName("Project Four");
		projectFour.setDescription("This is Project Four");
		projectFour.setCreator(userAdam);

		projectFive.setName("Project Five");
		projectFive.setDescription("This is Project Five");
		projectFive.setCreator(userAdam);
		
		projectSix.setName("Project Six");
		projectSix.setDescription("This is Project Six");
		projectSix.setCreator(userAdam);
		
		projectRepository.saveAll(Arrays.asList(projectOne, projectTwo, projectThree, projectFour, projectFive, projectSix));
	}
	
	private void seedCardsTable() {
		List<Card> cards = cardRepository.findAll();
		
		if (cards.size() > 0 || cards != null) {
			logger.info("Cards Seeding Not Required");			
		}
		categoryDone.addCard(cardOne);
		categoryDone.addCard(cardTwo);
		categoryDone.addCard(cardThree);
		
		categoryInProgress.addCard(cardFour);
		categoryInProgress.addCard(cardFive);
		
		categoryQA.addCard(cardSix);
		categoryQA.addCard(cardSeven);
		
		cardRepository.saveAll(Arrays.asList(cardOne, cardTwo, cardThree, cardFour, cardFive, cardSix, cardSeven));
	}
}
