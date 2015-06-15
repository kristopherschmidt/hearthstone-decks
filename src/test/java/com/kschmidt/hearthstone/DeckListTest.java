package com.kschmidt.hearthstone;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			LOG.debug(el.toString());
		}
	}

}
