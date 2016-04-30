package com.kschmidt.hearthstone.domain;

public enum Rarity {

	FREE(0), COMMON(40), RARE(100), EPIC(400), LEGENDARY(1600);

	private int dustValue;

	private Rarity(int dustValue) {
		this.dustValue = dustValue;
	}

	public int getDustValue() {
		return dustValue;
	}

}
