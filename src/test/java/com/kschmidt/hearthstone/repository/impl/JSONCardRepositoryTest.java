package com.kschmidt.hearthstone.repository.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kschmidt.hearthstone.repository.Card;
import com.kschmidt.hearthstone.repository.CardRepository;
import com.kschmidt.hearthstone.repository.impl.JSONCardRepository;

public class JSONCardRepositoryTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException,
			IOException {

		CardRepository cardRepository = new JSONCardRepository("AllSets.json");
		List<Card> cards = cardRepository.getCards();
		assertTrue(cards.size() > 0);

		Card card = cardRepository.findCard("Deathwing");
		assertNotNull(card);
		assertThat(card.getRarity(), equalTo("Legendary"));
	}
}
