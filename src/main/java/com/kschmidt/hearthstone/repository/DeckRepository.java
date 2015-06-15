package com.kschmidt.hearthstone.repository;

public interface DeckRepository {

	public abstract Deck getDeck(String url) throws Exception;

}