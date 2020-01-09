package com.example.demo.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
	
	@Query(value = "SELECT I FROM Invitation I WHERE I.receiverEmail = :email AND I.isAccepted = false")
	Set<Invitation> getInvitationsByEmail(@Param("email") String email);
	
}