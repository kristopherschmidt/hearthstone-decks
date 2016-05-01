package com.kschmidt.hearthstone.repository;

import java.util.List;

import com.kschmidt.hearthstone.domain.Deck;

public interface CustomMongoDeckRepository {

	List<Deck> find(String collectionName, List<String> cardNames, List<String> playerClasses);

}
