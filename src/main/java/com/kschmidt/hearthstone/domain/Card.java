package com.kschmidt.hearthstone.domain;

import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class Card {

	private static final Logger LOG = LoggerFactory.getLogger(Card.class);

	private String id;
	private String name;
	private PlayerClass playerClass;
	private Rarity rarity;
	private String cardSet;

	public Card() {

	}

	public Card(String id, String name, String rarityString) {
		this(id, name, rarityString, "Classic", null);
	}

	public Card(String id, String name, String rarityString, String cardSet,
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
			playerClass = PlayerClass.NEUTRAL;
		}
		if (rarityString != null) {
			try {
				this.rarity = Rarity.valueOf(rarityString.toUpperCase());
			} catch (IllegalArgumentException ex) {
				LOG.error("Unknown card rarity: '" + rarityString
						+ "' for card: " + name, ex);
			}
		}
		if (Strings.isNullOrEmpty(cardSet)) {
			throw new IllegalArgumentException("Unknown card set: '" + cardSet
					+ "' for card: " + name);
		}
		this.cardSet = cardSet;
	}

	public String getCardSet() {
		return cardSet;
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
