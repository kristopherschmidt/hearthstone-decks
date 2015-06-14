package com.kschmidt.hearthstone.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This class represents a certain amount of a card within the context of a
 * deck.
 */
@JsonPropertyOrder({ "cardName", "numCards" })
public class DeckCard {
	
	private int numCards;
	private String cardName;
	private String set;

	public DeckCard() {
		
	}
	
	public DeckCard(String cardName, int numCards, String set) {
		this.cardName = cardName;
		this.numCards = numCards;
		this.set = set;
	}

	public DeckCard(String cardName, int numCards) {
		this(cardName, numCards, null);
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
		if (set != null) {
			toString.append(" (");
			toString.append(set);
			toString.append(")");
		}
		return toString.toString();
	}

}