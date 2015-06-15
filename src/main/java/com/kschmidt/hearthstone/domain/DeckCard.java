package com.kschmidt.hearthstone.domain;

/**
 * This class represents a certain amount of a card within the context of a
 * deck.
 */
public class DeckCard {

	private int numCards;
	private String cardName;

	public DeckCard(String cardName, int numCards) {
		this.cardName = cardName;
		this.numCards = numCards;
	}

	public String getCardName() {
		return cardName;
	}

	public int getNumCards() {
		return numCards;
	}

	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append(numCards);
		toString.append(" ");
		toString.append(cardName);
		return toString.toString();
	}

}