package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.issue.model.dto.IssueDto;
import com.d103.dddev.api.issue.repository.IssueRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IssueServiceImpl implements IssueService{
    private final IssueRepository issueRepository;
    @Override
    public IssueDto.Response create(IssueDto.Request request) {

        return null;
    }
}
