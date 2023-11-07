package com.d103.dddev.api.issue.repository;

import com.d103.dddev.api.issue.model.document.Issue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface IssueRepository extends MongoRepository<Issue, String> {

    ArrayList<Issue> findAllByGroundIdAndType(Integer groundId, String type);

    Optional<Issue> findByGroundIdAndId(Integer groundId, String targetId);


    Optional<Issue> findByGroundIdAndUnclassifiedAndType(Integer groundId, boolean unclassified, String type);

    ArrayList<Issue> findAllByGroundIdAndParentIdAndType(Integer groundId, String parentId, String type);

}
