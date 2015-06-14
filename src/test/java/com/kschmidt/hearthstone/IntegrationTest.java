package com.kschmidt.hearthstone;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kschmidt.hearthstone.domain.DeckDiff;
import com.kschmidt.hearthstone.repository.Deck;
import com.kschmidt.hearthstone.repository.IcyVeinsDeckGateway;
import com.kschmidt.hearthstone.repository.UserCsvDeckGateway;

public class IntegrationTest {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(IntegrationTest.class);

	private IcyVeinsDeckGateway icyVeinsGateway = new IcyVeinsDeckGateway();
	private UserCsvDeckGateway userCsvGateway = new UserCsvDeckGateway();

	public IntegrationTest() {

	}

	@Test
	public void test() throws IOException {
		Deck desiredDeck = icyVeinsGateway
				.get("http://www.icy-veins.com/hearthstone/legendary-druid-fast-brm-deck");
		Deck userDeck = userCsvGateway
				.get("DruidAndNeutralCards.csv");
		
		DeckDiff deckDiff = new DeckDiff(desiredDeck, userDeck);
		Deck missing = deckDiff.getMissingCards();
		LOG.debug(missing.toString());
		LOG.debug(userDeck.toString());
	}

}
