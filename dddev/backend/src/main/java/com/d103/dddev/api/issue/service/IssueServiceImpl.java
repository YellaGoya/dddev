package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.model.dto.IssueDto;
import com.d103.dddev.api.issue.repository.IssueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class IssueServiceImpl implements IssueService{
    private final IssueRepository issueRepository;

    // 문서의 최초 생성 시
    @Override
    public IssueDto.Response create(IssueDto.Request request) {
        // 생성 조건
        // 스프린트 지정 또는 미지정 가능

        Issue issue = Issue.builder()
                .groundId(request.getGroundId()) // ground not null
                .sprintId(request.getSprintId()) // 미지정 시 기본값
                .parentId(request.getParentId()) //
                .step(request.getStep()) // not null, confirm step
                .type("issue")
                .status(0) // 진행 예정 상태
                .title("") // 기본값
                .content("") // 기본값
                .build();

        issueRepository.save(issue);
        System.out.println(issue.getId());

        return IssueDto.Response.builder()
                .message("문서 등록이 완료되었습니다.")
                .status(200)
                .success(true)
                .build();
    }
}
