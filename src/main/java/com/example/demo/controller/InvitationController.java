package com.example.demo.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.message.request.InvitationRequest;
import com.example.demo.model.Invitation;
import com.example.demo.model.User;
import com.example.demo.services.InvitationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

	@Autowired
	private InvitationService invitationService;
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('USER')")
	public Invitation createInvitation(@RequestBody InvitationRequest request) {
		return invitationService.createInvitation(request);
	}

	@PostMapping("/accept")
	@PreAuthorize("hasRole('USER')")
	public User acceptInvitation(@RequestBody InvitationRequest request) throws Exception {
		return invitationService.acceptInvitation(request.getId());
	}

	@DeleteMapping("/remove/{id}")
	@PreAuthorize("hasRole('USER')")
	public String removeInvitation(@PathVariable Long id) throws Exception {
		invitationService.removeInvitationById(id);
		
		return "Invitation has been removed";
	}
	
	@GetMapping
	@PreAuthorize("hasRole('USER')")
	public Set<Invitation> getInvitations() {
		return invitationService.getInvitations();
	}
	
}
