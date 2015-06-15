package com.kschmidt.hearthstone.repository;

public class Card {

	private String id;
	private String name;
	private String rarity;

	public Card(String id, String name, String rarity) {
		this.id = id;
		this.name = name;
		this.rarity = rarity;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRarity() {
		return rarity;
	}

}
