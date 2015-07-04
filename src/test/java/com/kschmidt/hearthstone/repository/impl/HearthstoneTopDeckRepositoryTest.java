package com.kschmidt.hearthstone.repository.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
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
import com.kschmidt.hearthstone.repository.CardRepository;

public class HearthstoneTopDeckRepositoryTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(HearthstoneTopDeckRepositoryTest.class);

	private static CardRepository cardRepository;

	private HearthstoneTopDeckRepository topDeck;

	@BeforeClass
	public static void setUpClass() throws JsonParseException,
			JsonMappingException, IOException {
		cardRepository = new JSONCardRepository("AllSets.json");
	}

	@Before
	public void setUp() {
		topDeck = new HearthstoneTopDeckRepository(cardRepository);
	}

	@Test
	public void testGetDeck() throws Exception {
		Deck deck = topDeck
				.getDeck("http://www.hearthstonetopdeck.com/deck.php?d=4577&filter=current");
		assertThat(deck.getNumCards(), equalTo(30));
		assertThat(deck.getName(), equalTo("#1 - HandLock - Falcon"));
		assertThat(
				deck.getUrl(),
				equalTo("http://www.hearthstonetopdeck.com/deck.php?d=4577&filter=current"));
		assertThat(deck.getCollection(), equalTo("hearthstoneTopDeckRepository"));

		Optional<DeckCard> card = deck.findCard("Loatheb");
		assertTrue(card.isPresent());
		assertThat(card.get().getCardName(), equalTo("Loatheb"));
		assertThat(card.get().getNumCards(), equalTo(1));

		card = deck.findCard("Rampage");
		assertFalse(card.isPresent());
	}

	@Test
	public void testGetDeckList() throws IOException {
		List<String> deckUrls = topDeck
				.getDeckUrls("http://www.hearthstonetopdeck.com/metagame.php?m=Mage&t=0&filter=current");
		Assert.assertFalse(deckUrls.isEmpty());
		Assert.assertTrue(deckUrls.get(0).startsWith(
				"http://www.hearthstonetopdeck.com"));
	}

	@Test
	public void testGetDecks() throws IOException {
		List<Deck> decks = topDeck
				.getDecks("http://www.hearthstonetopdeck.com/metagame.php?m=Mage&t=0&filter=current");
		Assert.assertFalse(decks.isEmpty());
		for (Deck deck : decks) {
			assertThat(deck.getNumCards(), equalTo(30));
		}
	}

	@Ignore
	@Test
	public void testGetAllDecks() throws Exception {
		List<Deck> decks = topDeck.getAllDecks();
		LOG.debug(decks.toString());
	}

}
