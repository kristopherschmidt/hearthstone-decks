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

	private static final Logger LOG = LoggerFactory
			.getLogger(TempoStormDeckRepositoryTest.class);

	private static CardRepository cardRepository;

	private TempoStormDeckRepository tempoStorm;

	@BeforeClass
	public static void setUpClass() throws JsonParseException,
			JsonMappingException, IOException {
		cardRepository = new JSONCardRepository("AllSets.json");
	}

	@Before
	public void setUp() {
		tempoStorm = new TempoStormDeckRepository(cardRepository);
	}

	@Test
	public void testGetDeckFromSlug() throws IOException {
		Deck deck = tempoStorm.getDeck("seventythree-aggro-flamewaker-mage");
		assertThat(deck.getNumCards(), equalTo(30));
		assertThat(deck.getName(),
				equalTo("seventythree-aggro-flamewaker-mage"));
		assertThat(
				deck.getUrl(),
				equalTo("https://tempostorm.com/hearthstone/decks/seventythree-aggro-flamewaker-mage"));

		Optional<DeckCard> card = deck.findCard("Clockwork Gnome");
		assertTrue(card.isPresent());
		assertThat(card.get().getCardName(), equalTo("Clockwork Gnome"));
		assertThat(card.get().getNumCards(), equalTo(2));

		card = deck.findCard("Rampage");
		assertFalse(card.isPresent());
	}

	@Test
	public void testGetDeckSlugs() throws IOException {
		List<String> deckSlugs = tempoStorm
				.getDeckSlugs("https://tempostorm.com/decks");
		Assert.assertFalse(deckSlugs.isEmpty());
	}

	@Test
	public void testGetDecks() throws IOException {
		List<Deck> decks = tempoStorm.getDecks("https://tempostorm.com/decks");
		Assert.assertFalse(decks.isEmpty());
		for (Deck deck : decks) {
			assertThat(deck.getNumCards(), equalTo(30));
		}
		LOG.debug("Num decks found: " + decks.size());
		for (Deck deck : decks) {
			LOG.debug(deck.getName());
		}
	}

	@Test
	public void testGetAllDecks() throws IOException {
		List<Deck> decks = tempoStorm.getAllDecks();
		Assert.assertFalse(decks.isEmpty());
		for (Deck deck : decks) {
			assertThat(deck.getNumCards(), equalTo(30));
		}
		LOG.debug("Num decks found: " + decks.size());
		for (Deck deck : decks) {
			LOG.debug(deck.getName());
		}
	}
}
