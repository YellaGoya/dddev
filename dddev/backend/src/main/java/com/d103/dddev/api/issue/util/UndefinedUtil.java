package com.d103.dddev.api.issue.util;

import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.repository.IssueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UndefinedUtil {

    private final IssueRepository issueRepository;

    @Transactional
    public void createUndefined(GroundDto groundDto){
        Issue target = Issue.builder()
                .groundId(groundDto.getId())
                .childrenId(new ArrayList<>())
                .step(1) // 최상단 문서의 단계는 1
                .type("target")
                .title("미분류")
                .unclassified(true)
                .build();

        issueRepository.save(target);
        Issue check = Issue.builder()
                .groundId(groundDto.getId())
                .parentId(target.getId())
                .childrenId(new ArrayList<>())
                .step(2)
                .type("check")
                .title("미분류")
                .unclassified(true)
                .build(); // 체크포인트 문서 객체 생성

        issueRepository.save(check);

        target.setChildrenId(List.of(check.getId()));

        issueRepository.save(target);
    }

}
