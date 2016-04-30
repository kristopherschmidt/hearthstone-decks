package com.kschmidt.hearthstone.repository.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kschmidt.hearthstone.App;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@ActiveProfiles("integrationtest")
public class MongoDeckRepositoryTransactionalTest {

	@Autowired
	private IcyVeinsDeckRepository repository;

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void setUp() {
		setupTestData();
	}

	@Test
	public void testSaveAndReadBack() throws IOException {
		String deckUrl = "http://www.icy-veins.com/hearthstone/legendary-ramp-c-thun-druid-standard-deck";
		Deck deck = repository.getDeck(deckUrl);
		assertNotNull(deck.getLastUpdated());
		mongoDeckRepository.save(deck);
		Deck deckRead = mongoDeckRepository.findByUrl(deckUrl);

		System.out.println(deckRead);
		System.out.println(deckRead.getLastUpdated());
	}

	private void setupTestData() {
		mongoTemplate.dropCollection("deck");
	}

}
