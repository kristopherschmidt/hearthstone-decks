package com.kschmidt.hearthstone.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class DiffAnalyzer {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DiffAnalyzer.class);

	private String cardSet;
	private List<DeckDiff> diffs;
	private int maxRequiredDust = Integer.MAX_VALUE;
	private int minRequiredDust = 0;
	private double percentComplete = 0;

	public DiffAnalyzer(List<DeckDiff> diffs) {
		this.diffs = diffs;
	}

	public void filterByCardSet(String cardSet) {
		this.cardSet = cardSet;
	}

	public void filterByMaxRequiredDust(int maxRequiredDust) {
		if (maxRequiredDust < 0) {
			throw new IllegalArgumentException("maxRequiredDust: "
					+ maxRequiredDust + " must be non-negative");
		}
		this.maxRequiredDust = maxRequiredDust;
	}

	public void filterByMinRequiredDust(int minRequiredDust) {
		if (minRequiredDust < 0) {
			throw new IllegalArgumentException("minRequiredDust: "
					+ minRequiredDust + " must be non-negative");
		}
		this.minRequiredDust = minRequiredDust;
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

	List<DeckDiff> getFilteredDiffs() {
		List<DeckDiff> filteredDiffs = new ArrayList<DeckDiff>();
		for (DeckDiff diff : diffs) {
			int requiredDust = diff.getRequiredDust();
			if (diff.getPercentComplete() >= percentComplete
					&& requiredDust <= maxRequiredDust
					&& requiredDust >= minRequiredDust
					&& (Strings.isNullOrEmpty(cardSet) || diff
							.hasCardsFromSet(cardSet))) {
				filteredDiffs.add(diff);
			}
		}
		return filteredDiffs;
	}

}
