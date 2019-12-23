package com.example.demo.repository;

import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.DailyGoal;

@Repository
public interface DailyGoalsRepository extends JpaRepository<DailyGoal, Long> {
		
	@Query(value = "SELECT dg FROM DailyGoal dg WHERE dg.user.id = :userId AND :today < dg.expiresAt")
	Set<DailyGoal> getActiveDailyGoalsByUserId(@Param("userId") Long userId, @Param("today") Date today);

//	@Query(value = "SELECT c FROM DailyGoal dg JOIN dg.card AS c WHERE dg.user.id = :userId")
//	Set<DailyGoal> getActiveDailyGoalsByUserId(@Param("userId") Long userId);

}