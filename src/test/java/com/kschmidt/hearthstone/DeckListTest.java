package com.kschmidt.hearthstone;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
