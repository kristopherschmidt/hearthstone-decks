package com.kschmidt.hearthstone.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class DiffAnalyzerTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(DiffAnalyzerTest.class);

	private DiffAnalyzer analyzer;
	private List<DeckDiff> diffs;
	private DeckDiff diff1;
	private DeckDiff diff2;
	private DeckDiff diff3;

	@Before
	public void setUp() {
		diff1 = new DeckDiffBuilder().withUserCard("a", Rarity.Common, 1)
				.withUserCard("d", Rarity.Common, 2)
				.withDesiredCard("a", Rarity.Common, 2).build();
		diff2 = new DeckDiffBuilder().withUserCard("a", Rarity.Common, 1)
				.withDesiredCard("b", Rarity.Rare, 2)
				.withDesiredCard("a", Rarity.Common, 2).build();
		diff3 = new DeckDiffBuilder().withUserCard("a", Rarity.Common, 1)
				.withDesiredCard("a", Rarity.Common, 2)
				.withDesiredCard("c", Rarity.Epic, 1).build();
		diffs = Arrays.asList(new DeckDiff[] { diff1, diff2, diff3 });
		analyzer = new DiffAnalyzer(diffs);
	}

	@Test
	public void testGetAllMissingCards() {
		Deck missingCards = analyzer.getAllMissingCards();
		LOG.debug(missingCards.toString());
		assertThat(missingCards.getCards().size(), equalTo(3));
		assertThat(missingCards.getNumCards(), equalTo(6));

		Optional<DeckCard> card = missingCards.findCard("a");
		Assert.assertTrue(card.isPresent());
		assertThat(card.get().getNumCards(), equalTo(3));

		card = missingCards.findCard("b");
		Assert.assertTrue(card.isPresent());
		assertThat(card.get().getNumCards(), equalTo(2));

		card = missingCards.findCard("c");
		Assert.assertTrue(card.isPresent());
		assertThat(card.get().getNumCards(), equalTo(1));

		card = missingCards.findCard("d");
		Assert.assertFalse(card.isPresent());
	}

	@Test
	public void testSortByNumCards() {
		List<DeckCard> missing = analyzer.getAllMissingCards().sortByNumCards();
		assertThat(missing.get(0).getCardName(), equalTo("a"));
		assertThat(missing.get(1).getCardName(), equalTo("b"));
		assertThat(missing.get(2).getCardName(), equalTo("c"));
	}

	@Test
	public void testSortByDustValue() {
		List<DeckCard> missing = analyzer.getAllMissingCards()
				.sortByDustValue();
		assertThat(missing.get(0).getCardName(), equalTo("c"));
		assertThat(missing.get(1).getCardName(), equalTo("b"));
		assertThat(missing.get(2).getCardName(), equalTo("a"));
	}

	@Test
	public void testFilterByPercentComplete() {
		analyzer.filterByPercentComplete(50);
		List<DeckDiff> diffs = analyzer.getFilteredDiffs();
		assertThat(diffs.size(), equalTo(1));
		Assert.assertSame(diff1, diffs.get(0));

		analyzer.filterByPercentComplete(10);
		diffs = analyzer.getFilteredDiffs();
		assertThat(diffs.size(), equalTo(2));
		Assert.assertSame(diff1, diffs.get(0));
		Assert.assertSame(diff2, diffs.get(1));

		Deck missingCards = analyzer.getAllMissingCards();
		assertThat(missingCards.getCards().size(), equalTo(2));
		assertThat(missingCards.getNumCards(), equalTo(4));
		Optional<DeckCard> card = missingCards.findCard("a");
		Assert.assertTrue(card.isPresent());
		assertThat(card.get().getNumCards(), equalTo(2));
		card = missingCards.findCard("b");
		Assert.assertTrue(card.isPresent());
		assertThat(card.get().getNumCards(), equalTo(2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterByPercentCompleteFailsForPercentageLessThanZero() {
		analyzer.filterByPercentComplete(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterByPercentCompleteFailsForPercentageGreaterThanOneHundred() {
		analyzer.filterByPercentComplete(101);
	}
}
