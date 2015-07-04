package com.kschmidt.hearthstone.web.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.kschmidt.hearthstone.domain.Card;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckDiff;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;
import com.kschmidt.hearthstone.repository.impl.JSONCardRepository;

@RestController
public class DeckDiffResource {

	@Autowired
	private JSONCardRepository jsonCardRepository;

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Autowired
	private Deck userDeck;

	@RequestMapping(value = "/api/diffs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffs(
			@RequestParam(required = false, value = "collection") String collectionName)
			throws IOException {
		List<Deck> decks;
		if (!Strings.isNullOrEmpty(collectionName)) {
			decks = mongoDeckRepository.findByCollection(collectionName);
		} else {
			decks = mongoDeckRepository.findAll();
		}

		return DeckDiff.diffDecks(userDeck, decks);
	}

	@RequestMapping(value = "/api/cards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Card> getCards() {
		return jsonCardRepository.getCards();
	}

	/** todo map this somehow to diffs */
	@RequestMapping(value = "/api/diffs2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffs2(
			@RequestParam(required = false, value = "card") String cardName)
			throws IOException {
		return DeckDiff.diffDecks(userDeck,
				mongoDeckRepository.findDecksContainingCard(cardName));
	}

	/** todo implement resource to look for multi cards */
	@RequestMapping(value = "/api/diffs4", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffs4() throws IOException {
		return DeckDiff.diffDecks(userDeck, mongoDeckRepository
				.findDecksContainingAllCards(Arrays.asList("Dr. Boom",
						"Sylvanas Windrunner", "Ragnaros the Firelord")));
	}

}
