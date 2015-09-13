package com.kschmidt.hearthstone.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.kschmidt.hearthstone.util.MongoDeckLoader;

public class DeckResourceTest {

	@InjectMocks
	private DeckResource deckResource;

	@Mock
	private MongoDeckLoader mongoDeckLoader;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(deckResource).build();
	}

	@Test
	public void testRefreshSpecificCollection() throws Exception {

		this.mockMvc
				.perform(
						get("/api/decks/refresh")
								.param("collection", "abc")
								.accept(MediaType
										.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("application/json;charset=UTF-8"));

		Mockito.verify(mongoDeckLoader).refresh("abc");

	}

	@Test
	public void testRefreshAll() throws Exception {

		this.mockMvc
				.perform(
						get("/api/decks/refresh")
								.param("collection", "")
								.accept(MediaType
										.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("application/json;charset=UTF-8"));

		Mockito.verify(mongoDeckLoader).refreshAll();

	}

}
