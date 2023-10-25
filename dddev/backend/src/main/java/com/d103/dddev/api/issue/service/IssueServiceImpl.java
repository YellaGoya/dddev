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
    @Override
    public IssueDto.Response create(IssueDto.Request request) {
        // 생성 조건
        // 스프린트 지정 또는 미지정 가능


        Issue issue = Issue.builder()
                .sprintId(request.getSprintId() != null ? request.getSprintId() : "") // 미지정 시 공백
                .parentId(request.getParentId() != null ? request.getParentId() : "") // 연결 문서 지정
                .childrenId(new ArrayList<>()) // 최초 생성이므로 하단 문서 자체가 없음.
                .step(request.getStep()) // 문서 단계 파악 해아함(parentId 기준으로 지정)
                .type("issue")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .startDate(LocalDate.now()) // 시작일자 == 현재 날짜
                .endDate(LocalDate.now().plusDays(7)) // 종료일자(1주일로)
                .workTime(0) // 기본값
                .studyTime(0) // 기본값
                .author("일단은 테스터") // 유저정보 로딩
                .modifier("일단은 테스터(여기는 추후 자동 수정됨)")
                .status(0) // 진행 예정 상태
                .title("") // 기본값
                .content("") // 기본값
                .build();

        issueRepository.save(issue);

        return IssueDto.Response.builder()
                .message("문서 등록이 완료되었습니다.")
                .status(200)
                .success(true)
                .build();
    }
}
