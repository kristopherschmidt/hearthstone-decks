package com.kschmidt.hearthstone.repository;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.kschmidt.hearthstone.domain.DeckCard;

public class UserCsvDeckGateway {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(UserCsvDeckGateway.class);

	private CsvMapper mapper;
	private CsvSchema schema;

	public UserCsvDeckGateway() {
		mapper = new CsvMapper();
		schema = mapper.schemaFor(DeckCard.class);
	}

	public Deck get(String filename) throws JsonParseException,
			JsonMappingException, IOException {
		Deck deck = new Deck();
		File csvFile = new File(this.getClass().getClassLoader()
				.getResource(filename).getFile());
		ObjectReader reader = mapper.reader(DeckCard.class).with(schema);
		MappingIterator<DeckCard> cardIterator = reader.readValues(csvFile);
		while (cardIterator.hasNext()) {
			deck.add(cardIterator.next());
		}
		return deck;
	}

}
