package com.kschmidt.hearthstone.repository.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
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
import com.kschmidt.hearthstone.repository.WebDeckRepository;
import com.kschmidt.hearthstone.util.MultiThreadedDeckRetriever;

public class HearthpwnRepository implements WebDeckRepository {

	private static final String BASE_URI = "http://www.hearthpwn.com";

	private static final Set<String> BLACKLIST_URLS = new HashSet<String>(
			Arrays.asList("http://www.hearthpwn.com/decks/369529-fuck-you-reno-jackson"));

	private static final Logger LOG = LoggerFactory.getLogger(HearthpwnRepository.class);

	private CardRepository cardRepository;

	public HearthpwnRepository(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@Override
	public Deck getDeck(String url) throws IOException {
		LOG.info("Hearthpwn getDeck: " + url);
		Document doc = getDocument(url);
		Deck deck = new Deck(getDeckTitle(doc));
		deck.setUrl(url);
		deck.setCollection("hearthpwnRepository");
		deck.addAll(getCards(doc, url));
		deck.setRating(getRating(doc));
		deck.setLastUpdated(getLastUpdated(doc));
		return deck;
	}

	private String getDeckTitle(Document doc) {
		return doc.select("header.deck-detail section.deck-info h2.deck-title").get(0).text();
	}

	private List<DeckCard> getCards(Document doc, String url) {
		List<DeckCard> deckCards = new ArrayList<DeckCard>();
		Elements cardElements = doc.select("aside.infobox table.listing-cards-tabular tr td.col-name");
		for (Element cardElement : cardElements) {
			String cardName = cardElement.select("b a").get(0).text();
			Card card = cardRepository.findCard(cardName);
			Pattern p = Pattern.compile(".* (\\d)$");
			Matcher m = p.matcher(cardElement.text());
			if (m.matches()) {
				DeckCard deckCard = new DeckCard(card, Integer.parseInt(m.group(1)));
				deckCards.add(deckCard);
			} else {
				throw new IllegalStateException(
						"Text: '" + cardElement.text() + "' did not parse as a card from: " + url);
			}
		}
		return deckCards;
	}

	private int getRating(Document doc) {
		String ratingString = doc.select(".deck-actions .rating-sum").get(0).text();
		return Integer.parseInt(ratingString);
	}

	private LocalDate getLastUpdated(Document doc) {
		Element e = doc.select(".deck-detail .standard-datetime").first();
		String epochSecondsString = e.attr("data-epoch");
		Instant instant = Instant.ofEpochSecond(Long.parseLong(epochSecondsString));
		return instant.atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@Override
	public List<Deck> getDecks(String deckListUrl) throws IOException {
		LOG.info("Hearthpwn fetching decks from: " + deckListUrl);
		List<Deck> decks = getDecks(getDeckUrls(deckListUrl));
		if (decks.isEmpty()) {
			throw new IllegalArgumentException("No decks found at: " + deckListUrl);
		}
		LOG.info("fetched: " + decks.size() + " decks");
		return decks;
	}

	private List<Deck> getDecks(List<String> deckUrls) {
		MultiThreadedDeckRetriever deckRetriever = new MultiThreadedDeckRetriever();
		return deckRetriever.getDecks(deckUrls, this);
	}

	/**
	 * Filter only decks post-TGT, 1st page of results for now, sorted by
	 * popularity
	 */
	@Override
	public List<Deck> getAllDecks() throws IOException {
		// sample URL for Druid. Filter-build is for TGT. Class is for druid.
		// http://www.hearthpwn.com/decks?filter-build=24&filter-class=100&sort=-rating
		List<Deck> decks = new ArrayList<Deck>();
		String deckListUrlPrefix = "http://www.hearthpwn.com/decks?filter-unreleased-cards=f&filter-build=26&filter-deck-tag=1&filter-class=";
		String deckListUrlPostfix = "&sort=-rating";
		String[] classes = new String[] { "4", "8", "16", "32", "64", "128", "256", "512", "1024" };
		for (int i = 0; i < classes.length; ++i) {
			String deckListUrl = deckListUrlPrefix + classes[i] + deckListUrlPostfix;
			decks.addAll(getDecks(deckListUrl));
		}
		return decks;
	}

	/**
	 * Return relative URLs for decks at the given location. Absolute URLs
	 * cannot be returned because Jsoup has no context.
	 */
	List<String> getDeckUrls(String deckListUrl) throws IOException {
		List<String> deckUrls = new ArrayList<String>();
		Document doc = getDocument(deckListUrl);
		// LOG.debug(doc.toString());
		Elements links = doc.select("table.listing-decks tbody tr td.col-name span a");
		for (Element link : links) {
			String deckUrl = link.attr("abs:href");
			if (!BLACKLIST_URLS.contains(deckUrl)) {
				deckUrls.add(deckUrl);
			}
		}
		return deckUrls;
	}

	/**
	 * Hearthpwn does a cookie check, redirecting from the original url to a
	 * cookie setter to an authentication service and finally back to the
	 * original url where the cookie must be passed. Jsoup does not handle this
	 * by default so use httpclient instead and pass the result to jsoup.
	 * 
	 * @param url
	 *            the url to fetch, which should be relative to
	 *            www.hearthpwn.com
	 */
	Document getDocument(String url) throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create()
				.setDefaultRequestConfig(RequestConfig.custom().setCircularRedirectsAllowed(true).build()).build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		Document doc = Jsoup.parse(result.toString());
		doc.setBaseUri(BASE_URI);
		return doc;
	}

}
