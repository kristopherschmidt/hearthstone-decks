package com.kschmidt.hearthstone.repository.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
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
		cardRepository = new JSONCardRepository("cards.collectible.json");
	}

	@Before
	public void setUp() {
		topDeck = new HearthstoneTopDeckRepository(cardRepository);
	}

	@Test
	public void testGetDeck() throws Exception {
		Deck deck = topDeck
				.getDeck("http://www.hearthstonetopdeck.com/deck/standard/5591/warrior-control-bradfordlee");
		assertThat(deck.getNumCards(), equalTo(30));
		assertThat(deck.getName(), equalTo("#1 - Warrior Control - Bradfordlee"));
		assertThat(
				deck.getUrl(),
				equalTo("http://www.hearthstonetopdeck.com/deck/standard/5591/warrior-control-bradfordlee"));
		assertThat(deck.getCollection(),
				equalTo("hearthstoneTopDeckRepository"));
		Optional<DeckCard> card = deck.findCard("Frothing Berserker");
		assertTrue(card.isPresent());
		assertThat(card.get().getNumCards(), equalTo(2));
	}
	
	@Test
	public void testParseLastUpdated() {
		String text = "Format: Standard - Updated: 2016/05/23";
		LocalDate date = topDeck.parseLastUpdated(text);
		assertThat(date.getMonth(), equalTo(Month.MAY));
		assertThat(date.getDayOfMonth(), equalTo(23));
		assertThat(date.getYear(), equalTo(2016));
	}

	@Test
	public void testGetDeckList() throws IOException {
		List<String> deckUrls = topDeck
				.getDeckUrls("http://www.hearthstonetopdeck.com/metagame/standard/mage/0");
		Assert.assertFalse(deckUrls.isEmpty());
		Assert.assertTrue(deckUrls.get(0).startsWith(
				"http://www.hearthstonetopdeck.com"));
	}

	@Test
	public void testGetDecks() throws IOException {
		List<Deck> decks = topDeck
				.getDecks("http://www.hearthstonetopdeck.com/metagame/standard/mage/0");
		Assert.assertFalse(decks.isEmpty());
		for (Deck deck : decks) {
			assertThat(deck.getNumCards(), equalTo(30));
		}
	}

	//previously blacklisted decks no longer exist
	@Ignore
	@Test
	public void testDeckBlacklist() throws IOException {
		List<String> urls = topDeck
				.getDeckUrls("TBD");
		Assert.assertFalse(urls.isEmpty());
		assertFalse(urls
				.contains("TBD"));
	}

	@Test
	public void testGetAllDecks() throws Exception {
		List<Deck> decks = topDeck.getAllDecks();
		assertTrue(decks.size() > 10);
	}

}
