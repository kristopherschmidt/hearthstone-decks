package com.kschmidt.hearthstone.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.junit.Test;

public class UserCsvDeckGatewayTest {

	@Test
	public void test2ColumnRead() throws IOException {
		UserCsvDeckGateway gateway = new UserCsvDeckGateway();
		Deck deck = gateway
				.get("DruidCards2Column.csv");
		assertThat(deck.getSize(), equalTo(27));
	}
	
	@Test
	public void test4ColumnRead() throws IOException {
		UserCsvDeckGateway gateway = new UserCsvDeckGateway();
		Deck deck = gateway
				.get("DruidCards4Column.csv");
		assertThat(deck.getSize(), equalTo(27));
	}
	
	@Test
	public void testDruidAndNeutrals() throws IOException {
		UserCsvDeckGateway gateway = new UserCsvDeckGateway();
		Deck deck = gateway
				.get("DruidAndNeutralCards.csv");
		assertThat(deck.getSize(), equalTo(164));
	}

}
