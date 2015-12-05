package com.kschmidt.hearthstone.repository;

import java.io.IOException;
import java.util.List;

import com.kschmidt.hearthstone.domain.Deck;

public interface WebDeckRepository {

	public Deck getDeck(String url) throws IOException;

	public List<String> getDeckUrls(String deckListUrl) throws IOException;

	public List<Deck> getDecks(String url) throws IOException;

	public List<Deck> getAllDecks() throws IOException;

}