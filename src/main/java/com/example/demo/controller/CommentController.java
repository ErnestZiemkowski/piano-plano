package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.message.request.CommentRequest;
import com.example.demo.model.Comment;
import com.example.demo.services.CommentService;

@CrossOrigin(origins =  "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public Comment createComment(@RequestBody CommentRequest commentRequest) {
		return commentService.createComment(commentRequest);
	}
}