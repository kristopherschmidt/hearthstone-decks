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

import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckDiff;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;

@RestController
public class DeckDiffResource {

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Autowired
	private Deck userDeck;

	@RequestMapping(value = "/api/diffs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffs() throws IOException {
		return DeckDiff.diffDecks(userDeck, mongoDeckRepository.findAll());
	}

	@RequestMapping(value = "/api/diffs2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffs2(@RequestParam(required = false, value = "card") String cardName) throws IOException {
		return DeckDiff.diffDecks(userDeck, mongoDeckRepository
				.findDecksContainingCard(cardName));
	}

	@RequestMapping(value = "/api/diffs3", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffs3() throws IOException {
		return DeckDiff.diffDecks(userDeck, mongoDeckRepository
				.findDecksContainingAllCards(Arrays.asList("Dr. Boom",
						"Sylvanas Windrunner")));
	}
	
	@RequestMapping(value = "/api/diffs4", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffs4() throws IOException {
		return DeckDiff.diffDecks(userDeck, mongoDeckRepository
				.findDecksContainingAllCards(Arrays.asList("Dr. Boom",
						"Sylvanas Windrunner", "Ragnaros the Firelord")));
	}
	
	@RequestMapping(value = "/api/diffs5", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffs5() throws IOException {
		return DeckDiff.diffDecks(userDeck, mongoDeckRepository
				.findDecksContainingAllCards(Arrays.asList("Sea Giant")));
	}
}
