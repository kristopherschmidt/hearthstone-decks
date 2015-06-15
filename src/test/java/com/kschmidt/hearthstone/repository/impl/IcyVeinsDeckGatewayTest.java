package com.kschmidt.hearthstone.repository.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.DeckRepository;

public class IcyVeinsDeckGatewayTest {

	@Test
	public void test() throws Exception {
		DeckRepository icyVeins = new IcyVeinsDeckRepository(
				new JSONCardRepository("AllSets.json"));
		Deck deck = icyVeins
				.getDeck("http://www.icy-veins.com/hearthstone/legendary-druid-fast-brm-deck");
		assertThat(deck.getNumCards(), equalTo(30));
	}

}
