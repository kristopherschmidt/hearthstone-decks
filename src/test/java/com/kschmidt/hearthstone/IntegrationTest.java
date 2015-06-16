package com.kschmidt.hearthstone;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.domain.DeckDiff;
import com.kschmidt.hearthstone.repository.CardRepository;
import com.kschmidt.hearthstone.repository.impl.ExcelDeckRepository;
import com.kschmidt.hearthstone.repository.impl.IcyVeinsDeckRepository;
import com.kschmidt.hearthstone.repository.impl.JSONCardRepository;

public class IntegrationTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(IntegrationTest.class);

	private static IcyVeinsDeckRepository icyVeins;
	private static ExcelDeckRepository masterCollection;
	private static Deck userDeck;

	@BeforeClass
	public static void setUp() throws JsonParseException, JsonMappingException,
			IOException, InvalidFormatException {
		CardRepository cardRepository = new JSONCardRepository("AllSets.json");
		icyVeins = new IcyVeinsDeckRepository(cardRepository);
		masterCollection = new ExcelDeckRepository(cardRepository);
		userDeck = masterCollection.getDeck("HearthstoneMasterCollection.xlsx");
	}

	@Test
	public void test1() throws Exception {
		diffAgainst(
				"http://www.icy-veins.com/hearthstone/legendary-druid-fast-brm-deck",
				0, 11180);
	}

	@Test
	public void test2() throws Exception {
		diffAgainst(
				"http://www.icy-veins.com/hearthstone/legendary-dragon-ramp-druid-brm-deck",
				2800, 8160);
	}

	@Test
	public void test3() throws Exception {
		diffAgainst(
				"http://www.icy-veins.com/hearthstone/mid-budget-ramp-druid-brm-deck",
				800, 6300);
	}

	private void diffAgainst(String url, int expectedRequiredDust,
			int expectedFullDust) throws Exception {
		Deck desiredDeck = icyVeins.getDeck(url);
		DeckDiff deckDiff = new DeckDiff(desiredDeck, userDeck);
		Deck missing = deckDiff.getMissingCards();

		LOG.debug(url);
		LOG.debug(missing.toString());
		LOG.debug("Required dust: " + deckDiff.getRequiredDust());

		for (DeckCard card : desiredDeck.getCards()) {
			LOG.debug(card.toString() + ": " + card.getDustValue());
		}

		assertThat(deckDiff.getRequiredDust(), equalTo(expectedRequiredDust));
		assertThat(desiredDeck.getDustValue(), equalTo(expectedFullDust));
	}
}
