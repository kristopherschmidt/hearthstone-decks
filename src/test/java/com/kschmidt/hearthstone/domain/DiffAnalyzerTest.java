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

	private List<DeckDiff> diffs;

	@Before
	public void setUp() {
		DeckDiff diff1 = new DeckDiffBuilder()
				.withUserCard("a", Rarity.Common, 1)
				.withUserCard("d", Rarity.Common, 2)
				.withDesiredCard("a", Rarity.Common, 2).build();
		DeckDiff diff2 = new DeckDiffBuilder()
				.withUserCard("a", Rarity.Common, 1)
				.withDesiredCard("b", Rarity.Rare, 2)
				.withDesiredCard("a", Rarity.Common, 2).build();
		DeckDiff diff3 = new DeckDiffBuilder()
				.withUserCard("a", Rarity.Common, 1)
				.withDesiredCard("a", Rarity.Common, 2)
				.withDesiredCard("c", Rarity.Common, 1).build();
		diffs = Arrays.asList(new DeckDiff[] { diff1, diff2, diff3 });
	}

	@Test
	public void testGetAllMissingCards() {
		DiffAnalyzer analyzer = new DiffAnalyzer(diffs);
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
}
