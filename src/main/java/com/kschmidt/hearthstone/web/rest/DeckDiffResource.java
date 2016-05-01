package com.kschmidt.hearthstone.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor(null));
	}

	@RequestMapping(value = "/api/diffanalyzer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DiffAnalyzer diffAnalyzer(@RequestParam(required = false, value = "collection") String collectionName,
			@RequestParam(required = false, value = "cards") String[] cardNamesArray,
			@RequestParam(required = false, value = "playerClasses") List<String> playerClasses,
			@RequestParam(required = false, value = "maxRequiredDust") Integer maxRequiredDust,
			@RequestParam(required = false, value = "minRequiredDust") Integer minRequiredDust,
			@RequestParam(required = false, value = "cardSet") String cardSet,
			@RequestParam(required = false, value = "minDate", defaultValue = "20160401") String minDateString)
					throws IOException {
		List<String> cardNames;
		if (Strings.isNullOrEmpty(collectionName)) {
			collectionName = null;
		}
		if (cardNamesArray == null) {
			cardNames = new ArrayList<String>();
		} else {
			cardNames = Arrays.asList(cardNamesArray);
		}
		if (playerClasses == null) {
			playerClasses = new ArrayList<String>();
		}
		List<Deck> decks = mongoDeckRepository.find(collectionName, cardNames, playerClasses);
		System.out.println("decks found: " + decks);
		DiffAnalyzer analyzer = new DiffAnalyzer(DeckDiff.diffDecks(userDeck, decks));
		if (maxRequiredDust != null) {
			analyzer.filterByMaxRequiredDust(maxRequiredDust);
		}
		if (minRequiredDust != null) {
			analyzer.filterByMinRequiredDust(minRequiredDust);
		}
		if (!Strings.isNullOrEmpty(cardSet)) {
			analyzer.filterByCardSet(cardSet);
		}
		analyzer.filterByDate(minDateString);
		return analyzer;
	}

}
