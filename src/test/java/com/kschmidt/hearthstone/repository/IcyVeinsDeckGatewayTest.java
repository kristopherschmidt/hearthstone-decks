package com.kschmidt.hearthstone.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.junit.Test;

public class IcyVeinsDeckGatewayTest {

	@Test
	public void test() throws IOException {
		IcyVeinsDeckGateway gateway = new IcyVeinsDeckGateway();
		Deck deck = gateway
				.get("http://www.icy-veins.com/hearthstone/legendary-druid-fast-brm-deck");
		assertThat(deck.getNumCards(), equalTo(30));
	}

}
