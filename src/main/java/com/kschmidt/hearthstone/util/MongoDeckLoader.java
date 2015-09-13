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
	
	public void refresh(String collectionName) {
		throw new UnsupportedOperationException();	
	}

	public void refreshAll() throws IOException {
		deleteAll();
		for (String collectionName : webDeckRepositories.keySet()) {
			if (!"hearthstoneTopDeckRepository".equals(collectionName)) {
				load(collectionName);
			}
		}
		deleteRepeats(mongoDeckRepository.findAll());
	}

	public void deleteRepeats(List<Deck> decks) throws IOException {
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

	void deleteAll() {
		LOG.info("deleting all deck collections from db");
		mongoDeckRepository.deleteAll();
	}

	private void load(String collectionName) throws IOException {
		LOG.info("loading collection: " + collectionName + " into db");
		WebDeckRepository repo = webDeckRepositories.get(collectionName);
		List<Deck> decks = repo.getAllDecks();
		mongoDeckRepository.save(decks);
		LOG.info("Loaded decks for collection: " + collectionName);
	}

}
