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

import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Component
public class SeedMain {

	private static final Logger logger = LoggerFactory.getLogger(SeedMain.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository  roleRepository;
	
	private Role roleAdmin = new Role();
	private Role roleUser = new Role();
	
	private User admin = new User();
	private User userAdam = new User();
	
	@EventListener
	public void seed(ContextRefreshedEvent event) {
		seedRolesTable();
		seedUsersTable();
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
						
			admin.setName("Admin");
			admin.setUsername("admin123");
			admin.setEmail("admin@demo.com");
			admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
			admin.addRole(roleUser);
			admin.addRole(roleAdmin);
			
			userAdam.setName("Adam");
			userAdam.setUsername("adam123");
			userAdam.setEmail("adam@demo.com");
			userAdam.setPassword(new BCryptPasswordEncoder().encode("adam"));
			userAdam.addRole(roleUser);
						
			userRepository.saveAll(Arrays.asList(admin, userAdam));
		} else {
            logger.info("Users Seeding Not Required");
        }
	}
}
