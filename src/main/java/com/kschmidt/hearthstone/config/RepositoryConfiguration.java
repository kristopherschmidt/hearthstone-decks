package com.kschmidt.hearthstone.config;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.repository.impl.ExcelDeckRepository;
import com.kschmidt.hearthstone.repository.impl.IcyVeinsDeckRepository;
import com.kschmidt.hearthstone.repository.impl.JSONCardRepository;

@Configuration
public class RepositoryConfiguration {

	@Bean
	public JSONCardRepository jsonCardRepository() throws JsonParseException,
			JsonMappingException, IOException {
		return new JSONCardRepository("AllSets.json");
	}

	@Bean
	public IcyVeinsDeckRepository icyVeinsDeckRepository()
			throws JsonParseException, JsonMappingException, IOException {
		return new IcyVeinsDeckRepository(jsonCardRepository());
	}

	@Bean
	public Deck userDeck() throws JsonParseException, JsonMappingException,
			IOException, InvalidFormatException {
		ExcelDeckRepository excelDeckRepository = new ExcelDeckRepository(
				jsonCardRepository());
		return excelDeckRepository.getDeck("HearthstoneMasterCollection.xlsx");
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("decks");
	}

}
