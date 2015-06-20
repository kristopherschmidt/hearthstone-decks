package com.kschmidt.hearthstone.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiffAnalyzer {

	private List<DeckDiff> diffs;

	public DiffAnalyzer(List<DeckDiff> diffs) {
		this.diffs = diffs;
	}

	public Deck getAllMissingCards() {
		Map<String, DeckCard> foundCards = new HashMap<String, DeckCard>();

		for (DeckDiff diff : diffs) {
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
}
