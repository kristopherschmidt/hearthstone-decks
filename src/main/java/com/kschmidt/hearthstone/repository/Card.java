package com.kschmidt.hearthstone.repository;

import com.google.common.base.Strings;

public class Card {

	private String id;
	private String name;
	private String rarity;

	public Card(String id, String name, String rarity) {
		if (Strings.isNullOrEmpty(id)) {
			throw new IllegalArgumentException("Card is missing id (name = "
					+ name + ")");
		}
		if (Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Card is missing name (id = "
					+ id + ")");
		}
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
