package com.kschmidt.hearthstone.domain;

public enum Rarity {

	Free(0), Common(40), Rare(100), Epic(400), Legendary(1600);

	private int dustValue;

	private Rarity(int dustValue) {
		this.dustValue = dustValue;
	}

	public int getDustValue() {
		return dustValue;
	}

}
