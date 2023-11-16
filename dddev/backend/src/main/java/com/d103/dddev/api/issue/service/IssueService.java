package com.d103.dddev.api.issue.service;

import java.util.List;
import java.util.Map;

import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.model.dto.IssueDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface IssueService  {

    IssueDto.Create.Response issueCreate(Integer groundId, IssueDto.Create.Request request, UserDetails userDetails);

    IssueDto.List.Response issueList(Integer groundId, String checkId);

    IssueDto.Detail.Response issueDetail(Integer groundId, String issueId);

    IssueDto.Delete.Response issueDelete(String issueId);

    IssueDto.Content.Response issueContent(IssueDto.Content.Request request, String issueId, UserDetails userDetails);

    IssueDto.Status.Response issueStatus(IssueDto.Status.Request request, String issueId, UserDetails userDetails);

    IssueDto.Connect.Response issueConnect(IssueDto.Connect.Request request, String issueId, UserDetails userDetails);

    IssueDto.Time.Response issueTime(IssueDto.Time.Request request, String issueId, UserDetails userDetails);

    IssueDto.Sprint.Response issueSprint(IssueDto.Sprint.Request request, String issueId, UserDetails userDetails);
    void changeIssuesStatusWhenSprintDelete(Integer sprintId) throws Exception;
    void changeIssuesStatusWhenSprintComplete(Integer sprintId) throws Exception;
    void deleteAllIssuesWhenGroundDelete(int groundId) throws Exception;

    Integer getSprintTotalFocusTime(Integer sprintId) throws Exception;
    Integer getSprintTotalActiveTime(Integer sprintId) throws Exception;

    List<Issue> getSprintFocusIssueDoneAsc(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintFocusTime(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintActiveTime(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintTotalTime(Integer sprintId) throws Exception;

    Map<String, Integer> getSprintFocusTimeCount(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintActiveTimeCount(Integer sprintId) throws Exception;
    Map<String, Integer> getSprintTotalTimeCount(Integer sprintId) throws Exception;

    IssueDto.Title.Response issueTitle(IssueDto.Title.Request request, String issueId, UserDetails userDetails);

    IssueDto.List.Response issueTotalList(Integer groundId, String checkId);

    IssueDto.List.Response issueBackLog(Integer groundId, Integer sprintId);

    IssueDto.SprintList.Response issueSprintList(IssueDto.SprintList.Request request, UserDetails userDetails);

    IssueDto.Status.Response issueStatusToggle(String issueId, UserDetails userDetails);
}
