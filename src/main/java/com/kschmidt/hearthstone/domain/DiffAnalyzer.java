package com.kschmidt.hearthstone.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class DiffAnalyzer {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DiffAnalyzer.class);

	private String cardSet;
	private List<DeckDiff> diffs;
	private LocalDate minDate;
	private int maxRequiredDust = Integer.MAX_VALUE;
	private int minRequiredDust = 0;
	private double percentComplete = 0;

	public DiffAnalyzer(List<DeckDiff> diffs) {
		this.diffs = diffs;
	}

	public void filterByCardSet(String cardSet) {
		this.cardSet = cardSet;
	}

	/**
	 * @param minDateString
	 *            in the format 20150824
	 */
	public void filterByDate(String minDateString) {
		minDate = LocalDate.parse(minDateString,
				DateTimeFormatter.BASIC_ISO_DATE);
	}

	public void filterByMaxRequiredDust(int maxRequiredDust) {
		if (maxRequiredDust < 0) {
			throw new IllegalArgumentException("maxRequiredDust: "
					+ maxRequiredDust + " must be non-negative");
		}
		this.maxRequiredDust = maxRequiredDust;
	}

	public void filterByMinRequiredDust(int minRequiredDust) {
		if (minRequiredDust < 0) {
			throw new IllegalArgumentException("minRequiredDust: "
					+ minRequiredDust + " must be non-negative");
		}
		this.minRequiredDust = minRequiredDust;
	}

	public void filterByPercentComplete(double percentComplete) {
		if (percentComplete < 0 || percentComplete > 100) {
			throw new IllegalArgumentException("percentComplete: "
					+ percentComplete + " must be between 0 and 100");
		}
		this.percentComplete = percentComplete;
	}

	public Deck getAllMissingCards() {
		Map<String, DeckCard> foundCards = new HashMap<String, DeckCard>();

		for (DeckDiff diff : getFilteredDiffs()) {
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

	public List<CardRating> getCardRatings() {
		List<CardRating> cardRatings = new ArrayList<CardRating>();
		for (DeckCard deckCard : getAllMissingCards().getCards()) {
			int ratingTotal = 0;
			for (DeckDiff diff : getFilteredDiffs()) {
				Optional<DeckCard> missingCard = diff.getMissingCards()
						.findCard(deckCard.getCardName());
				if (missingCard.isPresent()) {
					ratingTotal += diff.getDesiredDeck().getRating();
				}
			}
			cardRatings.add(new CardRating(deckCard.getCard(), ratingTotal));
		}
		cardRatings.sort(new Comparator<CardRating>() {
			public int compare(CardRating cardRating1, CardRating cardRating2) {
				return Integer.compare(cardRating2.getRatingTotal(),
						cardRating1.getRatingTotal());
			}
		});
		return cardRatings;
	}

	List<DeckDiff> getFilteredDiffs() {
		List<DeckDiff> filteredDiffs = new ArrayList<DeckDiff>();
		for (DeckDiff diff : diffs) {
			int requiredDust = diff.getRequiredDust();
			if (diff.getPercentComplete() >= percentComplete
					&& requiredDust <= maxRequiredDust
					&& requiredDust >= minRequiredDust
					&& (Strings.isNullOrEmpty(cardSet) || diff
							.hasCardsFromSet(cardSet))
					&& (minDate == null || diff.getDesiredDeck()
							.getLastUpdated().isAfter(minDate))) {
				filteredDiffs.add(diff);
			}
		}
		return filteredDiffs;
	}

	public static class CardRating {
		private Card card;
		private int ratingTotal;

		public CardRating(Card card, int ratingTotal) {
			this.card = card;
			this.ratingTotal = ratingTotal;
		}

		public int getRatingTotal() {
			return ratingTotal;
		}

		public String toString() {
			return card.getName() + "(" + ratingTotal + ")";
		}
	}

}
