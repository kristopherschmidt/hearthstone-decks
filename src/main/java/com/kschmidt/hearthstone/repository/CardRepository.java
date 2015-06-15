package com.kschmidt.hearthstone.repository;

import java.util.List;

import com.kschmidt.hearthstone.domain.Card;

public interface CardRepository {

	public abstract List<Card> getCards();

	public abstract Card findCard(String cardName);

}