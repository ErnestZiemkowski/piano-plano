package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Settings;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long>{

	@Query(value = "SELECT s FROM Settings s WHERE s.user.id = :userId")
	Settings findSettingsByUserId(@Param("userId") Long userId); 

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE Settings s set s.backgroundImageUrl = :backgroundImageUrl WHERE s.user.id = :userId")
	void updateSettingsByUserId(@Param("backgroundImageUrl") String backgroundImageUrl, @Param("userId") Long userId);
}