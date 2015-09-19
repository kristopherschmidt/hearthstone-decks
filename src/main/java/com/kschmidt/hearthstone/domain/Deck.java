package com.kschmidt.hearthstone.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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

	private Date lastUpdatedDate;

	private String name;
	private PlayerClass playerClass;
	private int rating;
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

	public LocalDate getLastUpdated() {
		return lastUpdatedDate == null ? null : lastUpdatedDate.toInstant()
				.atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public void setLastUpdated(LocalDate lastUpdatedLocalDate) {
		if (lastUpdatedLocalDate != null) {
			Instant instant = lastUpdatedLocalDate.atStartOfDay()
					.atZone(ZoneId.systemDefault()).toInstant();
			this.lastUpdatedDate = Date.from(instant);
		} else {
			this.lastUpdatedDate = null;
		}
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
		return playerClass != null ? playerClass : PlayerClass.Neutral;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
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
		PlayerClass cardClass = deckCard.getCard().getPlayerClass();
		if (playerClass == null && cardClass != PlayerClass.Neutral) {
			playerClass = cardClass;
		} else if (playerClass != null && cardClass != PlayerClass.Neutral
				&& playerClass != cardClass) {
			// we have mixed player classes in the same deck
			playerClass = PlayerClass.Neutral;
		} else {
			// Either the player class is the same as the card class,
			// or the player class is null and the card class is neutral,
			// so we can't determine the class (wait for the next card)
		}
	}

	public void addAll(List<DeckCard> deckCards) {
		for (DeckCard deckCard : deckCards) {
			add(deckCard);
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

	public boolean hasCardsFromSet(String cardSet) {
		for (DeckCard deckCard : cards) {
			if (cardSet.equals(deckCard.getCardSet())) {
				return true;
			}
		}
		return false;
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
