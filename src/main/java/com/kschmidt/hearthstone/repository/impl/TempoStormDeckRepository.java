package com.kschmidt.hearthstone.repository.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.repository.CardRepository;
import com.kschmidt.hearthstone.repository.WebDeckRepository;

public class TempoStormDeckRepository implements WebDeckRepository {

	private static final Logger LOG = LoggerFactory
			.getLogger(TempoStormDeckRepository.class);

	private CardRepository cardRepository;

	public TempoStormDeckRepository(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	public Deck getDeck(String slug) throws IOException {
		Deck deck = new Deck(slug);
		deck.setUrl("https://tempostorm.com/hearthstone/decks/" + slug);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json;charset=UTF-8");
		String body = "{\"slug\":\"seventythree-aggro-flamewaker-mage\" }";
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<String> result = restTemplate.exchange(
				"https://tempostorm.com/deck", HttpMethod.POST, entity,
				String.class);
		String json = result.getBody();
		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = mapper.readValue(json, Map.class);
		Map<String, Object> deckMap = (Map<String, Object>) data.get("deck");
		List<Map<String, Object>> cards = (List<Map<String, Object>>) deckMap
				.get("cards");
		for (Map<String, Object> cardMap : cards) {
			Map<String, Object> cardInternal = (Map<String, Object>) cardMap
					.get("card");
			deck.add(new DeckCard(cardRepository.findCard((String) cardInternal
					.get("name")), (Integer) cardMap.get("qty")));
		}
		return deck;
	}

	public List<Deck> getAllDecks() throws IOException {
		List<Deck> decks = new ArrayList<Deck>();

		return decks;
	}

	@Cacheable("decks")
	public List<Deck> getDecks(String deckListUrl) throws IOException {
		List<Deck> decks = new ArrayList<Deck>();
		for (String deckSlug : getDeckSlugs()) {
			decks.add(getDeck(deckSlug));
		}
		if (decks.isEmpty()) {
			throw new IllegalArgumentException("No decks found at: "
					+ deckListUrl);
		}
		return decks;
	}

	List<String> getDeckSlugs() {
		List<String> urls = new ArrayList<String>();

		return urls;
	}

}
