package com.d103.dddev.api.issue.util;

import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.repository.IssueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UndefinedUtil {

    private final IssueRepository issueRepository;

    public void createUndefined(GroundDto groundDto){
        Issue target = Issue.builder()
                .groundId(groundDto.getId().toString())
                .childrenId(new ArrayList<>())
                .step(1) // 최상단 문서의 단계는 1
                .type("target")
                .title("미분류")
                .unclassified(true)
                .build();

        Issue check = Issue.builder()
                .groundId(groundDto.getId().toString())
                .childrenId(new ArrayList<>())
                .step(2)
                .type("check")
                .title("미분류")
                .unclassified(true)
                .build(); // 체크포인트 문서 객체 생성

        issueRepository.save(target);
        issueRepository.save(check);

    }

}
