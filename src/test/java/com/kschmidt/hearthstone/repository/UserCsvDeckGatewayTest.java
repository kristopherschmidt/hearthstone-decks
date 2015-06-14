package com.kschmidt.hearthstone.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.junit.Test;

public class UserCsvDeckGatewayTest {

	@Test
	public void test() throws IOException {
		UserCsvDeckGateway gateway = new UserCsvDeckGateway();
		Deck deck = gateway
				.get("HearthstoneCollection.csv");
		assertThat(deck.getSize(), equalTo(27));
	}

}
