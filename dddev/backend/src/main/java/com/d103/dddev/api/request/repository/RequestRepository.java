package com.d103.dddev.api.request.repository;

import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.request.collection.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends MongoRepository<Request, String> {
    Optional<List<Request>> findByParentId(String parentId);
    Optional<List<Request>> findByGroundIdAndStep(int groundId, int step);
    Optional<Request> findByGroundIdAndUnclassified(int groundId, boolean unclassified);
    Optional<List<Request>> findByGroundIdAndStepAndStatus(int groundId, int step, int status);
    void deleteByGroundId(int groundId);
}
