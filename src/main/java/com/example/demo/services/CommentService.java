package com.example.demo.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AppException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.message.request.CommentRequest;
import com.example.demo.model.Card;
import com.example.demo.model.Comment;
import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CommentService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private CardRepository cardRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	public Set<Comment> getCommentsByProjectId(Long projectId) {
		return commentRepository.findCommentsByProjectId(projectId);
	}
	
	public Set<Comment> getCommentsByCardId(Long cardId) {
		return commentRepository.findCommentsByCardId(cardId);
	}
	
	
	public Comment createComment(CommentRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = userRepository
				.findByUsername(auth.getName())
				.orElseThrow(() -> new AppException("User Role not set."));

		Comment comment = Comment.createComment(request.content, loggedUser);
		commentRepository.save(comment);

		switch (request.type) {
			case COMMENT_CARD:
				Card card = cardRepository.findById(request.parentId)
						.orElseThrow(() -> new ResourceNotFoundException("Card", "id", request.parentId));		
				card.addComment(comment);
				cardRepository.save(card);
				break;
			case COMMENT_PROJECT:
				Project project = projectRepository.findById(request.parentId)
						.orElseThrow(() -> new ResourceNotFoundException("Project", "id", request.parentId));
				project.addComment(comment);
				projectRepository.save(project);
				break;
			default:
				break;
		}
		
		return comment;
	}
}