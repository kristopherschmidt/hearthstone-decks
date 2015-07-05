package com.kschmidt.hearthstone.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class Card {

	private static final Logger LOG = LoggerFactory.getLogger(Card.class);

	private String id;
	private String name;
	private PlayerClass playerClass;
	private Rarity rarity;

	public Card() {

	}

	public Card(String id, String name, String rarity) {
		this(id, name, rarity, null);
	}

	public Card(String id, String name, String rarityString,
			String playerClassString) {
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
		if (playerClassString != null) {
			try {
				this.playerClass = PlayerClass.valueOf(playerClassString);
			} catch (IllegalArgumentException ex) {
				LOG.error("Unknown card class: '" + playerClassString
						+ "' for card: " + name, ex);
			}
		} else {
			playerClass = PlayerClass.Neutral;
		}
		if (rarityString != null) {
			try {
				this.rarity = Rarity.valueOf(rarityString);
			} catch (IllegalArgumentException ex) {
				LOG.error("Unknown card rarity: '" + rarityString
						+ "' for card: " + name, ex);
			}
		}
	}

	public int getDustValue() {
		if (rarity != null) {
			return rarity.getDustValue();
		} else {
			throw new IllegalStateException(
					"Tried to get dust value of card with no rarity: " + id
							+ "/" + name);
		}
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public String toString() {
		return name;
	}

}
