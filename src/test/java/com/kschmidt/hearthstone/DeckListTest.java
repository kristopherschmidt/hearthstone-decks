package com.kschmidt.hearthstone;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kschmidt.hearthstone.domain.DeckCard;

public class DeckListTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(DeckListTest.class);

	@Ignore
	@Test
	public void testIt() throws IOException {
		Document doc = Jsoup.connect(
				"http://www.icy-veins.com/hearthstone/warlock-decks").get();
		Elements els = doc.select("table.deck_presentation tr");
		for (int i = 1; i < els.size(); ++i) {
			Element el = els.get(i);
		}
	}

	@Ignore
	@Test
	public void testIt2() throws IOException {
		Document doc = Jsoup
				.connect(
						"http://www.icy-veins.com/hearthstone/legendary-malygos-dragon-warlock-brm-deck")
				.get();
		Element deckCardListTable = doc.select("table.deck_card_list").get(0);
		Elements cards = deckCardListTable.select("td ul li");
		Pattern p = Pattern.compile("(\\d)x *(.*?)( (GvG|BrM|Naxx))?");
		for (int i = 0; i < cards.size(); ++i) {
			Element card = cards.get(i);
			Matcher m = p.matcher(card.text());
			if (m.matches()) {
				DeckCard deckCard = new DeckCard(m.group(2), Integer.parseInt(m
						.group(1)), m.group(4));
			} else {
				throw new IllegalStateException("Text: '" + card.text()
						+ "' did not parse as a card");
			}
		}
	}

	@Test
	public void testIt3() throws JsonParseException, JsonMappingException,
			IOException {
		File jsonCardFile = new File(this.getClass().getClassLoader()
				.getResource("AllSets.json").getFile());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = mapper.readValue(jsonCardFile, Map.class);
		for (String set : data.keySet()) {
			List<Map> cards = (List<Map>) data.get("Basic");
			for (Map card : cards) {
				LOG.debug(card.get("name").toString());
			}
		}
	}

}
