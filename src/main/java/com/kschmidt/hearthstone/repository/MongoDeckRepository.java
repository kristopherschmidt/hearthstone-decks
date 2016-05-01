package com.kschmidt.hearthstone.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.kschmidt.hearthstone.domain.Deck;

public interface MongoDeckRepository extends MongoRepository<Deck, String>, CustomMongoDeckRepository {

	@Query(value = "{ 'cards.card.name' : ?0 }")
	List<Deck> findDecksContainingCard(String cardName);


	@Query(value = "{ $or : [ { $where: '?0 == null' } , { collection : ?0 } ] }")
	List<Deck> findByCollection(String collectionName);

	Deck findByUrl(String url);

}
