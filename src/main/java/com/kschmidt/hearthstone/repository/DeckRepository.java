package com.kschmidt.hearthstone.repository;

import com.kschmidt.hearthstone.domain.Deck;

public interface DeckRepository {

	public abstract Deck getDeck(String url) throws Exception;

}