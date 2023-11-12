package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.file.service.DocumentService;
import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.model.dto.IssueDto;
import com.d103.dddev.api.issue.model.dto.TargetDto;
import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.model.message.IssueMessage;
import com.d103.dddev.api.issue.model.message.TargetMessage;
import com.d103.dddev.api.issue.repository.IssueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final DocumentService documentService;

    @Override
    public TargetDto.Create.Response createTarget(Integer groundId, UserDetails userDetails) {

        Issue target = Issue.builder()
                .groundId(groundId)
                .childrenId(new ArrayList<>())
                .author(userDetails.getUsername())
                .step(1) // 최상단 문서의 단계는 1
                .type("target")
                .title("")
                .content("")
                .build();

        issueRepository.save(target);

        return TargetDto.Create.Response.builder()
                .message(TargetMessage.create())
                .code(HttpStatus.OK.value())
                .data(target)
                .build();
    }

    // 그라운드에 해당하는 목표 문서 리스트 조회

    @Override
    public TargetDto.List.Response targetList(Integer groundId) {
        ArrayList<Issue> targetList = issueRepository.findAllByGroundIdAndType(groundId,"target"); // 입력한 그라운드에 해당하는 target만

        if(targetList.isEmpty()){
            return TargetDto.List.Response.builder()
                    .message(TargetMessage.emptyList())
                    .code(HttpStatus.OK.value())
                    .data(targetList)
                    .build();
        }

        return TargetDto.List.Response.builder()
                .message(TargetMessage.list())
                .code(HttpStatus.OK.value())
                .data(targetList)
                .build();
    }

    @Override
    public TargetDto.Detail.Response targetDetail(Integer groundId, String targetId) {
        // targetId만 있어도 조회는 가능 하지만 조금 더 확실히 하기 위해 두개를 비교
        Issue target = issueRepository.findByGroundIdAndId(groundId, targetId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        return TargetDto.Detail.Response.builder()
                .message(TargetMessage.detail())
                .code(HttpStatus.OK.value())
                .data(target)
                .build();
    }

    @Override
    @Transactional
    public TargetDto.Delete.Response targetDelete(Integer groundId, String targetId) {
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

        documentService.deleteFile(targetId);

        return TargetDto.Delete.Response.builder()
                .message(TargetMessage.delete())
                .code(HttpStatus.OK.value())
                .build();
    }

    @Override
    public TargetDto.Update.Response targetUpdate(TargetDto.Update.Request request, String targetId, UserDetails userDetails) {
        Issue target = issueRepository.findById(targetId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        String title = request.getTitle();
        if(title != null){
            target.setTitle(request.getTitle());
        }
        target.setContent(request.getContent());
        target.setModifier(userDetails.getUsername());

//        target.setUnclassified(true);

        issueRepository.save(target);

        return TargetDto.Update.Response.builder()
                .message(TargetMessage.update())
                .code(HttpStatus.OK.value())
                .data(target)
                .build();
    }

    @Override
    public TargetDto.Tree.Response Tree(Integer groundId) {
        // 목표의 내용을 조회
        List<Issue> targetList = issueRepository.findAllByGroundIdAndType(groundId, "target");

        List<TargetDto.Tree.Docs> docs = new ArrayList<>();

        for(Issue target : targetList){
            // 목표 문서 하나에 대한 트리
            List<TargetDto.Tree.Docs> checkTree = new ArrayList<>();

            // check의 하위 문서 조회
            for(String childCheck : target.getChildrenId()){
                Issue checkObject = issueRepository.findById(childCheck)
                        .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

                List<TargetDto.Tree.Docs> issueTree = new ArrayList<>();

                for(String childIssue : checkObject.getChildrenId()){
                    Issue issueObject = issueRepository.findById(childIssue)
                            .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

                    issueTree.add(TargetDto.Tree.Docs.builder()
                                    .id(issueObject.getId())
                                    .title(issueObject.getTitle())
                                    .step(issueObject.getStep())
                                    .sprintId(issueObject.getSprintId())
                                    .build());
                }

                TargetDto.Tree.Docs check = TargetDto.Tree.Docs.builder()
                        .id(checkObject.getId())
                        .title(checkObject.getTitle())
                        .step(checkObject.getStep())
                        .children(issueTree)
                        .build();

                checkTree.add(check);
            }


            docs.add(TargetDto.Tree.Docs.builder()
                    .id(target.getId())
                    .title(target.getTitle())
                    .step(target.getStep())
                    .children(checkTree)
                    .build());
        }

        return TargetDto.Tree.Response.builder()
                .message(TargetMessage.tree())
                .code(HttpStatus.OK.value())
                .data(docs)
                .build();
    }

    @Override
    public TargetDto.Title.Response targetTitle(TargetDto.Title.Request request, String targetId, UserDetails userDetails) {
        Issue target = issueRepository.findById(targetId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        target.setTitle(request.getTitle());
        target.setModifier(userDetails.getUsername());

        issueRepository.save(target);

        return TargetDto.Title.Response.builder()
                .message(TargetMessage.title())
                .code(HttpStatus.OK.value())
                .data(target)
                .build();
    }

    @Override
    public TargetDto.Template.Response isTemplate(String targetId) {
        Issue target = issueRepository.findById(targetId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        if(target.isTemplate()){
            target.setTemplate(false);
        }else{
            target.setTemplate(true);
        }

        issueRepository.save(target);

        return TargetDto.Template.Response.builder()
                .message(TargetMessage.template())
                .code(200)
                .isTemplate(target.isTemplate())
                .build();
    }


}