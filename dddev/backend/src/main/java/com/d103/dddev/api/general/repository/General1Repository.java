package com.d103.dddev.api.general.repository;

import com.d103.dddev.api.general.collection.General1;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface General1Repository extends MongoRepository<General1, String> {
    Optional<List<General1>> findGeneralsByGroundId(int groundId);
//    Optional<List<General1>> findGeneralsByGroundIdAndStep(int groundId, int step);
//    Optional<List<General1>> findGeneralsByGroundId(int groundId, String parentId);
}
