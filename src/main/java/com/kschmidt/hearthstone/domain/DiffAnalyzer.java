package com.kschmidt.hearthstone.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiffAnalyzer {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DiffAnalyzerTest.class);

	private List<DeckDiff> diffs;
	private double percentComplete = 0;

	public DiffAnalyzer(List<DeckDiff> diffs) {
		this.diffs = diffs;
	}

	public void filterByPercentComplete(double percentComplete) {
		if (percentComplete < 0 || percentComplete > 100) {
			throw new IllegalArgumentException("percentComplete: "
					+ percentComplete + " must be between 0 and 100");
		}
		this.percentComplete = percentComplete;
	}

	public Deck getAllMissingCards() {
		Map<String, DeckCard> foundCards = new HashMap<String, DeckCard>();

		for (DeckDiff diff : getFilteredDiffs()) {
			Deck missingCards = diff.getMissingCards();
			for (DeckCard card : missingCards.getCards()) {
				DeckCard alreadyFound = foundCards.get(card.getCardName());
				if (alreadyFound != null) {
					foundCards.put(card.getCardName(), new DeckCard(
							alreadyFound.getCard(), alreadyFound.getNumCards()
									+ card.getNumCards()));
				} else {
					foundCards.put(card.getCardName(), card);
				}
			}
		}

		Deck deck = new Deck(this + ".allMissingCards");
		Collection<DeckCard> allMissingCards = foundCards.values();
		for (DeckCard card : allMissingCards) {
			deck.add(card);
		}
		return deck;
	}

	public List<DeckDiff> getFilteredDiffs() {
		List<DeckDiff> filteredDiffs = new ArrayList<DeckDiff>();
		for (DeckDiff diff : diffs) {

			if (diff.getPercentComplete() >= percentComplete) {
				filteredDiffs.add(diff);
			}
		}
		return filteredDiffs;
	}

}
