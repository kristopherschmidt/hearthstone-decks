package com.kschmidt.hearthstone.repository.impl;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kschmidt.hearthstone.App;
import com.kschmidt.hearthstone.domain.Card;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class MongoDeckRepositoryTest {

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Ignore
	@Test
	public void testSave() {
		Deck deck = new Deck("deckname");
		deck.add(new DeckCard(new Card("aid", "a", "Common"), 1));
		deck.add(new DeckCard(new Card("bid", "b", "Rare"), 2));
		mongoDeckRepository.save(deck);
	}

	@Test
	public void testLoad() {
		List<Deck> decks = mongoDeckRepository.findAll();
		for (Deck deck : decks) {
			System.out.println(deck);
		}
	}

}
