package com.kschmidt.hearthstone.repository;

import java.util.List;

public interface CardRepository {

	public abstract List<Card> getCards();

	public abstract Card findCard(String cardName);

}