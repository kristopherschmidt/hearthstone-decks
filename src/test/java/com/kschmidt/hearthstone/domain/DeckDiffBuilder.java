package com.kschmidt.hearthstone.domain;

public class DeckDiffBuilder {

	private Deck desiredDeck = new Deck("desiredDeck");
	private Deck userDeck = new Deck("userDeck");

	public DeckDiffBuilder withDesiredCard(String name, Rarity rarity,
			int quantity) {
		desiredDeck.add(new DeckCard(
				new Card(name + "id", name, rarity.name()), quantity));
		return this;
	}

	public DeckDiffBuilder withDesiredCard(String name, Rarity rarity,
			int quantity, PlayerClass playerClass, String cardSet) {
		desiredDeck.add(new DeckCard(new Card(name + "id", name, rarity.name(),
				cardSet, playerClass.name()), quantity));
		return this;
	}

	public DeckDiffBuilder withUserCard(String name, Rarity rarity, int quantity) {
		userDeck.add(new DeckCard(new Card(name + "id", name, rarity.name()),
				quantity));
		return this;
	}

	public DeckDiff build() {
		return new DeckDiff(desiredDeck, userDeck);
	}

}
