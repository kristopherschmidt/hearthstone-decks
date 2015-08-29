package com.kschmidt.hearthstone.util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kschmidt.hearthstone.App;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class MongoDeckLoaderTest {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(MongoDeckLoaderTest.class);

	@Autowired
	private MongoDeckLoader loader;

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Ignore
	@Test
	public void testDeleteAll() {
		loader.deleteAll();
		assertTrue(mongoDeckRepository.findAll().isEmpty());
	}

	@Ignore
	@Test
	public void testRefreshAll() throws IOException {
		loader.deleteAll();
		loader.refreshAll();
		List<Deck> decks = mongoDeckRepository.findAll();
		assertTrue(!decks.isEmpty());
	}

}
