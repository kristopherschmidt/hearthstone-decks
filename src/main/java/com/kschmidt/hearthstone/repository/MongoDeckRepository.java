package com.kschmidt.hearthstone.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kschmidt.hearthstone.domain.Deck;

public interface MongoDeckRepository extends MongoRepository<Deck, String> {

}
