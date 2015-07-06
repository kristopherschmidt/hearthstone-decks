package com.kschmidt.hearthstone.web.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kschmidt.hearthstone.util.MongoDeckLoader;

@RestController
public class DeckResource {

	@Autowired
	private MongoDeckLoader mongoDeckLoader;

	@RequestMapping(value = "/api/decks/refresh", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean refreshAll() throws IOException {
		mongoDeckLoader.refreshAll();
		return true;
	}

}
