package com.kschmidt.hearthstone.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

public class DeckDiff {

	private Deck desiredDeck;
	private Deck missingCards;
	private Deck userDeck;

	public static List<DeckDiff> diffDecks(Deck userDeck, List<Deck> decks) {
		List<DeckDiff> diffs = new ArrayList<DeckDiff>();
		for (Deck deck : decks) {
			DeckDiff deckDiff = new DeckDiff(deck, userDeck);
			diffs.add(deckDiff);
		}
		return diffs;
	}

	public DeckDiff(Deck desiredDeck, Deck userDeck) {
		this.desiredDeck = desiredDeck;
		this.userDeck = userDeck;
		missingCards = calculateMissingCards();
	}

	public String getDesiredDeckName() {
		return desiredDeck.getName();
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
	 * @return dust value of the desired deck
	 */
	public int getFullDustValue() {
		return desiredDeck.getDustValue();
	}

	/**
	 * @return the amount of dust required to completely fill out the deck
	 */
	public int getRequiredDust() {
		return missingCards.getDustValue();
	}

	public double getPercentComplete() {
		return (getFullDustValue() - getRequiredDust())
				/ (double) getFullDustValue() * 100;
	}

	public double getRankingMetric() {
		int investedAmount = getFullDustValue() - getRequiredDust();
		return investedAmount * getPercentComplete() / 100;
	}

}
