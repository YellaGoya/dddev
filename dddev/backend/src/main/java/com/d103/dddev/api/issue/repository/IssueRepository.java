package com.d103.dddev.api.issue.repository;

import com.d103.dddev.api.issue.model.document.Issue;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends MongoRepository<Issue, String> {

	ArrayList<Issue> findAllByGroundIdAndType(String groundId, String type);

	Optional<Issue> findByGroundIdAndId(String groundId, String targetId);

	Optional<Issue> findByGroundIdAndUnclassifiedAndType(String groundId, boolean unclassified, String type);

	ArrayList<Issue> findAllByGroundIdAndParentIdAndType(String groundId, String parentId, String type);

	@Query("{ 'ground_id' : ?0, 'sprint_id' : ?1, 'status' : 3, 'focus_time' : { $gt : 0 } }")
	List<Issue> findFocusTimeDone(Integer groundId, Integer sprintId);

	@Query("{ 'ground_id' : ?0, 'sprint_id' : ?1, 'status' : { $gt : 0, $lt : 3 }, 'focus_time' : { $gt : 0 } }")
	List<Issue> findFocusTimeUndone(Integer groundId, Integer sprintId);

	@Query("{ 'ground_id' : ?0, 'sprint_id' : ?1, 'status' : 3, 'active_time' : { $gt : 0 } }")
	List<Issue> findActiveTimeDone(Integer groundId, Integer sprintId);

	@Query("{ 'ground_id' : ?0, 'sprint_id' : ?1, 'status' : { $gt : 0, $lt : 3 }, 'active_time' : { $gt : 0 } }")
	List<Issue> findActiveTimeUndone(Integer groundId, Integer sprintId);

}
