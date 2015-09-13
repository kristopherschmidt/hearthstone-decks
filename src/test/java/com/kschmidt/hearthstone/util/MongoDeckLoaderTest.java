package com.kschmidt.hearthstone.util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kschmidt.hearthstone.App;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class MongoDeckLoaderTest {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(MongoDeckLoaderTest.class);

	@Autowired
	private MongoDeckLoader loader;

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void setUp() {
		setupTestData();
	}

	@Test
	public void testDeleteByCollection() {
		int tempoStormSize = mongoDeckRepository.findByCollection(
				"tempoStormDeckRepository").size();
		Assert.assertTrue(tempoStormSize > 0);
		int icyVeinsSize = mongoDeckRepository.findByCollection(
				"icyVeinsDeckRepository").size();
		Assert.assertTrue(icyVeinsSize > 0);

		loader.deleteByCollection("tempoStormDeckRepository");

		Assert.assertEquals(0,
				mongoDeckRepository
						.findByCollection("tempoStormDeckRepository").size());
		Assert.assertEquals(icyVeinsSize,
				mongoDeckRepository.findByCollection("icyVeinsDeckRepository")
						.size());
	}
	
	@Test
	public void testRefreshCollection() throws IOException {
		int tempoStormSize = mongoDeckRepository.findByCollection(
				"tempoStormDeckRepository").size();
		loader.refresh("icyVeinsDeckRepository");

		Assert.assertEquals(tempoStormSize,
				mongoDeckRepository.findByCollection("tempoStormDeckRepository")
						.size());
	}

	@Ignore
	@Test
	public void testRefreshAll() throws IOException {
		loader.refreshAll();
		List<Deck> decks = mongoDeckRepository.findAll();
		assertTrue(!decks.isEmpty());
	}

	private void setupTestData() {
		mongoTemplate.dropCollection("deck");
		List<Deck> decks = mongoTemplate.findAll(Deck.class, "deck_copy");
		mongoTemplate.insert(decks, "deck");
	}

}
