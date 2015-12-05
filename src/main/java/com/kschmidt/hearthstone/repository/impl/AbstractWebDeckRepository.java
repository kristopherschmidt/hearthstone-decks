package com.kschmidt.hearthstone.repository.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.WebDeckRepository;
import com.kschmidt.hearthstone.util.MultiThreadedDeckRetriever;

public abstract class AbstractWebDeckRepository implements WebDeckRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractWebDeckRepository.class);

	public List<Deck> getDecks(String deckListUrl) throws IOException {
		LOG.info(this + " fetching decks from: " + deckListUrl);
		List<String> deckUrls = getDeckUrls(deckListUrl);
		return new MultiThreadedDeckRetriever().getDecks(deckUrls, this);
	}

}
