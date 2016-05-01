package com.kschmidt.hearthstone.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.google.common.base.Strings;
import com.kschmidt.hearthstone.domain.Deck;

public class MongoDeckRepositoryImpl implements CustomMongoDeckRepository {

	private static final Logger LOG = LoggerFactory.getLogger(MongoDeckRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<Deck> find(String collectionName, List<String> cardNames, List<String> playerClasses) {
		LOG.info("find(" + collectionName + ", " + cardNames + ", " + playerClasses + ")");
		LOG.info("cardNames size: "+cardNames.size());
		Query query = new Query();
		if (collectionName != null && !Strings.isNullOrEmpty(collectionName)) {
			query.addCriteria(Criteria.where("collection").is(collectionName));
		}
		if (cardNames != null && !cardNames.isEmpty()) {
			query.addCriteria(Criteria.where("cards.card.name").all(cardNames));
		}
		if (playerClasses != null && !playerClasses.isEmpty()) {
			query.addCriteria(Criteria.where("playerClass").in(playerClasses));
		}
		return mongoTemplate.find(query, Deck.class);
	}

}
