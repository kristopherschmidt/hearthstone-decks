package com.kschmidt.hearthstone.repository;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kschmidt.hearthstone.domain.DeckCard;

public class IcyVeinsDeckGateway {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(IcyVeinsDeckGateway.class);

	public Deck get(String url) throws IOException {
		Deck deck = new Deck();
		Document doc = Jsoup.connect(url).get();
		Element deckCardListTable = doc.select("table.deck_card_list").get(0);
		Elements cards = deckCardListTable.select("td ul li");
		Pattern p = Pattern.compile("(\\d)x *(.*?)( (GvG|BrM|Naxx))?");
		for (int i = 0; i < cards.size(); ++i) {
			Element card = cards.get(i);
			Matcher m = p.matcher(card.text());
			if (m.matches()) {
				DeckCard deckCard = new DeckCard(m.group(2), Integer.parseInt(m
						.group(1)), m.group(4));
				deck.add(deckCard);
			} else {
				throw new IllegalStateException("Text: '" + card.text()
						+ "' did not parse as a card");
			}
		}
		return deck;
	}

}
