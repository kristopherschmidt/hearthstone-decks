package com.kschmidt.hearthstone.domain;

/**
 * This class represents a certain amount of a card within the context of a
 * deck.
 */
public class DeckCard {

	private Card card;
	private int numCards;

	public DeckCard(Card card, int numCards) {
		this.card = card;
		this.numCards = numCards;
	}

	public Card getCard() {
		return card;
	}

	public String getCardName() {
		return card.getName();
	}

	public String getCardSet() {
		return card.getCardSet();
	}

	public int getDustValue() {
		return card.getDustValue() * numCards;
	}

	public int getNumCards() {
		return numCards;
	}

	public Rarity getRarity() {
		return card.getRarity();
	}

	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append(numCards);
		toString.append(" ");
		toString.append(getCardName());
		return toString.toString();
	}

}