package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.file.service.DocumentService;
import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.model.dto.CheckDto;
import com.d103.dddev.api.issue.model.dto.IssueDto;
import com.d103.dddev.api.issue.model.message.CheckMessage;
import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.model.message.IssueMessage;
import com.d103.dddev.api.issue.repository.IssueRepository;
import com.d103.dddev.api.issue.util.IssueUtil;
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
public class CheckServiceImpl implements CheckService{

    private final IssueRepository issueRepository;
    private final IssueUtil issueUtil;
    private final DocumentService documentService;

    @Override
    @Transactional
    public CheckDto.Create.Response createCheck(Integer groundId, CheckDto.Create.Request request, UserDetails userDetails) {
        Issue target = issueRepository.findById(issueUtil.unclassified(request.getParentId(),groundId, "target"))
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 상위 문서 조회

        Issue check = Issue.builder()
                    .groundId(groundId)
                    .parentId(issueUtil.unclassified(request.getParentId(),groundId, "target"))
                    .childrenId(new ArrayList<>())
                    .author(userDetails.getUsername())
                    .step(2)
                    .type("check")
                    .title("")
                    .content(target.isTemplate() ? target.getContent() : "")
                    .build(); // 체크포인트 문서 객체 생성

        issueRepository.save(check); // 체크포인트 문서 저장

        target.getChildrenId().add(check.getId()); // 상위 문서에 체크포인트 문서 ID 추가

        issueRepository.save(target); // 상위문서 저장

        return CheckDto.Create.Response.builder()
                .message(CheckMessage.create())
                .code(HttpStatus.OK.value())
                .data(check)
                .build();
    }

    @Override
    public CheckDto.List.Response checkList(Integer groundId, String targetId) {
        ArrayList<Issue> checkList = issueRepository.findAllByGroundIdAndParentIdAndType(groundId,targetId,"check");

        if(checkList.isEmpty()){
            return CheckDto.List.Response.builder()
                    .message(CheckMessage.emptyList())
                    .code(HttpStatus.OK.value())
                    .data(checkList)
                    .build();
        }

        return CheckDto.List.Response.builder()
                .message(CheckMessage.list())
                .code(HttpStatus.OK.value())
                .data(checkList)
                .build();
    }



    @Override
    public CheckDto.Detail.Response checkDetail(Integer groundId, String checkId) {
        Issue check = issueRepository.findById(checkId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        if(check.getStep() != 2){
            throw new NoSuchElementException(Error.WrongStep());
        }
        return CheckDto.Detail.Response.builder()
                .message(CheckMessage.detail())
                .code(HttpStatus.OK.value())
                .data(check)
                .build();
    }

    // 삭제 시 연결된 이슈 미분류로 변경하는 로직 필요
    @Override
    @Transactional
    public CheckDto.Delete.Response checkDelete(Integer groundId, String checkId) {
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

        documentService.deleteFile(checkId);

        return CheckDto.Delete.Response.builder()
                .message(CheckMessage.delete())
                .code(HttpStatus.OK.value())
                .build();
    }

    @Override
    public CheckDto.Update.Response checkUpdate(CheckDto.Update.Request request, String checkId, UserDetails userDetails) {
        Issue check = issueRepository.findById(checkId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 저장 체크포인트 문서 조회

        // 필요시 현재 상태와 다른지 검증 코드 구현
        String title = request.getTitle();
        if(title != null){
            check.setTitle(request.getTitle());
        }
        check.setContent(request.getContent());
        check.setModifier(userDetails.getUsername());

        issueRepository.save(check);

        return CheckDto.Update.Response.builder()
                .message(CheckMessage.update())
                .code(HttpStatus.OK.value())
                .data(check)
                .build();
    }

    @Override
    @Transactional
    public CheckDto.Connect.Response connectTarget(CheckDto.Connect.Request request, UserDetails userDetails, String checkId) {
        Issue newTarget = issueRepository.findById(request.getParentId())
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 목표 문서 조회

        Issue check = issueRepository.findById(checkId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 체크포인트 문서 조회

        Issue oldTarget = issueRepository.findById(check.getParentId())
                        .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 이전 목표 문서 조회

        List<String> newChildren = newTarget.getChildrenId();
        List<String> oldChildren = oldTarget.getChildrenId();

        log.info(oldChildren.toString());
        log.info(newChildren.toString());

        if(oldChildren.contains(checkId)){
            oldChildren.remove(check.getId()); // 기존 연결된 문서에서 하위 문서 정보 삭제
        }else{
            return CheckDto.Connect.Response.builder()
                    .message(CheckMessage.notContains())
                    .code(HttpStatus.OK.value())
                    .data(check)
                    .build();
        }

        if(!newChildren.contains(checkId)){
            newChildren.add(checkId); // 새로 연결할 문서에 추가
        }else{
            return CheckDto.Connect.Response.builder()
                    .message(CheckMessage.contains())
                    .code(HttpStatus.OK.value())
                    .data(check)
                    .build();
        }
        log.info(oldChildren.toString());
        log.info(newChildren.toString());

        oldTarget.setChildrenId(oldChildren); // 이전 연결 문서 정보 최신화
        newTarget.setChildrenId(newChildren); // 새로운 연결 문서 정보 최신화
        check.setParentId(newTarget.getId()); // 상위 문서 ID 변경

        oldTarget.setModifier(userDetails.getUsername());
        newTarget.setModifier(userDetails.getUsername());
        check.setModifier(userDetails.getUsername());

        issueRepository.save(oldTarget);
        issueRepository.save(newTarget);
        issueRepository.save(check);

        return CheckDto.Connect.Response.builder()
                .message(CheckMessage.connect())
                .code(HttpStatus.OK.value())
                .data(check)
                .build();
    }

    @Override
    public CheckDto.Title.Response checkTitle(CheckDto.Title.Request request, String checkId, UserDetails userDetails) {
        Issue check = issueRepository.findById(checkId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        check.setTitle(request.getTitle());
        check.setModifier(userDetails.getUsername());

        issueRepository.save(check);

        return CheckDto.Title.Response.builder()
                .message(CheckMessage.title())
                .code(HttpStatus.OK.value())
                .data(check)
                .build();
    }

    @Override
    public CheckDto.List.Response checkTotalList(Integer groundId) {
        ArrayList<Issue> checkList = issueRepository.findAllByGroundIdAndType(groundId,"check");

        if(checkList.isEmpty()){
            return CheckDto.List.Response.builder()
                    .message(CheckMessage.emptyList())
                    .code(HttpStatus.OK.value())
                    .data(checkList)
                    .build();
        }

        return CheckDto.List.Response.builder()
                .message(CheckMessage.list())
                .code(HttpStatus.OK.value())
                .data(checkList)
                .build();
    }

    @Override
    public CheckDto.Template.Response isTemplate(String checkId) {
        Issue check = issueRepository.findById(checkId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        if(check.isTemplate()){
            check.setTemplate(false);
        }else{
            check.setTemplate(true);
        }

        issueRepository.save(check);
        return CheckDto.Template.Response
                .builder()
                .message(CheckMessage.template())
                .code(HttpStatus.OK.value())
                .isTemplate(check.isTemplate())
                .build();
    }

}
