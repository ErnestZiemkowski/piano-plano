package com.example.demo.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AppException;
import com.example.demo.message.request.InvitationRequest;
import com.example.demo.model.Invitation;
import com.example.demo.model.User;
import com.example.demo.repository.InvitationRepository;
import com.example.demo.repository.UserRepository;

@Service
public class InvitationService {

	@Autowired
	private InvitationRepository invitationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public User getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User not found."));
	}
	
	public Invitation createInvitation(InvitationRequest request) {
		Invitation invitation = Invitation.createInvitation(this.getLoggedUser(), request.getReceiverEmail());
		return invitationRepository.save(invitation);
	}
	
	public User acceptInvitation(Long id) throws Exception {
		Invitation invitation = invitationRepository
				.findById(id)
				.orElseThrow(() -> new AppException("Invitation not found."));

		if(invitation.getCreator().getEmail() != this.getLoggedUser().getEmail()) {
			throw new AuthenticationServiceException("User unauthorized to modify this resource");
		}
		
		System.out.println(invitation.getReceiverEmail());
		
		User friendToAdd = userRepository
				.findByEmail(invitation.getReceiverEmail())
				.orElseThrow(() -> new AppException("User not found."));

		User owner = invitation.getCreator();
		
		invitation.setAccepted();
		owner.addFriend(friendToAdd);

		invitationRepository.save(invitation);
		userRepository.save(owner);

		return friendToAdd;
	}
	
	public void removeInvitationById(Long id) throws Exception {
		invitationRepository.deleteById(id);
	}
	
	public Set<Invitation> getInvitations() {
		return invitationRepository.getInvitationsByEmail(this.getLoggedUser().getEmail());
	}

}