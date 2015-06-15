package com.kschmidt.hearthstone.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.repository.impl.JSONCardRepository;

public class ExcelMasterCollectionTest {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ExcelMasterCollectionTest.class);

	private Deck deck;

	@Test
	public void testGetDeck() throws IOException, InvalidFormatException {
		ExcelMasterCollection masterCollection = new ExcelMasterCollection(
				"HearthstoneMasterCollection.xlsx", new JSONCardRepository(
						"AllSets.json"));
		deck = masterCollection.getDeck();

		// check for a card that shouldn't be present
		Optional<DeckCard> card = deck.findCard("Millhouse Manastorm");
		assertFalse(card.isPresent());

		// neutral
		assertCardExists("Elven Archer", 2);
		// druid
		assertCardExists("Soul of the Forest", 1);
		// hunter
		assertCardExists("King of Beasts", 1);
		// mage
		assertCardExists("Flamecannon", 2);
		// paladin
		assertCardExists("Repentance", 2);
		// priest
		assertCardExists("Prophet Velen", 1);
		// rogue
		assertCardExists("Conceal", 2);
		// shaman
		assertCardExists("Crackle", 1);
		// warlock
		assertCardExists("Darkbomb", 2);
		// warrior
		assertCardExists("Warbot", 2);
	}

	private void assertCardExists(String cardName, int expectedNumCards) {
		Optional<DeckCard> card = deck.findCard(cardName);
		assertTrue(card.isPresent());
		assertThat(card.get().getNumCards(), equalTo(expectedNumCards));
	}

}
