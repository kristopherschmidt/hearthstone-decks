package com.kschmidt.hearthstone.repository;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.kschmidt.hearthstone.domain.DeckCard;

public class Deck {

	private List<DeckCard> cards = new ArrayList<DeckCard>();

	public List<DeckCard> getCards() {
		return ImmutableList.copyOf(cards);
	}

	public int getNumCards() {
		int numCards = 0;
		for (DeckCard card : cards) {
			numCards += card.getNumCards();
		}
		return numCards;
	}

	public int getSize() {
		return getCards().size();
	}

	public void add(DeckCard deckCard) {
		if (deckCard.getNumCards() > 0) {
			cards.add(deckCard);
		}
	}

	public Optional<DeckCard> findCard(final String cardName) {
		return Iterables.tryFind(cards, new Predicate<DeckCard>() {
			@Override
			public boolean apply(DeckCard userCard) {
				return Objects.equal(userCard.getCardName(), cardName);
			}
		});
	}

	public String toString() {
		return MoreObjects.toStringHelper(getClass()).add("cards", cards)
				.toString();

	}

}
