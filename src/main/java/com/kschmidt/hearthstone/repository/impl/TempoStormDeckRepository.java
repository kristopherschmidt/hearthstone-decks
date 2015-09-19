package com.kschmidt.hearthstone.repository.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.repository.CardRepository;
import com.kschmidt.hearthstone.repository.WebDeckRepository;

public class TempoStormDeckRepository implements WebDeckRepository {

	private static final Logger LOG = LoggerFactory
			.getLogger(TempoStormDeckRepository.class);

	private Map<String, String> cardNameCorrections;
	private CardRepository cardRepository;
	private HttpHeaders headers;
	private ObjectMapper mapper;
	private RestTemplate restTemplate;
	private List<String> unknownCards;

	public TempoStormDeckRepository(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
		cardNameCorrections = new HashMap<String, String>();
		cardNameCorrections.put("Staemwheedle Sniper", "Steamwheedle Sniper");
		cardNameCorrections.put("Ship Cannon", "Ship's Cannon");
		cardNameCorrections.put("Bouncing Blades", "Bouncing Blade");
		unknownCards = new ArrayList<String>();
		unknownCards.add("Hemit Nesingwary");
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.set("Content-Type", "application/json;charset=UTF-8");
		mapper = new ObjectMapper();
	}

	@SuppressWarnings("unchecked")
	public Deck getDeck(String slug) throws IOException {
		LOG.info("TempoStorm fetching deck with slug: " + slug);
		Deck deck = new Deck(slug);
		deck.setCollection("tempoStormDeckRepository");
		deck.setUrl("https://tempostorm.com/hearthstone/decks/" + slug);

		String body = "{\"slug\":\"" + slug + "\" }";
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<String> result = restTemplate.exchange(
				"https://tempostorm.com/deck", HttpMethod.POST, entity,
				String.class);
		String json = result.getBody();

		Map<String, Object> data = mapper.readValue(json, Map.class);
		Map<String, Object> deckMap = (Map<String, Object>) data.get("deck");
		deck.setRating(getRating(deckMap));
		List<Map<String, Object>> cards = (List<Map<String, Object>>) deckMap
				.get("cards");
		for (Map<String, Object> cardMap : cards) {
			String cardName = getCardName(cardMap);
			try {
				deck.add(new DeckCard(cardRepository.findCard(cardName),
						(Integer) cardMap.get("qty")));
			} catch (NoSuchElementException ex) {
				if (!unknownCards.contains(cardName)) {
					LOG.error("Card not found: " + cardName, ex);
					throw ex;
				} else {
					return null;
				}
			}
		}
		return deck;
	}

	public List<Deck> getAllDecks() throws IOException {
		return getDecks("https://tempostorm.com/decks");
	}

	@Cacheable("decks")
	public List<Deck> getDecks(String serviceUrl) throws IOException {
		LOG.info("TempoStorm fetching all decks from: " + serviceUrl);
		List<Deck> decks = new ArrayList<Deck>();
		for (String deckSlug : getDeckSlugs(serviceUrl)) {
			Deck deck = getDeck(deckSlug);
			if (deck != null) {
				decks.add(deck);
			}
		}
		if (decks.isEmpty()) {
			throw new IllegalArgumentException("No decks found at: "
					+ serviceUrl);
		}
		LOG.info("TempoStorm fetched a total of: " + decks.size() + " decks");
		return decks;
	}

	@SuppressWarnings("unchecked")
	List<String> getDeckSlugs(String url) throws JsonParseException,
			JsonMappingException, IOException {
		List<String> slugs = new ArrayList<String>();
		String body = "{\"klass\":\"all\",\"page\":1,\"perpage\":200,\"search\":\"\",\"age\":\"120\",\"order\":\"high\"}";
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<String> result = restTemplate.exchange(url,
				HttpMethod.POST, entity, String.class);
		String json = result.getBody();

		Map<String, Object> data = mapper.readValue(json, Map.class);
		List<Map<String, Object>> decks = (List<Map<String, Object>>) data
				.get("decks");
		for (Map<String, Object> deck : decks) {
			slugs.add((String) deck.get("slug"));
		}
		return slugs;
	}

	@SuppressWarnings("unchecked")
	private String getCardName(Map<String, Object> cardMap) {
		Map<String, Object> cardInternal = (Map<String, Object>) cardMap
				.get("card");
		String cardName = (String) cardInternal.get("name");
		if (cardNameCorrections.containsKey(cardName)) {
			return cardNameCorrections.get(cardName);
		} else {
			return cardName;
		}
	}

	@SuppressWarnings("unchecked")
	int getRating(Map<String, Object> deckMap) {
		Integer rating = 0;
		List<Map<String, Integer>> votes = (List<Map<String, Integer>>) deckMap
				.get("votes");
		for (Map<String, Integer> vote : votes) {
			rating += ((Integer) vote.get("direction"));
		}
		return rating;
	}

}
