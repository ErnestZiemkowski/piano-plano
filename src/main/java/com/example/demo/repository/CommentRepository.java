package com.example.demo.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	@Query(value = "SELECT c FROM Project p JOIN p.comments c WHERE p.id = :projectId")
	Set<Comment> findCommentsByProjectId(@Param("projectId") Long projectId);
	
	@Query(value = "SELECT cm FROM Card c JOIN c.comments cm WHERE c.id = :cardId")
	Set<Comment> findCommentsByCardId(@Param("cardId") Long cardId);

}