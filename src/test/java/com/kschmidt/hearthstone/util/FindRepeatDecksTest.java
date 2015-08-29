package com.kschmidt.hearthstone.util;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kschmidt.hearthstone.App;
import com.kschmidt.hearthstone.repository.MongoDeckRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class FindRepeatDecksTest {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(FindRepeatDecksTest.class);

	@Autowired
	private MongoDeckLoader mongoDeckLoader;

	@Autowired
	private MongoDeckRepository mongoDeckRepository;

	@Ignore
	@Test
	public void testDeleteRepeats() throws IOException {
		mongoDeckLoader.deleteRepeats(mongoDeckRepository.findAll());
	}

}
