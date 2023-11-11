package com.d103.dddev.api.sprint.service;

import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.sprint.repository.dto.SprintUpdateDto;
import com.d103.dddev.api.sprint.repository.entity.SprintEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SprintService {
    public SprintEntity createSprint(int groundId);
    public List<SprintEntity> loadSprintList(int groundId);
    public Optional<SprintEntity> loadSprint(int sprintId);
    public void deleteSprint(int sprintId);
    public SprintEntity updateSprint(int sprintId, SprintUpdateDto sprintUpdateDto);
    public void startSprint(int sprintId);
    public void completeSprint(int sprintId);

    // 차트
    Map<LocalDateTime, Integer> getSprintBurnDownChart(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintFocusTime(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintActiveTime(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintTotalTime(Integer sprintId) throws Exception;

    Map<String, Integer> getSprintFocusTimeCount(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintActiveTimeCount(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintTotalTimeCount(Integer sprintId) throws Exception;
}
