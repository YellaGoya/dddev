package com.d103.dddev.api.issue.repository;

import com.d103.dddev.api.issue.model.document.Issue;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends MongoRepository<Issue, String> {
    ArrayList<Issue> findAllByGroundIdAndType(Integer groundId, String type);

    Optional<Issue> findByGroundIdAndId(Integer groundId, String targetId);

	Optional<Issue> findByGroundIdAndUnclassifiedAndType(Integer groundId, boolean unclassified, String type);

	ArrayList<Issue> findAllByGroundIdAndParentIdAndType(Integer groundId, String parentId, String type);
	List<Issue> findBySprintIdAndStatus(Integer sprintId, Integer status);
	List<Issue> findBySprintId(Integer sprintId);

	@Query("{ 'sprint_id' : ?0, 'focus_time' : { $gt : 0 } }")
	List<Issue> getSprintIssueList(Integer sprintId);

	@Query("{ 'sprint_id' : ?0, 'status' : 3, 'focus_time' : { $gt : 0 } }")
	List<Issue> findFocusTimeDone(Integer sprintId);

	@Query("{ 'sprint_id' : ?0, 'status' : 3, 'focus_time' : { $gt : 0 } }")
	List<Issue> findFocusTimeDone(Integer sprintId, Sort sort);

	@Query("{ 'sprint_id' : ?0, 'status' : { $gt : 0, $lt : 3 }, 'focus_time' : { $gt : 0 } }")
	List<Issue> findFocusTimeUndone(Integer sprintId);

	@Query("{ 'sprint_id' : ?0, 'status' : 3, 'active_time' : { $gt : 0 } }")
	List<Issue> findActiveTimeDone(Integer sprintId);

	@Query("{ 'sprint_id' : ?0, 'status' : { $gt : 0, $lt : 3 }, 'active_time' : { $gt : 0 } }")
	List<Issue> findActiveTimeUndone(Integer sprintId);

	@Query(value = "{ 'sprint_id' : ?0, 'status' : 3, 'focus_time' : { $gt : 0 } }", count = true)
	Integer findFocusTimeDoneCount(Integer sprintId);

	@Query(value = "{ 'sprint_id' : ?0, 'status' : { $gt : 0, $lt : 3 }, 'focus_time' : { $gt : 0 } }", count = true)
	Integer findFocusTimeUndoneCount(Integer sprintId);

	@Query(value = "{ 'sprint_id' : ?0, 'status' : 3, 'active_time' : { $gt : 0 } }", count = true)
	Integer findActiveTimeDoneCount(Integer sprintId);

	@Query(value = "{ 'sprint_id' : ?0, 'status' : { $gt : 0, $lt : 3 }, 'active_time' : { $gt : 0 } }", count = true)
	Integer findActiveTimeUndoneCount(Integer sprintId);

}
