package com.kschmidt.hearthstone.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.impl.IcyVeinsDeckRepository;
import com.kschmidt.hearthstone.repository.impl.JSONCardRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryConfiguration.class })
public class RepositoryConfigurationTest {

	@Autowired
	private IcyVeinsDeckRepository icyVeinsDeckRepository;

	@Autowired
	private JSONCardRepository jsonCardRepository;

	@Autowired
	Deck userDeck;

	@Test
	public void testComponentCreation() {
		Assert.notNull(icyVeinsDeckRepository);
		Assert.notNull(jsonCardRepository);
		Assert.notNull(userDeck);
	}

}
