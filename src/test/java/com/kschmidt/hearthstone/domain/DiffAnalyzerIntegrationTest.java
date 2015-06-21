package com.kschmidt.hearthstone.domain;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kschmidt.hearthstone.config.RepositoryConfiguration;
import com.kschmidt.hearthstone.repository.WebDeckRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryConfiguration.class })
public class DiffAnalyzerIntegrationTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(DiffAnalyzerIntegrationTest.class);

	@Autowired
	@Qualifier("icyVeinsDeckRepository")
	private WebDeckRepository icyVeins;

	@Autowired
	@Qualifier("hearthstoneTopDeckRepository")
	private WebDeckRepository hearthstoneTopDeck;

	@Autowired
	private Deck userDeck;

	@Ignore
	@Test
	public void testIcyVeins() throws IOException {
		testWebDeckRepository(icyVeins);
	}

	@Ignore
	@Test
	public void testHearthstoneTopDeck() throws IOException {
		testWebDeckRepository(hearthstoneTopDeck);
	}

	private void testWebDeckRepository(WebDeckRepository deckRepository)
			throws IOException {
		List<DeckDiff> diffs = DeckDiff.diffDecks(userDeck,
				deckRepository.getAllDecks());
		LOG.debug(diffs.toString());
		DiffAnalyzer analyzer = new DiffAnalyzer(diffs);
		analyzer.filterByPercentComplete(70);
		Deck allMissing = analyzer.getAllMissingCards();

		LOG.debug(allMissing.sortByDustValue().toString());
		LOG.debug(allMissing.sortByNumCards().toString());
	}

}
