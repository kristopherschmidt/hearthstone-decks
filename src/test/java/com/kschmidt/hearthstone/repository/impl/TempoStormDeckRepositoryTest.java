package com.kschmidt.hearthstone.repository.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.repository.CardRepository;

public class TempoStormDeckRepositoryTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(TempoStormDeckRepositoryTest.class);

	private static CardRepository cardRepository;

	private TempoStormDeckRepository tempoStorm;

	@BeforeClass
	public static void setUpClass() throws JsonParseException,
			JsonMappingException, IOException {
		cardRepository = new JSONCardRepository("AllSets.json");
	}

	@Before
	public void setUp() {
		tempoStorm = new TempoStormDeckRepository(cardRepository);
	}

	@Ignore
	@Test
	public void test() throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json;charset=UTF-8");
		String body = "{\"klass\":\"all\",\"page\":1,\"perpage\":100,\"search\":\"\",\"age\":\"90\",\"order\":\"high\"}";
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<String> result = restTemplate.exchange(
				"https://tempostorm.com/decks", HttpMethod.POST, entity,
				String.class);
		String json = result.getBody();

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = mapper.readValue(json, Map.class);
		List<Map<String, Object>> decks = (List<Map<String, Object>>) data
				.get("decks");
		for (Map<String, Object> deck : decks) {
			System.out.println(deck.get("slug"));
			System.out.println(deck.get("votesCount"));
		}

		for (String key : decks.get(0).keySet()) {
			System.out.println(key);
		}

	}

	@Test
	public void testDeck() throws IOException {
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
		Map<String, Object> deck = (Map<String, Object>) data.get("deck");
		for (String property : deck.keySet()) {
			List<Map<String, Object>> cards = (List<Map<String, Object>>) deck
					.get("cards");
			for (Map<String, Object> card : cards) {
				Map<String, Object> cardInternal = (Map<String, Object>) card
						.get("card");
				System.out.println(cardInternal.get("name"));
				System.out.println(card.get("qty"));
			}
		}
	}

	@Test
	public void testGetDeckFromSlug() throws IOException {
		Deck deck = tempoStorm.getDeck("seventythree-aggro-flamewaker-mage");
		assertThat(deck.getNumCards(), equalTo(30));
		assertThat(deck.getName(),
				equalTo("seventythree-aggro-flamewaker-mage"));
		assertThat(
				deck.getUrl(),
				equalTo("https://tempostorm.com/hearthstone/decks/seventythree-aggro-flamewaker-mage"));

		Optional<DeckCard> card = deck.findCard("Clockwork Gnome");
		assertTrue(card.isPresent());
		assertThat(card.get().getCardName(), equalTo("Clockwork Gnome"));
		assertThat(card.get().getNumCards(), equalTo(2));

		card = deck.findCard("Rampage");
		assertFalse(card.isPresent());
	}
}
