package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.model.dto.TargetDto;
import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.model.message.TargetMessage;
import com.d103.dddev.api.issue.repository.IssueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class TargetServiceImpl implements TargetService {

    private final IssueRepository issueRepository;

    @Override
    public TargetDto.Create.Response createTarget(String groundId) {
        Issue target = Issue.builder()
                .groundId(groundId)
                .childrenId(new ArrayList<>())
                .step(1) // 최상단 문서의 단계는 1
                .type("target")
                .title("")
                .content("")
                .build();

        issueRepository.save(target);

        return TargetDto.Create.Response.builder()
                .message(TargetMessage.create())
                .status(HttpStatus.OK.value())
                .target(target)
                .build();
    }

    // 그라운드에 해당하는 목표 문서 리스트 조회

    @Override
    public TargetDto.List.Response targetList(String groundId) {
        ArrayList<Issue> targetList = issueRepository.findAllByGroundIdAndType(groundId,"target"); // 입력한 그라운드에 해당하는 target만

        if(targetList.isEmpty()){
            return TargetDto.List.Response.builder()
                    .message(TargetMessage.emptyList())
                    .status(HttpStatus.OK.value())
                    .targetList(targetList)
                    .build();
        }

        return TargetDto.List.Response.builder()
                .message(TargetMessage.list())
                .status(HttpStatus.OK.value())
                .targetList(targetList)
                .build();
    }

    @Override
    public TargetDto.Detail.Response targetDetail(String groundId, String targetId) {
        // targetId만 있어도 조회는 가능 하지만 조금 더 확실히 하기 위해 두개를 비교
        Issue target = issueRepository.findByGroundIdAndId(groundId, targetId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        return TargetDto.Detail.Response.builder()
                .message(TargetMessage.detail())
                .status(HttpStatus.OK.value())
                .target(target)
                .build();
    }

    @Override
    @Transactional
    public TargetDto.Delete.Response targetDelete(String groundId, String targetId) {
        // 해당 그라운드에 해당 목표 문서가 존재하는지 존재 여부 판단
        Issue target = issueRepository.findByGroundIdAndId(groundId, targetId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        Issue unclassified = issueRepository.findByGroundIdAndUnclassifiedAndType(groundId, true, "target")
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        List<String> children = unclassified.getChildrenId();

        // 목표 문서에 연결된 하위 문서 미분류로 분류
        for(String child : target.getChildrenId()){
            // 하위 문서에서 상위 문서 연결 변경
            Issue temp = issueRepository.findByGroundIdAndId(groundId, child)
                    .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

            // 미분류 문서에 연결
            temp.setParentId(unclassified.getId());
            issueRepository.save(temp);

            // 상위 문서에서 하위 문서 연결 추가
            children.add(temp.getId());
        }

        // 미분류 문서 childrenId 수정
        unclassified.setChildrenId(children);

        // 미분류 문서 저장
        issueRepository.save(unclassified);

        // 목표 문서 삭제
        issueRepository.deleteById(targetId);

        return TargetDto.Delete.Response.builder()
                .message(TargetMessage.delete())
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public TargetDto.Update.Response targetUpdate(TargetDto.Update.Request request, String targetId) {
        Issue target = issueRepository.findById(targetId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        target.setTitle(request.getTitle());
        target.setContent(request.getContent());
//        target.setUnclassified(true);

        issueRepository.save(target);

        return TargetDto.Update.Response.builder()
                .message(TargetMessage.update())
                .status(HttpStatus.OK.value())
                .target(target)
                .build();
    }

}