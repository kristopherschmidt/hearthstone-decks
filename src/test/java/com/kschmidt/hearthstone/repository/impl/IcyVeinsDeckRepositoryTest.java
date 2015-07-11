package com.kschmidt.hearthstone.repository.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Optional;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.domain.Rarity;
import com.kschmidt.hearthstone.repository.CardRepository;

public class IcyVeinsDeckRepositoryTest {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(IcyVeinsDeckRepositoryTest.class);

	private static CardRepository cardRepository;

	private IcyVeinsDeckRepository icyVeins;

	@BeforeClass
	public static void setUpClass() throws JsonParseException,
			JsonMappingException, IOException {
		cardRepository = new JSONCardRepository("AllSets.json");
	}

	@Before
	public void setUp() {
		icyVeins = new IcyVeinsDeckRepository(cardRepository);
	}

	@Test
	public void testGetDeck() throws Exception {
		Deck deck = icyVeins
				.getDeck("http://www.icy-veins.com/hearthstone/legendary-druid-fast-brm-deck");
		assertThat(deck.getNumCards(), equalTo(30));
		assertThat(
				deck.getName(),
				equalTo("http://www.icy-veins.com/hearthstone/legendary-druid-fast-brm-deck"));
		assertThat(
				deck.getUrl(),
				equalTo("http://www.icy-veins.com/hearthstone/legendary-druid-fast-brm-deck"));
		assertThat(deck.getCollection(), equalTo("icyVeinsDeckRepository"));
		Optional<DeckCard> card = deck.findCard("Loatheb");
		assertTrue(card.isPresent());
		assertThat(card.get().getCardName(), equalTo("Loatheb"));
		assertThat(card.get().getNumCards(), equalTo(1));
		assertThat(card.get().getRarity(), equalTo(Rarity.Legendary));
		assertThat(card.get().getDustValue(), equalTo(1600));

		card = deck.findCard("Rampage");
		assertFalse(card.isPresent());
	}

	@Test
	public void testGetDeckList() throws IOException {
		List<String> deckUrls = icyVeins
				.getDeckUrls("http://www.icy-veins.com/hearthstone/druid-decks");
		assertThat(deckUrls.size(), greaterThan(0));
	}

	@Test
	public void testGetDecks() throws IOException {
		List<Deck> decks = icyVeins
				.getDecks("http://www.icy-veins.com/hearthstone/druid-decks");
		assertThat(decks.size(), greaterThan(0));
		for (Deck deck : decks) {
			assertThat(deck.getNumCards(), equalTo(30));
		}
	}

	/** this test runs a long time. */
	@Ignore
	@Test
	public void testGetAllDecks() throws IOException {
		icyVeins.getAllDecks();
	}
}
