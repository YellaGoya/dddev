package com.d103.dddev.api.general.repository;

import com.d103.dddev.api.general.collection.General;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeneralRepository extends MongoRepository<General, String> {
    public General findByTitle(String title);
}
