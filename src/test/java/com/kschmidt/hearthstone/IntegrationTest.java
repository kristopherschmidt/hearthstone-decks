package com.kschmidt.hearthstone;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kschmidt.hearthstone.domain.DeckDiff;
import com.kschmidt.hearthstone.repository.Deck;
import com.kschmidt.hearthstone.repository.DeckRepository;
import com.kschmidt.hearthstone.repository.impl.ExcelDeckRepository;
import com.kschmidt.hearthstone.repository.impl.IcyVeinsDeckRepository;
import com.kschmidt.hearthstone.repository.impl.JSONCardRepository;

public class IntegrationTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(IntegrationTest.class);

	private DeckRepository icyVeins;
	private ExcelDeckRepository masterCollection;
	private Deck userDeck;

	public IntegrationTest() throws InvalidFormatException, IOException {
		icyVeins = new IcyVeinsDeckRepository(new JSONCardRepository(
				"AllSets.json"));
		masterCollection = new ExcelDeckRepository(new JSONCardRepository(
				"AllSets.json"));
		userDeck = masterCollection.getDeck("HearthstoneMasterCollection.xlsx");
	}

	@Test
	public void test() throws Exception {
		diffAgainst("http://www.icy-veins.com/hearthstone/legendary-druid-fast-brm-deck");
	}

	@Test
	public void test2() throws Exception {
		diffAgainst("http://www.icy-veins.com/hearthstone/legendary-dragon-ramp-druid-brm-deck");
	}

	private void diffAgainst(String url) throws Exception {
		Deck desiredDeck = icyVeins.getDeck(url);
		DeckDiff deckDiff = new DeckDiff(desiredDeck, userDeck);
		Deck missing = deckDiff.getMissingCards();
		LOG.debug(missing.toString());
	}

}
