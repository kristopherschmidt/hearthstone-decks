package com.kschmidt.hearthstone.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class Card {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(Card.class);

	private String id;
	private String name;
	private Rarity rarity;

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
		if (rarity != null) {
			try {
				this.rarity = Rarity.valueOf(rarity);
			} catch (IllegalArgumentException ex) {
				LOG.error("Unknown card rarity: '" + rarity + "' for card: "
						+ name, ex);
			}
		}
	}

	public int getDustValue() {
		return rarity.getDustValue();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Rarity getRarity() {
		return rarity;
	}

}
