package com.kschmidt.hearthstone.repository.impl;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.repository.CardRepository;

public class TempoStormDeckRepository extends AbstractWebDeckRepository {

	private static final Logger LOG = LoggerFactory.getLogger(TempoStormDeckRepository.class);

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
		cardNameCorrections.put("Ancestors Call", "Ancestor's Call");
		unknownCards = new ArrayList<String>();
		unknownCards.add("Hemit Nesingwary");
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.set("Content-Type", "application/json;charset=UTF-8");
		mapper = new ObjectMapper();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Deck getDeck(String slug) throws IOException {
		LOG.info("TempoStorm fetching deck with slug: " + slug);
		Deck deck = new Deck(slug);
		deck.setCollection("tempoStormDeckRepository");
		deck.setUrl("https://tempostorm.com/hearthstone/decks/" + slug);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://tempostorm.com/api/decks/findOne")
				.queryParam("filter", "{\"where\":{\"slug\":\"" + slug
						+ "\"},\"fields\":[\"id\",\"createdDate\",\"name\",\"description\",\"playerClass\",\"premium\",\"dust\",\"heroName\",\"authorId\",\"deckType\",\"isPublic\",\"chapters\",\"youtubeId\",\"gameModeType\",\"isActive\",\"isCommentable\"],\"include\":[{\"relation\":\"cards\",\"scope\":{\"include\":\"card\",\"scope\":{\"fields\":[\"id\",\"name\",\"cardType\",\"cost\",\"dust\",\"photoNames\"]}}},{\"relation\":\"comments\",\"scope\":{\"fields\":[\"id\",\"votes\",\"authorId\",\"createdDate\",\"text\"],\"include\":{\"relation\":\"author\",\"scope\":{\"fields\":[\"id\",\"username\"]}}}},{\"relation\":\"author\",\"scope\":{\"fields\":[\"id\",\"username\"]}},{\"relation\":\"matchups\",\"scope\":{\"fields\":[\"forChance\",\"deckName\",\"className\"]}},{\"relation\":\"votes\",\"fields\":[\"id\",\"direction\",\"authorId\"]}]}");
		HttpEntity<?> entity = new HttpEntity<>(headers);
		URI uri = builder.build().encode().toUri();
		HttpEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		String json = result.getBody();

		Map<String, Object> data = mapper.readValue(json, Map.class);

		deck.setRating(getRating(data));
		deck.setLastUpdated(getLastUpdated(data));

		List<Map<String, Object>> cards = (List<Map<String, Object>>) data.get("cards");
		for (Map<String, Object> cardMap : cards) {
			String cardName = getCardName(cardMap);
			try {
				deck.add(new DeckCard(cardRepository.findCard(cardName), (Integer) cardMap.get("cardQuantity")));
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

	@Override
	public List<Deck> getAllDecks() throws IOException {
		return getDecks("https://tempostorm.com/api/decks");
	}

	@SuppressWarnings("unchecked")
	public List<String> getDeckUrls(String slugListUrl) throws JsonParseException, JsonMappingException, IOException {
		List<String> slugs = new ArrayList<String>();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://tempostorm.com/api/decks").queryParam(
				"filter",
				"{\"where\":{\"isFeatured\":false,\"isPublic\":true},\"fields\":{\"id\":true,\"name\":true,\"heroName\":true,\"authorId\":true,\"playerClass\":true,\"dust\":true,\"createdDate\":true,\"premium\":true},\"include\":[{\"relation\":\"author\",\"scope\":{\"fields\":[\"id\",\"username\"]}},{\"relation\":\"votes\",\"scope\":{\"fields\":[\"id\",\"direction\",\"authorId\"]}},{\"relation\":\"slugs\"}],\"order\":\"createdDate DESC\",\"skip\":0,\"limit\":104}");

		HttpEntity<?> entity = new HttpEntity<>(headers);
		URI uri = builder.build().encode().toUri();
		HttpEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		String json = result.getBody();

		List<Map<String, Object>> data = mapper.readValue(json, List.class);
		for (Map<String, Object> deck : data) {
			slugs.add(((List<Map<String, String>>) deck.get("slugs")).get(0).get("slug"));
		}

		slugs.remove("old-tempogods");
		slugs.remove("old-tempo-gods");
		slugs.remove("cthun-elisa-jackson");
		slugs.remove("hope-ends-with-tempo");
		slugs.remove("zoo-cancer-12");
		return slugs;
	}

	@SuppressWarnings("unchecked")
	private String getCardName(Map<String, Object> cardMap) {
		Map<String, Object> cardInternal = (Map<String, Object>) cardMap.get("card");
		String cardName = (String) cardInternal.get("name");
		if (cardNameCorrections.containsKey(cardName)) {
			return cardNameCorrections.get(cardName);
		} else {
			return cardName;
		}
	}

	@SuppressWarnings("unchecked")
	int getRating(Map<String, Object> data) {
		List<Map<String, String>> votes = (List<Map<String, String>>) data.get("votes");
		return votes.size();
	}

	private LocalDate getLastUpdated(Map<String, Object> deckMap) {
		// createdDate=2015-04-25T19:30:40.311Z
		String dateString = (String) deckMap.get("createdDate");
		DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());
		return LocalDate.parse(dateString, formatter);
	}

}
