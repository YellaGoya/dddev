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

import java.util.ArrayList;
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

    @Override
    public TargetDto.List.Response targetList(String groundId) {
        ArrayList<Issue> targetList = issueRepository.findAllByGroundId(groundId);

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
        Issue target = issueRepository.findByGroundIdAndId(groundId, targetId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        return TargetDto.Detail.Response.builder()
                .message(TargetMessage.detail())
                .status(HttpStatus.OK.value())
                .target(target)
                .build();
    }

    @Override
    public TargetDto.Delete.Response targetDelete(String targetId) {
        issueRepository.findById(targetId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

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

        issueRepository.save(target);

        return TargetDto.Update.Response.builder()
                .message(TargetMessage.update())
                .status(HttpStatus.OK.value())
                .target(target)
                .build();
    }

}