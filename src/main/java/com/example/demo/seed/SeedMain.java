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

import com.example.demo.model.Project;
import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.User;
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
	
	private Role roleAdmin = new Role();
	private Role roleUser = new Role();
	
	private User admin = new User();
	private User userAdam = new User();
	
	private Project projectOne = new Project();
	private Project projectTwo = new Project();
	private Project projectThree = new Project();
	private Project projectFour = new Project();
	private Project projectFive = new Project();
	private Project projectSix = new Project();
	
	
	@EventListener
	public void seed(ContextRefreshedEvent event) {
		seedRolesTable();
		seedUsersTable();
		seedProjectsTable();
	}
	
	private void seedRolesTable() {
		List<Role> roles = roleRepository.findAll();
		
		if (roles.size() <= 0 || roles == null) {
			
			roleAdmin.setName(RoleName.ROLE_ADMIN);
			roleUser.setName(RoleName.ROLE_USER);
		
			roleRepository.saveAll(Arrays.asList(roleAdmin, roleUser));
		} else {
            logger.info("Users Seeding Not Required");
        }
	}
	
	private void seedUsersTable() {
		List<User> users = userRepository.findAll();
		
		if (users.size() <= 0 || users == null) {	
						
			admin.setUsername("admin123");
			admin.setEmail("admin@demo.com");
			admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
			admin.addRole(roleUser);
			admin.addRole(roleAdmin);
			
			userAdam.setUsername("adam123");
			userAdam.setEmail("adam@demo.com");
			userAdam.setPassword(new BCryptPasswordEncoder().encode("adam123"));
			userAdam.addRole(roleUser);
						
			userRepository.saveAll(Arrays.asList(admin, userAdam));
		} else {
            logger.info("Users Seeding Not Required");
        }
	}
	
	private void seedProjectsTable() {
		List<Project> projects = projectRepository.findAll();
		
		if (projects.size() <= 0 || projects == null) {
			
			projectOne.setName("Project One");
			projectOne.setDescription("This is Project One");
			projectOne.setCreator(userAdam);

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
		} else {
			logger.info("Projects Seeding Not Required");
		}
	}
}
