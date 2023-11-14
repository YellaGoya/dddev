package com.d103.dddev.api.sprint.service;

import com.d103.dddev.api.sprint.repository.dto.requestDto.SprintUpdateDto;
import com.d103.dddev.api.sprint.repository.dto.responseDto.SprintResponseDto;
import com.d103.dddev.api.sprint.repository.entity.SprintEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SprintService {
    SprintResponseDto createSprint(int groundId);
    List<SprintResponseDto> loadSprintList(int groundId);
    SprintResponseDto loadSprint(int sprintId) throws Exception;
    List<SprintResponseDto> loadRecentSprint(int sprintId) throws Exception;
    void deleteSprint(int sprintId) throws Exception;
    SprintResponseDto updateSprint(int sprintId, SprintUpdateDto sprintUpdateDto) throws Exception;
    void startSprint(int groundId, int sprintId) throws Exception;
    void completeSprint(int sprintId) throws Exception;
    void deleteAllSprintWhenGroundDelete(int groundId) throws Exception;

    // 차트
    Map<LocalDateTime, Integer> getSprintBurnDownChart(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintFocusTime(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintActiveTime(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintTotalTime(Integer sprintId) throws Exception;

    Map<String, Integer> getSprintFocusTimeCount(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintActiveTimeCount(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintTotalTimeCount(Integer sprintId) throws Exception;

}
