package com.example.demo.message.request;

import com.example.demo.model.CommentType;

public class CommentRequest {

	public  String content;
	
	public CommentType type;
	
	public Long parentId;
	
	private CommentRequest(String content, CommentType type, Long parentId) {
		this.content = content;
		this.type = type;
		this.parentId = parentId;
	}
	
	public static CommentRequest createCommentRequest(String content, CommentType type, Long parentId) {
		return new CommentRequest(content, type, parentId);
	}
}