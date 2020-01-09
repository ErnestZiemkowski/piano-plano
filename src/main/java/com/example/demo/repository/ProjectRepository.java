package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Project;
import com.example.demo.model.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{

//	@Query(value = "SELECT p FROM Project as p JOIN p.members as m WHERE m.id = :friendId")
//	User getMemberOfProject(@Param("friendId") Long friendId, @Param("projectId") Long projectId);

//	@Query(value = "SELECT p FROM Project as p JOIN p.members as m WHERE m.id = :friendId AND p.id = :projectId")
//	Project getMemberOfProject(@Param("friendId") Long friendId, @Param("projectId") Long projectId);

	@Query(value = "SELECT m FROM Project as p JOIN p.members as m WHERE m.id = :friendId AND p.id = :projectId")
	User getMemberOfProject(@Param("friendId") Long friendId, @Param("projectId") Long projectId);
}