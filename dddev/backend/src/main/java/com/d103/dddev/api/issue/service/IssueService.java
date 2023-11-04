package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.issue.model.dto.IssueDto;
import com.d103.dddev.api.issue.model.dto.TargetDto;
import org.springframework.stereotype.Service;

@Service
public interface IssueService  {

    IssueDto.Create.Response issueCreate(String groundId, IssueDto.Create.Request request);

    IssueDto.List.Response issueList(String groundId, String checkId);

    IssueDto.Detail.Response issueDetail(String groundId, String issueId);

    IssueDto.Delete.Response issueDelete(String issueId);

    IssueDto.Content.Response issueContent(IssueDto.Content.Request request, String issueId);

    IssueDto.Status.Response issueStatus(IssueDto.Status.Request request, String issueId);

    IssueDto.Connect.Response issueConnect(IssueDto.Connect.Request request, String issueId);

    IssueDto.Time.Response issueTime(IssueDto.Time.Request request, String issueId);

    IssueDto.Sprint.Response issueSprint(IssueDto.Sprint.Request request, String issueId);
}
