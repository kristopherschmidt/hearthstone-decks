package com.kschmidt.hearthstone.web.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.kschmidt.hearthstone.util.MongoDeckLoader;

@RestController
public class DeckResource {

	@Autowired
	private MongoDeckLoader mongoDeckLoader;

	@RequestMapping(value = "/api/decks/refresh", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean refreshAll(
			@RequestParam(required = false, value = "collection") String collectionName)
			throws IOException {
		if (Strings.isNullOrEmpty(collectionName)) {
			mongoDeckLoader.refreshAll();
		} else {
			mongoDeckLoader.refresh(collectionName);
		}
		return true;
	}

	public MongoDeckLoader getMongoDeckLoader() {
		return mongoDeckLoader;
	}

	public void setMongoDeckLoader(MongoDeckLoader mongoDeckLoader) {
		this.mongoDeckLoader = mongoDeckLoader;
	}

}
