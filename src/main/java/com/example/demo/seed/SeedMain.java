package com.example.demo.seed;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.repository.RoleRepository;

@Component
public class SeedMain {

	private static final Logger logger = LoggerFactory.getLogger(SeedMain.class);
	
	@Autowired
	private RoleRepository  roleRepository;	
	
	@EventListener
	public void seed(ContextRefreshedEvent event) {
		seedRolesTable();
	}
	
	private void seedRolesTable() {
		List<Role> roles = roleRepository.findAll();
		if(roles.size() > 0) {
			logger.info("Roles seeding not required");
		} else {
			Role roleAdmin = Role.createRole(RoleName.ROLE_ADMIN);
			Role roleUser = Role.createRole(RoleName.ROLE_USER);

			roleRepository.saveAll(Arrays.asList(roleAdmin, roleUser));				
		}	
	}
}