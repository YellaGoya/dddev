package com.d103.dddev.api.issue.util;

import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.repository.GeneralRepository;
import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.repository.IssueRepository;
import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.request.repository.RequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UndefinedUtil {

    private final IssueRepository issueRepository;
    private final RequestRepository requestRepository;
    private final GeneralRepository generalRepository;

    @Transactional
    public void createUndefined(Ground ground){
        Issue target = Issue.builder()
                .groundId(ground.getId())
                .childrenId(new ArrayList<>())
                .step(1) // 최상단 문서의 단계는 1
                .type("target")
                .title("미분류")
                .unclassified(true)
                .build();

        issueRepository.save(target);
        Issue check = Issue.builder()
                .groundId(ground.getId())
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

        Request request = Request.builder()
                .groundId(ground.getId())
                .step(1)
                .title("미분류")
                .unclassified(true)
                .build();

        requestRepository.save(request);

        General general = General.builder()
                .groundId(ground.getId())
                .step(1)
                .title("미분류")
                .unclassified(true)
                .build();

        generalRepository.save(general);
    }

}
