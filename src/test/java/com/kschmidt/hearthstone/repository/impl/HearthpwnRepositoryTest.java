package com.kschmidt.hearthstone.repository.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.CardRepository;

public class HearthpwnRepositoryTest {

	private static final Logger LOG = LoggerFactory.getLogger(HearthpwnRepositoryTest.class);

	private static CardRepository cardRepository;

	private HearthpwnRepository hearthpwn;

	@BeforeClass
	public static void setUpClass() throws JsonParseException, JsonMappingException, IOException {
		cardRepository = new JSONCardRepository("AllSets.json");
	}

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		hearthpwn = new HearthpwnRepository(cardRepository);
	}

	@Test
	public void testGetDocument() throws IOException {
		Document doc = hearthpwn.getDocument(
				"http://www.hearthpwn.com/decks?filter-build=26&filter-deck-tag=1&filter-class=64&sort=-rating");
		Assert.assertNotNull(doc);
	}

	@Test
	public void testGetDeckUrls() throws IOException {
		List<String> urls = hearthpwn.getDeckUrls(
				"http://www.hearthpwn.com/decks?filter-build=26&filter-deck-tag=1&filter-class=64&sort=-rating");
		Assert.assertFalse(urls.isEmpty());
		LOG.debug(urls.toString());
	}

	@Test
	public void testGetDeck() throws IOException {
		Deck deck = hearthpwn.getDeck("http://www.hearthpwn.com/decks/308694-reynads-op-dragon-priest");
		assertThat(deck.getNumCards(), equalTo(30));
		assertThat(deck.getCollection(), equalTo("hearthpwnRepository"));
		assertThat(deck.getRating(), greaterThan(700));
		assertThat(deck.getLastUpdated(), greaterThan((ChronoLocalDate) LocalDate.of(2015, Month.AUGUST, 26)));
	}

	@Test
	public void testGetDecks() throws IOException {
		List<Deck> decks = hearthpwn.getDecks(
				"http://www.hearthpwn.com/decks?filter-unreleased-cards=f&filter-build=26&filter-deck-tag=1&filter-class=64&sort=-rating");
		assertThat(decks.size(), equalTo(25));
	}

	@Test
	public void testGetAllDecks() throws IOException {
		List<Deck> decks = hearthpwn.getAllDecks();
		LOG.debug(decks.toString());
		assertThat(decks.size(), greaterThan(200));
	}

}
