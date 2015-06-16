package com.kschmidt.hearthstone.domain;

import com.google.common.base.Optional;

public class DeckDiff {

	private Deck desiredDeck;
	private Deck missingCards;
	private Deck userDeck;

	public DeckDiff(Deck desiredDeck, Deck userDeck) {
		this.desiredDeck = desiredDeck;
		this.userDeck = userDeck;
		missingCards = calculateMissingCards();
	}

	public boolean isMissingCards() {
		return !missingCards.getCards().isEmpty();
	}

	public Deck getMissingCards() {
		return missingCards;
	}

	/**
	 * @return a deck representing the missing cards
	 */
	private Deck calculateMissingCards() {
		Deck missingCards = new Deck("User cards missing from '"
				+ desiredDeck.getName() + "'");
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

	/**
	 * @return the amount of dust required to completely fill out the deck
	 */
	public int getRequiredDust() {
		return missingCards.getDustValue();
	}

}
