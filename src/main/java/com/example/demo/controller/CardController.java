package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.message.request.CardRequest;
import com.example.demo.message.response.ResponseMessage;
import com.example.demo.model.Card;
import com.example.demo.services.CardService;

@CrossOrigin(origins =  "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CardController {

	@Autowired
	private CardService cardService;
	
	@DeleteMapping("/cards/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseMessage deleteKanbanCategoryById(@PathVariable Long id) {
		cardService.deleteCardById(id);
		String responseMessage = String.format("Card with id of %d deleted successfully", id);
		
		return new ResponseMessage(responseMessage);
	}
	
	@PostMapping("/cards")
	@PreAuthorize("hasRole('USER')")
	public Card createCard(@RequestBody CardRequest cardRequest) {
		return cardService.createCard(cardRequest);
	}
	
	@PutMapping("/cards/{id}")
	@PreAuthorize("hasRole('USER')")
	public Card updateCard(@RequestBody CardRequest cardRequest, @PathVariable Long id) {
		return cardService.updateCard(cardRequest, id);
	}
	
}
