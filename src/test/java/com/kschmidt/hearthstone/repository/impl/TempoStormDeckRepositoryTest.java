package com.kschmidt.hearthstone.repository.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Optional;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.repository.CardRepository;

public class TempoStormDeckRepositoryTest {

	private static final Logger LOG = LoggerFactory.getLogger(TempoStormDeckRepositoryTest.class);

	private static CardRepository cardRepository;

	private TempoStormDeckRepository tempoStorm;

	@BeforeClass
	public static void setUpClass() throws JsonParseException, JsonMappingException, IOException {
		cardRepository = new JSONCardRepository("cards.collectible.json");
	}

	@Before
	public void setUp() {
		tempoStorm = new TempoStormDeckRepository(cardRepository);
	}

	@Test
	public void testRating() throws IOException {
		Deck deck = tempoStorm.getDeck("avatar-nicks-new-age-legend-zoolock");
		assertThat(deck.getRating(), greaterThan(40));
	}

	@Test
	public void testGetDeckFromSlug() throws IOException {
		Deck deck = tempoStorm.getDeck("prolly-bad-cthun-rogue");
		assertThat(deck.getNumCards(), equalTo(30));
		assertThat(deck.getName(), equalTo("prolly-bad-cthun-rogue"));
		assertThat(deck.getUrl(),
				equalTo("https://tempostorm.com/hearthstone/decks/prolly-bad-cthun-rogue"));
		assertThat(deck.getCollection(), equalTo("tempoStormDeckRepository"));
		//assertThat(deck.getRating(), greaterThan(1));
		assertThat(deck.getLastUpdated(), greaterThan((ChronoLocalDate) LocalDate.of(2016, Month.APRIL, 26)));

		Optional<DeckCard> card = deck.findCard("Sap");
		assertTrue(card.isPresent());
		assertThat(card.get().getCardName(), equalTo("Sap"));
		assertThat(card.get().getNumCards(), equalTo(2));

		card = deck.findCard("Rampage");
		assertFalse(card.isPresent());
	}

	@Test
	public void testDateTimeFormatter() {
		LocalDate localDate = LocalDate.parse("2014-05-14T06:18:48.649Z",
				DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()));
		assertNotNull(localDate);
	}

	@Test
	public void testGetDeckSlugs() throws IOException {
		List<String> deckSlugs = tempoStorm.getDeckUrls("https://tempostorm.com/api/decks");
		Assert.assertFalse(deckSlugs.isEmpty());
		LOG.info("num slugs: " + deckSlugs.size());
		for (String slug : deckSlugs) {
			LOG.info(slug);
		}
	}

	@Test
	public void testCorrectCardNames() throws IOException {
		Deck deck = tempoStorm.getDeck("reynads-pirates");
		Assert.assertTrue(deck.findCard("Ship's Cannon").isPresent());
	}

	@Test
	public void testGetAllDecks() throws IOException {
		List<Deck> decks = tempoStorm.getAllDecks();
		Assert.assertFalse(decks.isEmpty());
		for (Deck deck : decks) {
			LOG.info(deck.getName() + ": " + deck.getNumCards());
			assertThat(deck.getNumCards(), equalTo(30));
		}
	}
}
