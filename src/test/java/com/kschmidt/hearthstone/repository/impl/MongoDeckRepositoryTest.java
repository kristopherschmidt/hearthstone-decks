package com.kschmidt.hearthstone.repository.impl;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kschmidt.hearthstone.App;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class MongoDeckRepositoryTest {

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Test
	public void testFindAll() {
		List<Deck> decks = mongoDeckRepository.findAll();
		assertTrue(decks.size() > 100);
	}

	@Test
	public void testFindByCardName() {
		List<Deck> decks = mongoDeckRepository
				.findDecksContainingCard("Cruel Taskmaster");
		assertTrue(decks.size() > 1);
		for (Deck deck : decks) {
			assertTrue(deck.findCard("Cruel Taskmaster").isPresent());
		}
	}

	@Test
	public void testFindByCardNamesIn() {
		List<Deck> decks = mongoDeckRepository
				.findDecksContainingSomeCards(Arrays.asList("Cruel Taskmaster",
						"Warsong Commander"));
		assertTrue(decks.size() > 1);
		for (Deck deck : decks) {
			assertTrue(deck.findCard("Cruel Taskmaster").isPresent()
					|| deck.findCard("Warsong Commander").isPresent());
		}
	}

	@Test
	public void testFindByCardNamesAll() {
		List<Deck> decks = mongoDeckRepository
				.findDecksContainingAllCards(Arrays.asList("Cruel Taskmaster",
						"Warsong Commander"));
		assertTrue(decks.size() > 1);
		for (Deck deck : decks) {
			assertTrue(deck.findCard("Cruel Taskmaster").isPresent()
					&& deck.findCard("Warsong Commander").isPresent());
		}
	}

}
