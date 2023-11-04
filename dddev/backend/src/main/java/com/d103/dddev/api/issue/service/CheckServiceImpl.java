package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.model.dto.CheckDto;
import com.d103.dddev.api.issue.model.dto.TargetDto;
import com.d103.dddev.api.issue.model.message.CheckMessage;
import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.repository.IssueRepository;
import com.d103.dddev.api.issue.util.IssueUtil;
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
public class CheckServiceImpl implements CheckService{

    private final IssueRepository issueRepository;
    private final IssueUtil issueUtil;

    @Override
    @Transactional
    public CheckDto.Create.Response createCheck(String groundId, CheckDto.Create.Request request) {
        Issue check = Issue.builder()
                .groundId(groundId)
                .parentId(issueUtil.unclassified(request.getParentId(),groundId, "target"))
                .childrenId(new ArrayList<>())
                .step(2)
                .type("check")
                .title("")
                .content("")
                .build(); // 체크포인트 문서 객체 생성

        Issue target = issueRepository.findById(issueUtil.unclassified(request.getParentId(),groundId, "target"))
                        .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 상위 문서 조회


        issueRepository.save(check); // 체크포인트 문서 저장

        target.getChildrenId().add(check.getId()); // 상위 문서에 체크포인트 문서 ID 추가

        issueRepository.save(target); // 상위문서 저장

        return CheckDto.Create.Response.builder()
                .message(CheckMessage.create())
                .status(HttpStatus.OK.value())
                .check(check)
                .build();
    }

    @Override
    public CheckDto.List.Response checkList(String groundId, String targetId) {
        ArrayList<Issue> checkList = issueRepository.findAllByGroundIdAndParentIdAndType(groundId,targetId,"check");

        if(checkList.isEmpty()){
            return CheckDto.List.Response.builder()
                    .message(CheckMessage.emptyList())
                    .status(HttpStatus.OK.value())
                    .checkList(checkList)
                    .build();
        }

        return CheckDto.List.Response.builder()
                .message(CheckMessage.list())
                .status(HttpStatus.OK.value())
                .checkList(checkList)
                .build();
    }



    @Override
    public CheckDto.Detail.Response checkDetail(String groundId, String checkId) {
        Issue check = issueRepository.findById(checkId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        if(check.getStep() != 2){
            throw new NoSuchElementException(Error.WrongStep());
        }
        return CheckDto.Detail.Response.builder()
                .message(CheckMessage.detail())
                .status(HttpStatus.OK.value())
                .check(check)
                .build();
    }

    // 삭제 시 연결된 이슈 미분류로 변경하는 로직 필요
    @Override
    @Transactional
    public CheckDto.Delete.Response checkDelete(String groundId, String checkId) {
        Issue check = issueRepository.findByGroundIdAndId(groundId, checkId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 체크 포인트 문서 조회

        Issue unclassifiedCheck = issueRepository.findByGroundIdAndUnclassifiedAndType(groundId, true, "check")
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 미분류 체크 포인트 문서 조회

        /*
         *
         *  하위 문서 목록 정리 (이슈 문서의 상위 문서 ID 수정 및 미분류 체크 포인트 문서에 하위 문서 목록 최신화)
         *
         * */

        List<String> unclassifiedChildren = unclassifiedCheck.getChildrenId(); // 미분류 문서 하위 문서 목록 조회
        List<String> checkChildren = check.getChildrenId();

        // 목표 문서에 연결된 하위 문서 미분류로 분류
        for(String child : checkChildren){
            Issue temp = issueRepository.findByGroundIdAndId(groundId, child)
                    .orElseThrow(() -> new NoSuchElementException(child + " " + Error.NoSubDocument())); // 하위 문서 DB 조회

            temp.setParentId(unclassifiedCheck.getId()); // 미분류 문서에 연결

            issueRepository.save(temp); // 상위 문서 변경하고 저장

            unclassifiedChildren.add(temp.getId()); // 상위 문서에서 하위 문서 연결 추가
        }

        unclassifiedCheck.setChildrenId(unclassifiedChildren); // 미분류 문서 childrenId 수정

        issueRepository.save(unclassifiedCheck); // 미분류 문서 저장

        /*
         *
         *  상위 문서 목록 정리 (상위 목표 문서의 하위 체크 포인트 문서 정리)
         *
         * */

        Issue target = issueRepository.findByGroundIdAndId(groundId, check.getParentId())
                .orElseThrow(() -> new NoSuchElementException(Error.NoParentDocument())); // 상위 목표 문서 조회

        List<String> targetChildren = target.getChildrenId(); // 하위 문서의 목록 조회

        targetChildren.remove(check.getId()); // 하위 문서 목록 중 삭제할 체크 포인트 문서 삭제

        target.setChildrenId(targetChildren); // 하위 체크 포인트 문서 목록 최신화

        issueRepository.save(target); // 상위 목표 문서 저장

        issueRepository.deleteById(checkId); // 체크 포인트 문서 삭제

        return CheckDto.Delete.Response.builder()
                .message(CheckMessage.delete())
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public CheckDto.Update.Response checkUpdate(CheckDto.Update.Request request, String checkId) {
        Issue check = issueRepository.findById(checkId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 저장 체크포인트 문서 조회

        // 필요시 현재 상태와 다른지 검증 코드 구현
        check.setTitle(request.getTitle());
        check.setContent(request.getContent());

        issueRepository.save(check);

        return CheckDto.Update.Response.builder()
                .message(CheckMessage.update())
                .status(HttpStatus.OK.value())
                .check(check)
                .build();
    }

    @Override
    @Transactional
    public CheckDto.Connect.Response connectTarget(CheckDto.Connect.Request request) {
        Issue newTarget = issueRepository.findById(request.getTargetId())
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 목표 문서 조회

        Issue check = issueRepository.findById(request.getCheckId())
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 체크포인트 문서 조회

        Issue oldTarget = issueRepository.findById(check.getParentId())
                        .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 이전 목표 문서 조회

        List<String> newChildren = newTarget.getChildrenId();
        List<String> oldChildren = oldTarget.getChildrenId();

        log.info(oldChildren.toString());
        log.info(newChildren.toString());

        if(oldChildren.contains(request.getCheckId())){
            oldChildren.remove(check.getId()); // 기존 연결된 문서에서 하위 문서 정보 삭제
        }else{
            return CheckDto.Connect.Response.builder()
                    .message(CheckMessage.notContains())
                    .status(HttpStatus.OK.value())
                    .check(check)
                    .build();
        }

        if(!newChildren.contains(request.getCheckId())){
            newChildren.add(request.getCheckId()); // 새로 연결할 문서에 추가
        }else{
            return CheckDto.Connect.Response.builder()
                    .message(CheckMessage.contains())
                    .status(HttpStatus.OK.value())
                    .check(check)
                    .build();
        }
        log.info(oldChildren.toString());
        log.info(newChildren.toString());

        oldTarget.setChildrenId(oldChildren); // 이전 연결 문서 정보 최신화
        newTarget.setChildrenId(newChildren); // 새로운 연결 문서 정보 최신화
        check.setParentId(newTarget.getId()); // 상위 문서 ID 변경

        issueRepository.save(oldTarget);
        issueRepository.save(newTarget);
        issueRepository.save(check);

        return CheckDto.Connect.Response.builder()
                .message(CheckMessage.connect())
                .status(HttpStatus.OK.value())
                .check(check)
                .build();
    }

}
