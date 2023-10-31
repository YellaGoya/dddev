package com.d103.dddev.api.sprint.service;

import com.d103.dddev.api.sprint.repository.entity.SprintEntity;

import java.util.List;

public interface SprintService {
    public SprintEntity createSprint(int groundId);
    public List<SprintEntity> loadSprintList(int groundId);
    public void deleteSprint(int sprintId);
    public SprintEntity updateSprint(int sprintId, SprintEntity sprint);
    public void startSprint(int sprintId);
    public void completeSprint(int sprintId);
}
