package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.KanbanCategory;

@Repository
public interface KanbanCategoryRepository extends JpaRepository<KanbanCategory, Long> {

	@Query(value = "SELECT kc FROM KanbanCategory kc JOIN FETCH kc.project p WHERE p.id = :projectId")
	List<KanbanCategory> findKanbanCategoriesByProjectId(@Param("projectId") Long projectId);
	
}