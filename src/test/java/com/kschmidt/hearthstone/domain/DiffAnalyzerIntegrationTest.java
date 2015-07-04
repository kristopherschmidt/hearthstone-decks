package com.kschmidt.hearthstone.domain;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kschmidt.hearthstone.App;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class DiffAnalyzerIntegrationTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(DiffAnalyzerIntegrationTest.class);

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Autowired
	private Deck userDeck;

	@Test
	public void testDiffAnalyzer() throws IOException {
		List<DeckDiff> diffs = DeckDiff.diffDecks(userDeck,
				mongoDeckRepository.findAll());
		LOG.debug(diffs.toString());
		DiffAnalyzer analyzer = new DiffAnalyzer(diffs);
		analyzer.filterByPercentComplete(70);
		Deck allMissing = analyzer.getAllMissingCards();

		LOG.info(allMissing.sortByDustValue().toString());
		LOG.info(allMissing.sortByNumCards().toString());
	}

}
