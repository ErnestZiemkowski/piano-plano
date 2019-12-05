package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

}
