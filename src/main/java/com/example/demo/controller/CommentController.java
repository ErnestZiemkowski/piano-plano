package com.example.demo.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.message.request.CommentRequest;
import com.example.demo.model.Comment;
import com.example.demo.services.CommentService;

@CrossOrigin(origins =  "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comments")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public Comment createComment(@RequestBody CommentRequest commentRequest) {
		return commentService.createComment(commentRequest);
	}
	
	@GetMapping("/project/{id}")
	@PreAuthorize("hasRole('USER')")
	public Set<Comment> getCommentsByProjectId(@PathVariable Long id) {
		return commentService.getCommentsByProjectId(id);
	}

	
	@GetMapping("/card/{id}")
	@PreAuthorize("hasRole('USER')")
	public Set<Comment> getCommentsByCardId(@PathVariable Long id) {
		return commentService.getCommentsByCardId(id);
	}

}