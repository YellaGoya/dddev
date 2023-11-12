package com.d103.dddev.api.sprint.service;

import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.sprint.repository.dto.SprintUpdateDto;
import com.d103.dddev.api.sprint.repository.entity.SprintEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SprintService {
    SprintEntity createSprint(int groundId);
    List<SprintEntity> loadSprintList(int groundId);
    SprintEntity loadSprint(int sprintId) throws Exception;
    void deleteSprint(int sprintId);
    SprintEntity updateSprint(int sprintId, SprintUpdateDto sprintUpdateDto) throws Exception;
    void startSprint(int groundId, int sprintId) throws Exception;
    void completeSprint(int sprintId) throws Exception;

    // 차트
    Map<LocalDateTime, Integer> getSprintBurnDownChart(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintFocusTime(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintActiveTime(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintTotalTime(Integer sprintId) throws Exception;

    Map<String, Integer> getSprintFocusTimeCount(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintActiveTimeCount(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintTotalTimeCount(Integer sprintId) throws Exception;
}
