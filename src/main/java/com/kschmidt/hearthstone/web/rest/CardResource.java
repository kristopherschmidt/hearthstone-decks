package com.kschmidt.hearthstone.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kschmidt.hearthstone.domain.Card;
import com.kschmidt.hearthstone.repository.impl.JSONCardRepository;

@RestController
public class CardResource {

	@Autowired
	private JSONCardRepository jsonCardRepository;

	@RequestMapping(value = "/api/cards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Card> getCards() {
		return jsonCardRepository.getCards();
	}

}
