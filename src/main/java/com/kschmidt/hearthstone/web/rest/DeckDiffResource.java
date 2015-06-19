package com.kschmidt.hearthstone.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckDiff;
import com.kschmidt.hearthstone.repository.impl.IcyVeinsDeckRepository;

@RestController
public class DeckDiffResource {

	@Autowired
	private IcyVeinsDeckRepository icyVeinsDeckRepository;

	@Autowired
	private Deck userDeck;

	@RequestMapping(value = "/api/deckdiff", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DeckDiff diffDeck() throws IOException {
		Deck desiredDeck = icyVeinsDeckRepository
				.getDeck("http://www.icy-veins.com/hearthstone/legendary-dragon-ramp-druid-brm-deck");
		return new DeckDiff(desiredDeck, userDeck);
	}

	@RequestMapping(value = "/api/decklistdiff", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffDeckList() throws IOException {
		List<DeckDiff> diffs = new ArrayList<DeckDiff>();
		List<Deck> druidDecks = icyVeinsDeckRepository
				.getDecks("http://www.icy-veins.com/hearthstone/priest-decks");
		for (Deck deck : druidDecks) {
			DeckDiff deckDiff = new DeckDiff(deck, userDeck);
			diffs.add(deckDiff);
		}
		return diffs;
	}

}
