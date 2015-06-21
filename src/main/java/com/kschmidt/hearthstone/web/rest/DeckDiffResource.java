package com.kschmidt.hearthstone.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckDiff;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;
import com.kschmidt.hearthstone.repository.WebDeckRepository;

@RestController
public class DeckDiffResource {

	@Autowired
	@Qualifier("hearthstoneTopDeckRepository")
	private WebDeckRepository deckRepository;

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Autowired
	private Deck userDeck;

	@RequestMapping(value = "/api/diffdeck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DeckDiff diffDeck() throws IOException {
		Deck desiredDeck = deckRepository
				.getDeck("http://www.hearthstonetopdeck.com/deck.php?d=4576&filter=current");
		return new DeckDiff(desiredDeck, userDeck);
	}

	@RequestMapping(value = "/api/difflist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffDeckList() throws IOException {
		List<DeckDiff> diffs = new ArrayList<DeckDiff>();
		List<Deck> druidDecks = deckRepository
				.getDecks("http://www.hearthstonetopdeck.com/metagame.php?m=Druid&t=0&filter=current");
		for (Deck deck : druidDecks) {
			DeckDiff deckDiff = new DeckDiff(deck, userDeck);
			diffs.add(deckDiff);
		}
		return diffs;
	}

	@RequestMapping(value = "/api/diffall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffAllWebDecks() throws IOException {
		return DeckDiff.diffDecks(userDeck, deckRepository.getAllDecks());
	}

	@RequestMapping(value = "/api/diffalldb", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeckDiff> diffAllDbDecks() throws IOException {
		return DeckDiff.diffDecks(userDeck, mongoDeckRepository.findAll());
	}

}
