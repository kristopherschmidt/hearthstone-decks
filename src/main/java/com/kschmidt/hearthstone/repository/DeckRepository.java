package com.kschmidt.hearthstone.repository;

import java.io.IOException;

public interface DeckRepository {

	public abstract Deck get(String url) throws IOException;

}