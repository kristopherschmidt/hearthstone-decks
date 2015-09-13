package com.kschmidt.hearthstone.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
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

	private Set<String> alreadyDiffed = new HashSet<String>();

	/**
	 * Refresh decks in the given collection. Will delete all existing decks in
	 * the collection if the web load is successful.
	 */
	public synchronized void refresh(String collectionName) throws IOException {
		LOG.info("refreshing collection: " + collectionName + " into db");
		WebDeckRepository repo = webDeckRepositories.get(collectionName);
		List<Deck> decks = repo.getAllDecks();
		deleteByCollection(collectionName);
		mongoDeckRepository.save(decks);
		dedupeAll();
		LOG.info("Refreshed decks for collection: " + collectionName);
	}

	public void refreshAll() throws IOException {
		for (String collectionName : webDeckRepositories.keySet()) {
			if (!"hearthstoneTopDeckRepository".equals(collectionName)) {
				refresh(collectionName);
			}
		}
	}

	public void dedupeAll() throws IOException {
		deleteRepeats(mongoDeckRepository.findAll());
	}

	void deleteRepeats(List<Deck> decks) throws IOException {
		LOG.info("deleting repeats from db");
		int compareCount = 0;
		int duplicateCount = 0;
		for (Deck deck1 : decks) {
			for (Deck deck2 : decks) {
				if (alreadyDiffed.contains(deck2.getId())) {
					continue;
				}
				if (!Objects.equal(deck1.getId(), deck2.getId())) {
					compareCount++;
					if (sameCards(deck1, deck2)) {
						LOG.info("Same cards: " + deck1.getName() + ", "
								+ deck2.getName());
						duplicateCount++;
						mongoDeckRepository.delete(deck2.getId());
					}
				}
			}
			alreadyDiffed.add(deck1.getId());
		}
		LOG.info("compare count: " + compareCount);
		LOG.info("duplicate count: " + duplicateCount);
	}

	private boolean sameCards(Deck deck1, Deck deck2) {
		LOG.debug("checking: " + deck1.getName() + " vs " + deck2.getName());
		for (DeckCard deckCard1 : deck1.getCards()) {
			Optional<DeckCard> deckCard2 = deck2.findCard(deckCard1
					.getCardName());
			if (deckCard2.isPresent()
					&& deckCard2.get().getNumCards() == deckCard1.getNumCards()) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	void deleteByCollection(String collectionName) {
		List<Deck> decksInCollection = mongoDeckRepository
				.findByCollection(collectionName);
		mongoDeckRepository.delete(decksInCollection);
	}

}
