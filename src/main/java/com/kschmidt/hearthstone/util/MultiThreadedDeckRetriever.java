package com.kschmidt.hearthstone.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.WebDeckRepository;

public class MultiThreadedDeckRetriever {

	private static final Logger LOG = LoggerFactory.getLogger(MultiThreadedDeckRetriever.class);

	public List<Deck> getDecks(List<String> deckUrls, final WebDeckRepository deckRepository) {
		List<Deck> decks = new ArrayList<Deck>();
		List<Callable<Deck>> deckRetrievalTasks = new ArrayList<Callable<Deck>>();
		for (final String deckUrl : deckUrls) {
			deckRetrievalTasks.add(new Callable<Deck>() {
				public Deck call() throws Exception {
					return deckRepository.getDeck(deckUrl);
				}
			});
		}
		ExecutorService taskExecutor = Executors.newFixedThreadPool(10);
		try {
			List<Future<Deck>> results = taskExecutor.invokeAll(deckRetrievalTasks);
			for (Future<Deck> result : results) {
				decks.add(result.get());
			}
		} catch (InterruptedException e1) {
			throw new RuntimeException("Deck retrieval was interrupted", e1);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			} else {
				throw new RuntimeException(e.getCause());
			}
		}
		LOG.info("fetched: " + decks.size() + " decks from: " + deckRepository);
		return decks;
	}

}
