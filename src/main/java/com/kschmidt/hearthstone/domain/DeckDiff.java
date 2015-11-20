package com.kschmidt.hearthstone.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
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

	public Deck getDesiredDeck() {
		return desiredDeck;
	}

	/**
	 * @return dust value of the desired deck
	 */
	public int getFullDustValue() {
		return desiredDeck.getDustValue();
	}

	public LocalDate getLastUpdated() {
		return desiredDeck.getLastUpdated();
	}

	public Deck getMissingCards() {
		return missingCards;
	}

	public int getNumMissingCards() {
		return missingCards.getNumCards();
	}

	public double getPercentComplete() {
		return (getFullDustValue() - getRequiredDust())
				/ (double) getFullDustValue() * 100;
	}

	/**
	 * public double getRankingMetric() { double dustWeight =
	 * getFullDustValue(); return dustWeight * getPercentComplete() / 100; }
	 */

	public double getRankingMetric() {
		double dustWeight = getFullDustValue();
		// int daysSinceTGTLaunch = Period.between(LocalDate.of(2015,
		// Month.JULY, 24), LocalDate.now()).getDays();
		long daysFromTGTLaunchToLastUpdate = Duration.between(
				LocalDateTime.of(2015, Month.AUGUST, 24, 0, 0).toInstant(
						ZoneOffset.UTC),
				LocalDateTime.of(getLastUpdated(), LocalTime.MIDNIGHT)
						.toInstant(ZoneOffset.UTC)).toDays();

		long daysFromTGTLaunchToNow = Duration.between(
				LocalDateTime.of(2015, Month.AUGUST, 24, 0, 0).toInstant(
						ZoneOffset.UTC),
				LocalDateTime.now().toInstant(ZoneOffset.UTC)).toDays();

		double recencyMetric = (double) daysFromTGTLaunchToLastUpdate
				/ (double) daysFromTGTLaunchToNow;

		return recencyMetric * getPercentComplete() / 100 * getRating();
	}

	public int getRating() {
		return desiredDeck.getRating();
	}

	/**
	 * @return the amount of dust required to completely fill out the deck
	 */
	public int getRequiredDust() {
		return missingCards.getDustValue();
	}

	public boolean hasCardsFromSet(String cardSet) {
		return desiredDeck.hasCardsFromSet(cardSet);
	}

	public boolean isMissingCards() {
		return !missingCards.getCards().isEmpty();
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

}
