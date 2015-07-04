package com.kschmidt.hearthstone.repository.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import com.kschmidt.hearthstone.domain.Card;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.repository.CardRepository;
import com.kschmidt.hearthstone.repository.WebDeckRepository;

public class IcyVeinsDeckRepository implements WebDeckRepository {

	private static final Logger LOG = LoggerFactory
			.getLogger(IcyVeinsDeckRepository.class);

	private CardRepository cardRepository;

	public IcyVeinsDeckRepository(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	public Deck getDeck(String url) throws IOException {
		Deck deck = new Deck(url);
		deck.setCollection("icyVeinsDeckRepository");
		deck.setUrl(url);
		Document doc = Jsoup.connect(url).get();
		Element deckCardListTable = doc.select("table.deck_card_list").get(0);
		Elements cardElements = deckCardListTable.select("td ul li");
		Pattern p = Pattern.compile("(\\d)x *(.*?)( (GvG|BrM|Naxx))?");
		for (int i = 0; i < cardElements.size(); ++i) {
			Element cardElement = cardElements.get(i);
			Matcher m = p.matcher(cardElement.text());
			if (m.matches()) {
				Card card = cardRepository.findCard(m.group(2));
				DeckCard deckCard = new DeckCard(card, Integer.parseInt(m
						.group(1)));
				deck.add(deckCard);
			} else {
				throw new IllegalStateException("Text: '" + cardElement.text()
						+ "' did not parse as a card");
			}
		}
		return deck;
	}

	public List<Deck> getAllDecks() throws IOException {
		List<Deck> decks = new ArrayList<Deck>();
		String deckListUrlPrefix = "http://www.icy-veins.com/hearthstone/";
		String deckListUrlPostfix = "-decks";
		String[] classes = new String[] { "druid", "hunter", "mage", "paladin",
				"priest", "rogue", "shaman", "warlock", "warrior" };
		for (int i = 0; i < classes.length; ++i) {
			String deckListUrl = deckListUrlPrefix + classes[i]
					+ deckListUrlPostfix;
			LOG.debug("Fetching decks from: " + deckListUrl);
			decks.addAll(getDecks(deckListUrl));
		}
		return decks;
	}

	@Cacheable("decks")
	public List<Deck> getDecks(String deckListUrl) throws IOException {
		List<Deck> decks = new ArrayList<Deck>();
		for (String deckUrl : getDeckUrls(deckListUrl)) {
			if (deckUrl.startsWith("/basic-") || deckUrl.contains("/season-")
					|| deckUrl.endsWith("-mode")) {
				continue;
			}
			decks.add(getDeck(deckUrl));
		}
		return decks;
	}

	List<String> getDeckUrls(String deckListUrl) throws IOException {
		List<String> urls = new ArrayList<String>();
		Document doc = Jsoup.connect(deckListUrl).get();
		Elements deckRows = doc.select("table.deck_presentation tr");
		for (Element deckRow : deckRows) {
			Element link = deckRow.select("td.deck_presentation_name a")
					.first();
			if (link != null) {
				// LOG.debug(link.text());
				urls.add(link.attr("href"));
			}
		}
		return urls;
	}

}
