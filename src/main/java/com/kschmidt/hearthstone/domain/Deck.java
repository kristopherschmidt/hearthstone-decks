package com.kschmidt.hearthstone.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * A Deck represents an arbitrary collection of Cards and the number of each
 * Card (as represented by DeckCard)
 */
public class Deck {

	@Id
	private String id;

	private List<DeckCard> cards = new ArrayList<DeckCard>();
	private String collection;
	private String name;
	private PlayerClass playerClass;
	private String url;

	public Deck(String name) {
		this.name = name;
	}

	public List<DeckCard> getCards() {
		return ImmutableList.copyOf(cards);
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public int getDustValue() {
		int dust = 0;
		for (DeckCard card : cards) {
			dust += card.getDustValue();
		}
		return dust;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getNumCards() {
		int numCards = 0;
		for (DeckCard card : cards) {
			numCards += card.getNumCards();
		}
		return numCards;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public int getSize() {
		return getCards().size();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void add(DeckCard deckCard) {
		if (deckCard.getNumCards() > 0) {
			cards.add(deckCard);
		}
		if (playerClass == null) {
			playerClass = deckCard.getCard().getPlayerClass();
		} else if (playerClass != deckCard.getCard().getPlayerClass()) {
			playerClass = PlayerClass.Neutral;
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
		return MoreObjects.toStringHelper(getClass()).add("name", name)
				.add("cards", cards).toString();

	}

	public List<DeckCard> sortByNumCards() {
		List<DeckCard> cards = new ArrayList<DeckCard>(getCards());
		cards.sort(new Comparator<DeckCard>() {
			public int compare(DeckCard lhs, DeckCard rhs) {
				return rhs.getNumCards() - lhs.getNumCards();
			}
		});
		return cards;
	}

	public List<DeckCard> sortByDustValue() {
		List<DeckCard> cards = new ArrayList<DeckCard>(getCards());
		cards.sort(new Comparator<DeckCard>() {
			public int compare(DeckCard lhs, DeckCard rhs) {
				return rhs.getDustValue() - lhs.getDustValue();
			}
		});
		return cards;
	}

}
