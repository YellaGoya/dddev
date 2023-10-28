package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.issue.model.dto.IssueDto;
import org.springframework.stereotype.Service;

@Service
public interface IssueService  {
    IssueDto.Response create(IssueDto.Request request);
}
