package com.kschmidt.hearthstone.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class DeckDiffTest {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DeckDiffTest.class);

	private Card a = new Card("aid", "a", "Common");
	private Deck desiredDeck = new Deck("desiredDeck");
	private Deck userDeck = new Deck("userDeck");

	@Test
	public void testDeckRequires1AndUserHas1() {
		desiredDeck.add(new DeckCard(a, 1));

		userDeck.add(new DeckCard(a, 1));

		DeckDiff deckDiff = new DeckDiff(desiredDeck, userDeck);
		assertFalse(deckDiff.isMissingCards());
	}

	@Test
	public void testDeckRequires2AndUserHas2() {
		desiredDeck.add(new DeckCard(a, 2));

		userDeck.add(new DeckCard(a, 2));

		DeckDiff deckDiff = new DeckDiff(desiredDeck, userDeck);
		assertFalse(deckDiff.isMissingCards());
	}

	@Test
	public void testDeckRequires1ButUserHasNone() {
		desiredDeck.add(new DeckCard(a, 1));

		DeckDiff deckDiff = new DeckDiff(desiredDeck, userDeck);
		Deck difference = deckDiff.getMissingCards();

		assertThat(difference.getNumCards(), equalTo(1));
		Optional<DeckCard> a = difference.findCard("a");
		assertTrue(a.isPresent());
		assertThat(a.get().getNumCards(), equalTo(1));
	}

	@Test
	public void testDeckRequires2ButUserHasNone() {
		desiredDeck.add(new DeckCard(a, 2));

		DeckDiff deckDiff = new DeckDiff(desiredDeck, userDeck);
		Deck difference = deckDiff.getMissingCards();

		assertThat(difference.getNumCards(), equalTo(2));
		Optional<DeckCard> a = difference.findCard("a");
		assertTrue(a.isPresent());
		assertThat(a.get().getNumCards(), equalTo(2));
	}

	@Test
	public void testDeckRequires2ButUserHas1() {

		desiredDeck.add(new DeckCard(a, 2));

		userDeck.add(new DeckCard(a, 1));

		DeckDiff deckDiff = new DeckDiff(desiredDeck, userDeck);
		Deck difference = deckDiff.getMissingCards();

		assertThat(difference.getNumCards(), equalTo(1));
		Optional<DeckCard> a = difference.findCard("a");
		assertTrue(a.isPresent());
		assertThat(a.get().getNumCards(), equalTo(1));
	}

	@Test
	public void testDifferentRequirementsForMultipleCards() {

		desiredDeck.add(new DeckCard(a, 1));
		desiredDeck.add(new DeckCard(new Card("bid", "b", "Common"), 2));
		desiredDeck.add(new DeckCard(new Card("cid", "c", "Rare"), 1));
		desiredDeck.add(new DeckCard(new Card("did", "d", "Epic"), 2));
		desiredDeck.add(new DeckCard(new Card("eid", "e", "Legendary"), 2));

		// missing 1 card of a entirely
		// has 1 of c (ok)
		userDeck.add(new DeckCard(new Card("cid", "c", "Rare"), 1));
		// has 2 of b (ok), but added to the collection in a different order
		// than the
		// desiredDeck
		userDeck.add(new DeckCard(new Card("bid", "b", "Common"), 2));
		// missing 2 cards of d entirely
		// has an e but missing 1
		userDeck.add(new DeckCard(new Card("eid", "e", "Legendary"), 1));

		DeckDiff deckDiff = new DeckDiff(desiredDeck, userDeck);
		Deck difference = deckDiff.getMissingCards();

		assertThat(difference.getNumCards(), equalTo(4));
		Optional<DeckCard> a = difference.findCard("a");
		assertTrue(a.isPresent());
		assertThat(a.get().getNumCards(), equalTo(1));
		Optional<DeckCard> d = difference.findCard("d");
		assertTrue(d.isPresent());
		assertThat(d.get().getNumCards(), equalTo(2));
		Optional<DeckCard> e = difference.findCard("e");
		assertTrue(e.isPresent());
		assertThat(e.get().getNumCards(), equalTo(1));
	}
}
