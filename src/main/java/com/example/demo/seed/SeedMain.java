//package com.example.demo.seed;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import com.example.demo.model.Card;
//import com.example.demo.model.KanbanCategory;
//import com.example.demo.model.Project;
//import com.example.demo.model.Role;
//import com.example.demo.model.RoleName;
//import com.example.demo.model.User;
//import com.example.demo.repository.CardRepository;
//import com.example.demo.repository.KanbanCategoryRepository;
//import com.example.demo.repository.ProjectRepository;
//import com.example.demo.repository.RoleRepository;
//import com.example.demo.repository.UserRepository;
//
//import net.bytebuddy.asm.Advice.This;
//
//@Component
//public class SeedMain {
//
//	private static final Logger logger = LoggerFactory.getLogger(SeedMain.class);
//	
//	@Autowired
//	private UserRepository userRepository;
//	
//	@Autowired
//	private RoleRepository  roleRepository;
//	
//	@Autowired
//	private ProjectRepository projectRepository;
//	
//	@Autowired
//	private CardRepository cardRepository;
//	
//	@Autowired
//	private KanbanCategoryRepository kanbanCategoryRepository;
//	
//	private Role roleAdmin = Role.createRole(RoleName.ROLE_ADMIN);
//	private Role roleUser = Role.createRole(RoleName.ROLE_USER);
//	
//	private User admin = User.builder()
//			.username("admin123")
//			.email("admin@demo.com")
//			.password(new BCryptPasswordEncoder().encode("admin123"))
//			.addRole(roleAdmin)
//			.addRole(roleUser)
//			.build();
//	
//	private User adam = User.builder()
//			.username("adam123")
//			.email("adam@demo.com")
//			.password(new BCryptPasswordEncoder().encode("adam123"))
//			.addRole(roleUser)
//			.build();
//
//	private User eve = User.builder()
//			.username("eve123")
//			.email("eve@demo.com")
//			.password(new BCryptPasswordEncoder().encode("eve123"))
//			.addRole(roleUser)
//			.build();
//			
//	private KanbanCategory categoryToDo = KanbanCategory.createKanbanCategory("To Do", 1);
//	private KanbanCategory categoryInProgress = KanbanCategory.createKanbanCategory("In Progress", 2);
//	private KanbanCategory categoryQA = KanbanCategory.createKanbanCategory("Q&A", 3);
//	private KanbanCategory categoryDone = KanbanCategory.createKanbanCategory("Done", 4);
//	
//	private Project projectOne = Project.builder()
//			.name("Project One")
//			.description("This is description of project one")
//			.creator(adam)
//			.addKanbanCategory(categoryToDo)
//			.addKanbanCategory(categoryInProgress)
//			.addKanbanCategory(categoryQA)
//			.addKanbanCategory(categoryDone)
//			.build();
//	
//	private Project projectTwo = Project.builder()
//			.name("Project Two")
//			.description("This is description of project two")
//			.creator(eve)
//			.build();
//			
//	private Card cardOne = Card.builder()
//			.title("Card one")
//			.description("Description one")
//			.position(1)
//			.creator(adam)
//			.kanbanCategory(categoryToDo)
//			.build();
//
//	private Card cardTwo = Card.builder()
//			.title("Card two")
//			.description("Description two")
//			.position(2)
//			.creator(adam)
//			.kanbanCategory(categoryToDo)
//			.build();
//
//	private Card cardThree = Card.builder()
//			.title("Card three")
//			.description("Description three")
//			.position(3)
//			.creator(adam)
//			.kanbanCategory(categoryToDo)
//			.build();
//
//	private Card cardFour = Card.builder()
//			.title("Card four")
//			.description("Description four")
//			.position(1)
//			.creator(adam)
//			.kanbanCategory(categoryInProgress)
//			.build();
//	
//	private Card cardFive = Card.builder()
//			.title("Card five")
//			.description("Description five")
//			.position(2)
//			.creator(adam)
//			.kanbanCategory(categoryInProgress)
//			.build();
//
//	private Card cardSix = Card.builder()
//			.title("Card six")
//			.description("Description six")
//			.position(1)
//			.creator(adam)
//			.kanbanCategory(categoryQA)
//			.build();
//
//	private Card cardSeven = Card.builder()
//			.title("Card seven")
//			.description("Description seven")
//			.position(2)
//			.creator(adam)
//			.kanbanCategory(categoryQA)
//			.build();	
//	
////	@EventListener
//	public void seed(ContextRefreshedEvent event) {
//		seedRolesTable();
//		seedUsersTable();
//		seedProjectsTable();
//		seedKanbanCategoriesTable();
//		seedCardsTable();
//	}
//	
//	private void seedRolesTable() {
//		List<Role> roles = roleRepository.findAll();
//		if(roles.size() > 0) {
//			logger.info("Roles seeding not required");
//		} else {
//			roleRepository.saveAll(Arrays.asList(roleAdmin, roleUser));				
//		}		
//	}
//	
//	private void seedKanbanCategoriesTable() {
//		List<KanbanCategory> categories = kanbanCategoryRepository.findAll();
//		if(categories.size() > 0) {
//			logger.info("Kanban Categories seeding not required");
//		} else {
//			categoryDone.setProject(projectOne);
//			categoryInProgress.setProject(projectOne);
//			categoryQA.setProject(projectOne);
//			categoryToDo.setProject(projectOne);
//			kanbanCategoryRepository.saveAll(Arrays.asList(categoryDone, categoryInProgress, categoryQA, categoryQA));			
//		}
//	}
//	
//	private void seedUsersTable() {
//		List<User> users = userRepository.findAll();
//		if(users.size() > 0) {
//			logger.info("User seeding not required");
//		} else {
//			adam.addProject(projectOne);
//			eve.addProject(projectTwo);
//			userRepository.saveAll(Arrays.asList(admin, adam, eve));   			
//		}
//	}
//	
//	private void seedProjectsTable() {
//		List<Project> projects = projectRepository.findAll();
//		if(projects.size() > 0) {
//			logger.info("Project seeding not required");
//		} else {			
//			projectRepository.saveAll(Arrays.asList(projectOne, projectTwo));	
//		}
//	}
//	
//	private void seedCardsTable() {
//		List<Card> cards = cardRepository.findAll();
//		if(cards.size() > 0) {
//			logger.info("Cards seeding not required");
//		} else {
//			categoryDone.addCard(cardOne);
//			categoryDone.addCard(cardTwo);
//			categoryDone.addCard(cardThree);
//			
//			categoryInProgress.addCard(cardFour);
//			categoryInProgress.addCard(cardFive);
//			
//			categoryQA.addCard(cardSix);
//			categoryQA.addCard(cardSeven);
//
//			cardRepository.saveAll(Arrays.asList(cardOne, cardTwo, cardThree, cardFour, cardFive, cardSix, cardSeven));		
//		}
//	}
//}
