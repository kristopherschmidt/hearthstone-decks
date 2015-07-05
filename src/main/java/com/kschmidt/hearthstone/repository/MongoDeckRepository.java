package com.kschmidt.hearthstone.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.kschmidt.hearthstone.domain.Deck;

public interface MongoDeckRepository extends MongoRepository<Deck, String> {

	@Query(value = "{ 'cards.card.name' : ?0 }")
	List<Deck> findDecksContainingCard(String cardName);

	@Query(value = "{ 'cards.card.name' : { $in : ?0 } }")
	List<Deck> findDecksContainingSomeCards(List<String> cardNames);

	@Query(value = "{ 'cards.card.name' : { $all : ?0 } }")
	List<Deck> findDecksContainingAllCards(List<String> cardNames);

	@Query(value = "{ $or : [ { $where: '?0 == null' } , { collection : ?0 } ] }")
	List<Deck> findByCollection(String collectionName);

	@Query(value = "{ $and : [ "
			+ "{ $or : [ { $where: '?0 == null' }, { collection : ?0 } ] }, "
			+ "{ $or : [ { $where: '?1.length == 0' }, { cards.card.name : { $all : ?1 } } ] }"
			+ " ] }")
	List<Deck> findByCollectionAndCards(String collectionName,
			List<String> cardNames);

}
