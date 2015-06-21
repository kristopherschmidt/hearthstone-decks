package com.kschmidt.hearthstone.repository;

import java.util.List;

import com.kschmidt.hearthstone.domain.Card;

public interface CardRepository {

	public List<Card> getCards();

	public Card findCard(String cardName);

}