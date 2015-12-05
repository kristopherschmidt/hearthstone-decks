package com.kschmidt.hearthstone.repository.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.kschmidt.hearthstone.domain.Card;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.repository.CardRepository;

public class IcyVeinsDeckRepository extends AbstractWebDeckRepository {

	private static final Logger LOG = LoggerFactory.getLogger(IcyVeinsDeckRepository.class);

	private CardRepository cardRepository;

	public IcyVeinsDeckRepository(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	public Deck getDeck(String url) throws IOException {
		Deck deck = new Deck(url);
		deck.setCollection("icyVeinsDeckRepository");
		deck.setUrl(url);
		Document doc = Jsoup.connect(url).get();

		deck.setLastUpdated(getLastUpdated(doc, url));

		Element deckCardListTable = doc.select("table.deck_card_list").get(0);
		Elements cardElements = deckCardListTable.select("td ul li");
		Pattern p = Pattern.compile("(\\d)x *(.*?)( (GvG|BrM|Naxx|TGT|LoE))?");
		for (int i = 0; i < cardElements.size(); ++i) {
			Element cardElement = cardElements.get(i);
			Matcher m = p.matcher(cardElement.text());
			if (m.matches()) {
				Card card = cardRepository.findCard(m.group(2));
				DeckCard deckCard = new DeckCard(card, Integer.parseInt(m.group(1)));
				deck.add(deckCard);
			} else {
				throw new IllegalStateException("Text: '" + cardElement.text() + "' did not parse as a card");
			}
		}
		return deck;
	}

	private LocalDate getLastUpdated(Document doc, String url) {
		String updatedText = doc.select(".article_author_text").get(0).text();
		Pattern p = Pattern.compile("Last updated.*on (.*)");
		Matcher m = p.matcher(updatedText);
		if (m.matches()) {
			String dateString = m.group(1);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM. uuuu");
			LocalDate date = LocalDate.parse(dateString, formatter);
			return date;
		} else {
			throw new IllegalStateException("Text: " + updatedText + " could not parse a date, from url: " + url);
		}
	}

	public List<Deck> getAllDecks() throws IOException {
		List<Deck> decks = new ArrayList<Deck>();
		String deckListUrlPrefix = "http://www.icy-veins.com/hearthstone/";
		String deckListUrlPostfix = "-decks";
		String[] classes = new String[] { "druid", "hunter", "mage", "paladin", "priest", "rogue", "shaman", "warlock",
				"warrior" };
		for (int i = 0; i < classes.length; ++i) {
			String deckListUrl = deckListUrlPrefix + classes[i] + deckListUrlPostfix;
			decks.addAll(getDecks(deckListUrl));
		}
		return decks;
	}

	public List<String> getDeckUrls(String deckListUrl) throws IOException {
		List<String> urls = new ArrayList<String>();
		Document doc = Jsoup.connect(deckListUrl).get();
		Elements deckRows = doc.select("table.deck_presentation tr");
		for (Element deckRow : deckRows) {
			Element link = deckRow.select("td.deck_presentation_name a").first();
			if (link != null) {
				// LOG.debug(link.text());
				String url = link.attr("href");
				if (!url.startsWith("/basic-") && !url.contains("/season-") || !url.endsWith("-mode")) {
					urls.add(url);
				}
			}
		}
		return urls;
	}

}
