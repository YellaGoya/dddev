package com.d103.dddev.api.general.repository;

import com.d103.dddev.api.general.collection.General;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GeneralRepository extends MongoRepository<General, String> {
    Optional<List<General>> findByParentId(String parentId);
    Optional<List<General>> findByGroundIdAndStep(int groundId, int step);
    Optional<General> findByGroundIdAndUnclassified(int groundId, boolean unclassified);
}
