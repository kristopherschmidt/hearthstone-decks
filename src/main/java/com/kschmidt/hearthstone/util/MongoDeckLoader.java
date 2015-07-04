package com.kschmidt.hearthstone.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;
import com.kschmidt.hearthstone.repository.WebDeckRepository;

@Component
public class MongoDeckLoader {

	private static final Logger LOG = LoggerFactory
			.getLogger(MongoDeckLoader.class);

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Autowired
	Map<String, WebDeckRepository> webDeckRepositories;

	public void refreshAll() throws IOException {
		deleteAll();
		for (String collectionName : webDeckRepositories.keySet()) {
			load(collectionName);
		}
	}

	void deleteAll() {
		mongoDeckRepository.deleteAll();
	}

	private void load(String collectionName) throws IOException {
		WebDeckRepository repo = webDeckRepositories.get(collectionName);
		List<Deck> decks = repo.getAllDecks();
		mongoDeckRepository.save(decks);
		LOG.info("Loaded decks for collection: " + collectionName);
	}

}
