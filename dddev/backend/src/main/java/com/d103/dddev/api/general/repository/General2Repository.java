package com.d103.dddev.api.general.repository;

import com.d103.dddev.api.general.collection.General1;
import com.d103.dddev.api.general.collection.General2;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface General2Repository extends MongoRepository<General2, String> {
    Optional<List<General2>> findByParentId(String parentId);
}
