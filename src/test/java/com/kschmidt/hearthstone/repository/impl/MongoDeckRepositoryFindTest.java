package com.kschmidt.hearthstone.repository.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.kschmidt.hearthstone.domain.PlayerClass;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@ActiveProfiles("integrationtest")
public class MongoDeckRepositoryFindTest {

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void setUp() {
		setupTestData();
	}

	@Test
	public void testFindAll() {
		List<Deck> decks = mongoDeckRepository.findAll();
		assertTrue(decks.size() > 100);
	}

	@Test
	public void testFindByCardNameWithQuote() {
		List<Deck> decks = mongoDeckRepository.findDecksContainingCard("C'Thun");
		assertTrue(decks.size() > 1);
		for (Deck deck : decks) {
			assertTrue(deck.findCard("C'Thun").isPresent());
		}
	}

	@Test
	public void testFindByCardNamesWithQuote() {
		List<Deck> decks = mongoDeckRepository.find(null, Arrays.asList("C'Thun", "Twin Emperor Vek'lor"), null);
		assertTrue(decks.size() > 1);
		for (Deck deck : decks) {
			assertTrue(deck.findCard("C'Thun").isPresent());
			assertTrue(deck.findCard("Twin Emperor Vek'lor").isPresent());
		}
	}

	@Test
	public void testFindByCollection() {
		List<Deck> decks = mongoDeckRepository.findByCollection("icyVeinsDeckRepository");
		assertTrue(decks.size() > 1);
		for (Deck deck : decks) {
			assertThat(deck.getCollection(), equalTo("icyVeinsDeckRepository"));
		}
	}

	@Test
	public void testFindByCollectionNullReturnsAllDecks() {
		List<Deck> decks = mongoDeckRepository.findByCollection(null);
		assertThat(decks.size(), equalTo(mongoDeckRepository.findAll().size()));
	}

	@Test
	public void testFindByCollectionEmptyStringReturnsNoDecks() {
		List<Deck> decks = mongoDeckRepository.findByCollection("");
		assertThat(decks.size(), equalTo(0));
	}

	@Test
	public void testFindByCollectionAndCardsWithQuote() {
		List<Deck> decks = mongoDeckRepository.find("icyVeinsDeckRepository", Arrays.asList("C'Thun"), null);
		assertTrue(decks.size() > 0);
		for (Deck deck : decks) {
			assertThat(deck.getCollection(), equalTo("icyVeinsDeckRepository"));
			assertTrue(deck.findCard("C'Thun").isPresent());
		}
	}

	@Test
	public void testFindByCollectionAndCardsWhereCardsAreEmpty() {
		List<Deck> decks = mongoDeckRepository.find(null, new ArrayList<String>(), null);
		assertThat(decks.size(), equalTo(mongoDeckRepository.findAll().size()));
	}

	@Test
	public void testFindByCollectionAndCardsAndPlayerClass() {
		List<Deck> allClasses = mongoDeckRepository.find(null, Arrays.asList("Beckoner of Evil", "Twilight Elder"),
				null);
		List<Deck> druidOnly = mongoDeckRepository.find(null, Arrays.asList("Beckoner of Evil", "Twilight Elder"),
				Arrays.asList(PlayerClass.DRUID.name()));
		assertTrue(druidOnly.size() > 0);
		for (Deck deck : druidOnly) {
			assertThat(deck.getPlayerClass(), equalTo(PlayerClass.DRUID));
			assertTrue(deck.findCard("Beckoner of Evil").isPresent());
			assertTrue(deck.findCard("Twilight Elder").isPresent());
		}
		assertTrue(druidOnly.size() < allClasses.size());
	}

	// Should search for the cards in all collections
	@Test
	public void testFindByCollectionAndCardsAndPlayerClassWhereClassIsEmpty() {
		List<Deck> decks = mongoDeckRepository.find(null, new ArrayList<String>(), new ArrayList<String>());
		assertThat(decks.size(), equalTo(mongoDeckRepository.findAll().size()));
	}

	private void setupTestData() {
		mongoTemplate.dropCollection("deck");
		List<Deck> decks = mongoTemplate.findAll(Deck.class, "deck_copy");
		mongoTemplate.insert(decks, "deck");
	}
}
