package com.kschmidt.hearthstone.web.rest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckDiff;
import com.kschmidt.hearthstone.domain.DiffAnalyzer;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;

@RestController
public class DeckDiffResource {

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Autowired
	private Deck userDeck;

	@RequestMapping(value = "/api/diffanalyzer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DiffAnalyzer diffAnalyzer(
			@RequestParam(required = false, value = "collection") String collectionName,
			@RequestParam(required = false, value = "cards") List<String> cardNames,
			@RequestParam(required = false, value = "playerClasses") List<String> playerClasses,
			@RequestParam(required = false, value = "maxRequiredDust") Integer maxRequiredDust,
			@RequestParam(required = false, value = "minRequiredDust") Integer minRequiredDust,
			@RequestParam(required = false, value = "cardSet") String cardSet,
			@RequestParam(required = false, value = "minDate", defaultValue = "20150824") String minDateString)
			throws IOException {
		if (Strings.isNullOrEmpty(collectionName)) {
			collectionName = null;
		}
		if (cardNames == null) {
			cardNames = new ArrayList<String>();
		}
		if (playerClasses == null) {
			playerClasses = new ArrayList<String>();
		}
		List<Deck> decks = mongoDeckRepository.find(collectionName, cardNames,
				playerClasses);
		DiffAnalyzer analyzer = new DiffAnalyzer(DeckDiff.diffDecks(userDeck,
				filterByDate(decks, minDateString)));
		if (maxRequiredDust != null) {
			analyzer.filterByMaxRequiredDust(maxRequiredDust);
		}
		if (minRequiredDust != null) {
			analyzer.filterByMinRequiredDust(minRequiredDust);
		}
		if (!Strings.isNullOrEmpty(cardSet)) {
			analyzer.filterByCardSet(cardSet);
		}
		return analyzer;
	}

	private List<Deck> filterByDate(List<Deck> decks, String minDateString) {
		List<Deck> filteredDecks = new ArrayList<Deck>();
		LocalDate minDate = LocalDate.parse(minDateString,
				DateTimeFormatter.BASIC_ISO_DATE);
		for (Deck deck : decks) {
			if (deck.getLastUpdated().isAfter(minDate)) {
				filteredDecks.add(deck);
			}
		}
		return filteredDecks;
	}

}
