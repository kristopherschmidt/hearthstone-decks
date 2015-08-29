package com.kschmidt.hearthstone.repository.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.kschmidt.hearthstone.domain.Card;
import com.kschmidt.hearthstone.domain.Deck;
import com.kschmidt.hearthstone.domain.DeckCard;
import com.kschmidt.hearthstone.repository.CardRepository;

/**
 * This parses version 4.3 of Google Sheet Hearthstone Master Collection
 * https://
 * docs.google.com/spreadsheets/d/1VdqhpiremPEiIKmS1YI8_8HkdYb57wb8cD4ZLnxtffU
 * /edit#gid=357052481
 */
public class ExcelDeckRepository {

	private static final String[] SHEETS_WITH_CARDS = new String[] { "Neutral",
			"Druid", "Hunter", "Mage", "Paladin", "Priest", "Rogue", "Shaman",
			"Warlock", "Warrior" };

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ExcelDeckRepository.class);

	private CardRepository cardRepository;

	public ExcelDeckRepository(CardRepository cardRepository) {
		this.cardRepository = cardRepository;

	}

	public Deck getDeck(String filename) throws InvalidFormatException,
			IOException {
		Deck deck = new Deck(filename);
		File file = new File(this.getClass().getClassLoader()
				.getResource(filename).getFile());
		Workbook workbook = new XSSFWorkbook(file);
		try {
			addCardsFromAllSheets(deck, workbook);
		} finally {
			workbook.close();
		}
		return deck;
	}

	private void addCardsFromAllSheets(Deck deck, Workbook workbook) {
		// Check for single-sheet cardlist version 5 for TGT, else fall back to
		// pre-TGT
		Sheet sheet = workbook.getSheet("Card List");
		if (sheet != null) {
			addCardsFromSheet("Card List", deck, workbook,
					new PostTGTDeckCardBuilder());
		} else {
			for (int i = 0; i < SHEETS_WITH_CARDS.length; ++i) {
				addCardsFromSheet(SHEETS_WITH_CARDS[i], deck, workbook,
						new PreTGTDeckCardBuilder());
			}
		}
	}

	private void addCardsFromSheet(String sheetName, Deck deck,
			Workbook workbook, DeckCardBuilder deckCardBuilder) {
		Sheet sheet = workbook.getSheet(sheetName);
		Iterator<Row> rowIterator = sheet.rowIterator();
		// skip header row
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			DeckCard deckCard = deckCardBuilder
					.buildCardFromRow(row, sheetName);
			if (deckCard != null) {
				deck.add(deckCard);
			}
		}
	}

	private class PreTGTDeckCardBuilder implements DeckCardBuilder {
		public DeckCard buildCardFromRow(Row row, String sheetName) {
			if (row.getCell(1) == null
					|| "".equals(row.getCell(1).getStringCellValue())) {
				return null;
			}
			String cardName = row.getCell(1).getStringCellValue();
			Card card;
			try {
				card = cardRepository.findCard(cardName);
			} catch (Exception ex) {
				throw new IllegalArgumentException("card: '" + cardName
						+ "' from sheet: " + sheetName
						+ " does not exist in repository");
			}

			int numNormalCards = Double.valueOf(
					row.getCell(4).getNumericCellValue()).intValue();
			int numGoldCards = Double.valueOf(
					row.getCell(5).getNumericCellValue()).intValue();
			return new DeckCard(card, numNormalCards + numGoldCards);
		}
	}

	private class PostTGTDeckCardBuilder implements DeckCardBuilder {
		public DeckCard buildCardFromRow(Row row, String sheetName) {
			String cardName = row.getCell(2).getStringCellValue();
			if (Strings.isNullOrEmpty(cardName)) {
				return null;
			}
			Card card;
			try {
				card = cardRepository.findCard(cardName);
			} catch (Exception ex) {
				throw new IllegalArgumentException("card: '" + cardName
						+ "' from sheet: " + sheetName
						+ " does not exist in repository");
			}

			int numNormalCards = Double.valueOf(
					row.getCell(6).getNumericCellValue()).intValue();
			int numGoldCards = Double.valueOf(
					row.getCell(7).getNumericCellValue()).intValue();
			return new DeckCard(card, numNormalCards + numGoldCards);
		}
	}

	private interface DeckCardBuilder {
		DeckCard buildCardFromRow(Row row, String sheetName);
	}

}
