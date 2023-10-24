package com.d103.dddev.api.issue.repository;

import com.d103.dddev.api.issue.model.document.Issue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends MongoRepository<Issue, String> {

}
