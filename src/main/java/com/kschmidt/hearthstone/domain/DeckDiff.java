package com.kschmidt.hearthstone.domain;

import com.google.common.base.Optional;
import com.kschmidt.hearthstone.repository.Deck;

public class DeckDiff {

	private Deck desiredDeck;
	private Deck userDeck;

	public DeckDiff(Deck desiredDeck, Deck userDeck) {
		this.desiredDeck = desiredDeck;
		this.userDeck = userDeck;
	}

	public boolean isMissingCards() {
		return !getMissingCards().getCards().isEmpty();
	}

	/**
	 * @return a deck representing the missing cards
	 */
	public Deck getMissingCards() {
		Deck missingCards = new Deck();
		for (DeckCard desiredCard : desiredDeck.getCards()) {
			Optional<DeckCard> correspondingUserCard = userDeck
					.findCard(desiredCard.getCardName());
			int numMatchingUserCards = correspondingUserCard.isPresent() ? correspondingUserCard
					.get().getNumCards() : 0;
			boolean userIsMissingCards = (!correspondingUserCard.isPresent() || desiredCard
					.getNumCards() > numMatchingUserCards);
			if (userIsMissingCards) {
				missingCards.add(new DeckCard(desiredCard.getCard(),
						desiredCard.getNumCards() - numMatchingUserCards));
			}
		}
		return missingCards;
	}
}
