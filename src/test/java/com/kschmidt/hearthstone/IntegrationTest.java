package com.kschmidt.hearthstone;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kschmidt.hearthstone.domain.DeckDiff;
import com.kschmidt.hearthstone.repository.Deck;
import com.kschmidt.hearthstone.repository.DeckRepository;
import com.kschmidt.hearthstone.repository.ExcelMasterCollection;
import com.kschmidt.hearthstone.repository.impl.IcyVeinsDeckRepository;
import com.kschmidt.hearthstone.repository.impl.JSONCardRepository;

public class IntegrationTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(IntegrationTest.class);

	private DeckRepository icyVeins;
	private ExcelMasterCollection masterCollection;
	private Deck userDeck;

	public IntegrationTest() throws InvalidFormatException, IOException {
		icyVeins = new IcyVeinsDeckRepository(new JSONCardRepository(
				"AllSets.json"));
		masterCollection = new ExcelMasterCollection(
				"HearthstoneMasterCollection.xlsx", new JSONCardRepository(
						"AllSets.json"));
		userDeck = masterCollection.getDeck();
	}

	@Test
	public void test() throws IOException, InvalidFormatException {
		diffAgainst("http://www.icy-veins.com/hearthstone/legendary-druid-fast-brm-deck");
	}

	@Test
	public void test2() throws IOException, InvalidFormatException {
		diffAgainst("http://www.icy-veins.com/hearthstone/legendary-dragon-ramp-druid-brm-deck");
	}

	private void diffAgainst(String url) throws IOException {
		Deck desiredDeck = icyVeins.get(url);
		DeckDiff deckDiff = new DeckDiff(desiredDeck, userDeck);
		Deck missing = deckDiff.getMissingCards();
		LOG.debug(missing.toString());
	}

}
