package com.kschmidt.hearthstone.repository;

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

import com.kschmidt.hearthstone.domain.DeckCard;

/**
 * This parses version 4.3 of Google Sheet Hearthstone Master Collection
 * https://
 * docs.google.com/spreadsheets/d/1VdqhpiremPEiIKmS1YI8_8HkdYb57wb8cD4ZLnxtffU
 * /edit#gid=357052481
 */
public class ExcelMasterCollection {

	private static final String[] SHEETS_WITH_CARDS = new String[] { "Neutral",
			"Druid", "Hunter", "Mage", "Paladin", "Priest", "Rogue", "Shaman",
			"Warlock", "Warrior" };

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ExcelMasterCollection.class);

	private CardRepository cardRepository;
	private Deck deck;

	public ExcelMasterCollection(String filename, CardRepository cardRepository)
			throws InvalidFormatException, IOException {
		this.cardRepository = cardRepository;
		deck = new Deck();
		File file = new File(this.getClass().getClassLoader()
				.getResource(filename).getFile());
		Workbook workbook = new XSSFWorkbook(file);
		try {
			addCardsFromAllSheets(deck, workbook);
		} finally {
			workbook.close();
		}
	}

	public Deck getDeck() {
		return deck;
	}

	private void addCardsFromAllSheets(Deck deck, Workbook workbook) {
		for (int i = 0; i < SHEETS_WITH_CARDS.length; ++i) {
			addCardsFromSheet(SHEETS_WITH_CARDS[i], deck, workbook);
		}
	}

	private void addCardsFromSheet(String sheetName, Deck deck,
			Workbook workbook) {
		Sheet sheet = workbook.getSheet(sheetName);
		Iterator<Row> rowIterator = sheet.rowIterator();
		// skip header row
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getCell(1) == null
					|| "".equals(row.getCell(1).getStringCellValue())) {
				continue;
			}
			String cardName = row.getCell(1).getStringCellValue();
			try {
				Card card = cardRepository.findCard(cardName);
			} catch (Exception ex) {
				throw new IllegalArgumentException("card: '" + cardName
						+ "' from sheet: " + sheetName
						+ " does not exist in repository");
			}

			int numNormalCards = Double.valueOf(
					row.getCell(4).getNumericCellValue()).intValue();
			int numGoldCards = Double.valueOf(
					row.getCell(5).getNumericCellValue()).intValue();
			DeckCard deckCard = new DeckCard(cardName, numNormalCards
					+ numGoldCards);
			deck.add(deckCard);
		}
	}
}
