package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.message.request.CommentRequest;
import com.example.demo.model.Card;
import com.example.demo.model.Comment;
import com.example.demo.model.Project;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.ProjectRepository;

@Service
public class CommentService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private CardRepository cardRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	public Comment createComment(CommentRequest request) {
		Comment comment = Comment.createComment(request.content);
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