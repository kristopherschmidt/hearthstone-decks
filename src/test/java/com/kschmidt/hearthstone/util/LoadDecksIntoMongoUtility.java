package com.kschmidt.hearthstone.util;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kschmidt.hearthstone.App;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;
import com.kschmidt.hearthstone.repository.WebDeckRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class LoadDecksIntoMongoUtility {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(LoadDecksIntoMongoUtility.class);

	@Autowired
	@Qualifier("hearthstoneTopDeckRepository")
	private WebDeckRepository hearthstoneTopDeck;

	@Autowired
	@Qualifier("icyVeinsDeckRepository")
	private WebDeckRepository icyVeins;

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Autowired
	private Deck userDeck;

	@Ignore
	@Test
	public void deleteAll() {
		mongoDeckRepository.deleteAll();
	}

	@Ignore
	@Test
	public void insertAllCards() throws IOException {
		List<Deck> decks = hearthstoneTopDeck.getAllDecks();
		mongoDeckRepository.save(decks);
		decks = icyVeins.getAllDecks();
		mongoDeckRepository.save(decks);
	}

}
