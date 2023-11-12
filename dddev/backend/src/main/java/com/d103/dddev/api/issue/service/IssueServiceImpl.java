package com.d103.dddev.api.issue.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.d103.dddev.api.file.service.DocumentService;

import org.checkerframework.checker.units.qual.A;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.model.dto.IssueDto;
import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.model.message.IssueMessage;
import com.d103.dddev.api.issue.repository.IssueRepository;
import com.d103.dddev.api.issue.util.IssueUtil;
import com.d103.dddev.api.sprint.repository.SprintRepository;
import com.d103.dddev.api.sprint.repository.entity.SprintEntity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class IssueServiceImpl implements IssueService {

	private final IssueRepository issueRepository;
	private final IssueUtil issueUtil;
	private final SprintRepository sprintRepository;

	/*
	 * groundId는 필수 값
	 * 스프린트 내에서 생성하는 경우에는 sprintId 같이 저장
	 * 상위 문서에서 생성하는 경우 parentId 같이 저장
	 * 상위 문서 내에서 생성하는 경우 상위 문서의 step + 1
	 * 별도로 step을 지정하는 경우는 그 값을 저장
	 *
	 *
	 */

	// 문서의 최초 생성 시 기본 구조대로 생성한다.
	// 그라운드ID는 필수로 받아야한다.

	// 공통 조건
	/*
	 * 1. 생성자, 수정자, 생성 시간, 수정 시간은 무조건 자동 생성
	 * 2. 연결 수행 시 무조건 상위 문서에 하위 문서의 ID를 추가
	 *  */

	@Override
	public IssueDto.Create.Response issueCreate(Integer groundId, IssueDto.Create.Request request,
		UserDetails userDetails) {
		Integer sprintId = request.getSprintId();
		if (sprintId != null && sprintId != 0) {
			SprintEntity sprint = sprintRepository.findById(sprintId)
				.orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));
			sprintId = sprint.getId();
		} else {
			sprintId = 0;
		}

		Issue issue = Issue.builder()
			.groundId(groundId)
			.parentId(issueUtil.unclassified(request.getParentId(), groundId, "check"))
			.sprintId(sprintId)
			.step(3) // 고정값
			.author(userDetails.getUsername())
			.focusTime(0) // 기본값
			.activeTime(0) // 기본값
			.type("issue")
			.status(0) // 0 : 미분류, 1 : 진행 예정, 2 : 진행 중, 3 : 완료
			.title("")
			.content("")
			.build();

		issueRepository.save(issue); // 이슈 저장

		Issue check = issueRepository.findById(issueUtil.unclassified(request.getParentId(), groundId, "check"))
			.orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 연결할 문서 조회

		List<String> children = check.getChildrenId(); // 하위 문서 조회

		children.add(issue.getId()); // 하위 문서 ID 추가

		check.setChildrenId(children); // 객체에 값 변경

		issueRepository.save(check); // DB 저장

		return IssueDto.Create.Response.builder()
			.message(IssueMessage.create())
			.code(HttpStatus.OK.value())
			.data(issue)
			.build();
	}

	@Override
	public IssueDto.List.Response issueList(Integer groundId, String checkId) {
		ArrayList<Issue> issueList = issueRepository.findAllByGroundIdAndParentIdAndType(groundId, checkId, "issue");

		if (issueList.isEmpty()) {
			return IssueDto.List.Response.builder()
				.message(IssueMessage.emptyList())
				.code(HttpStatus.OK.value())
				.data(issueList)
				.build();
		}


		ArrayList<Issue> result = new ArrayList<>();

		for(Issue issue : issueList){
			if(issue.getStatus() != 2){
				result.add(issue);
			}
		}

		return IssueDto.List.Response.builder()
			.message(IssueMessage.list())
			.code(HttpStatus.OK.value())
			.data(result)
			.build();
	}

	@Override
	public IssueDto.Detail.Response issueDetail(Integer groundId, String issueId) {
		Issue issue = issueRepository.findByGroundIdAndId(groundId, issueId)
			.orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

		if (issue.getStep() != 3) {
			throw new NoSuchElementException(Error.WrongStep());
		}

		return IssueDto.Detail.Response.builder()
			.message(IssueMessage.detail())
			.code(HttpStatus.OK.value())
			.data(issue)
			.build();
	}

	@Override
	public IssueDto.Delete.Response issueDelete(String issueId) {
		Issue issue = issueRepository.findById(issueId)
			.orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 이슈 정보 조회

		Issue check = issueRepository.findById(issue.getParentId())
			.orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 상위 체크 문서 조회

		List<String> children = check.getChildrenId(); // 하위 문서 목록

		children.remove(issue.getId()); // 삭제할 이슈 삭제

		check.setChildrenId(children); // 체크 포인트 문서 객체 수정

		issueRepository.deleteById(issueId); // 이슈 문서 삭제

		issueRepository.save(check); // 상위 체크 포인트 문서 최신화

		return IssueDto.Delete.Response.builder()
			.message(IssueMessage.delete())
			.code(HttpStatus.OK.value())
			.build();
	}

	@Override
	public IssueDto.Content.Response issueContent(IssueDto.Content.Request request, String issueId,
		UserDetails userDetails) {
		Issue issue = issueRepository.findById(issueId)
			.orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

		String title = request.getTitle();
		if(title != null){
			issue.setTitle(request.getTitle());
		}
		issue.setContent(request.getContent());
		issue.setModifier(userDetails.getUsername());

		issueRepository.save(issue);

		return IssueDto.Content.Response.builder()
			.message(IssueMessage.content())
			.code(HttpStatus.OK.value())
			.data(issue)
			.build();
	}

	@Override
	public IssueDto.Status.Response issueStatus(IssueDto.Status.Request request, String issueId,
		UserDetails userDetails) {
		Issue issue = issueRepository.findById(issueId)
			.orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

		// 진행 순방향
		if (issue.getStatus() == 0 && request.getStatus() == 1) {
			issue.setStartDate(LocalDateTime.now());
		}

		else if (issue.getStatus() == 1 && request.getStatus() == 2) {
			issue.setEndDate(LocalDateTime.now());
		}

		else if(issue.getStatus() == 0 && request.getStatus() == 2){
			issue.setStartDate(LocalDateTime.now());
			issue.setEndDate(LocalDateTime.now());
		}

		// 진행 역방향
		else if (issue.getStatus() > request.getStatus() && request.getStatus() == 0) {
			issue.setEndDate(null);
			issue.setStartDate(null);
		}

		else if (issue.getStatus() > request.getStatus() && request.getStatus() == 1) {
			issue.setEndDate(null);
		}

		issue.setModifier(userDetails.getUsername());
		issue.setStatus(request.getStatus());

		issueRepository.save(issue);

		return IssueDto.Status.Response.builder()
			.message(IssueMessage.status())
			.code(HttpStatus.OK.value())
			.data(issue)
			.build();
	}

	@Override
	public IssueDto.Connect.Response issueConnect(IssueDto.Connect.Request request, String issueId,
		UserDetails userDetails) {
		Issue issue = issueRepository.findById(issueId)
			.orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())); // 이슈 조회

		Issue oldCheck = issueRepository.findById(issue.getParentId())
			.orElseThrow(() -> new NoSuchElementException(Error.NoParentDocument())); // 연결 된 체크 포인트 문서 조회

		Issue newCheck = issueRepository.findById(request.getParentId())
			.orElseThrow(() -> new NoSuchElementException(Error.NoParentDocument())); // 연결할 체크 포인트 문서 조회

		List<String> oldChildren = oldCheck.getChildrenId(); // 기존 연결 된 문서
		List<String> newChildren = newCheck.getChildrenId(); // 연결 할 문서의 하단

		// 최신화
		oldChildren.remove(issue.getId());
		newChildren.add(issue.getId());
		issue.setParentId(newCheck.getId());

		oldCheck.setChildrenId(oldChildren);
		newCheck.setChildrenId(newChildren);

		// 수정자 수정
		oldCheck.setModifier(userDetails.getUsername());
		newCheck.setModifier(userDetails.getUsername());
		issue.setModifier(userDetails.getUsername());

		// 저장
		issueRepository.save(oldCheck);
		issueRepository.save(newCheck);
		issueRepository.save(issue);

		return IssueDto.Connect.Response.builder()
			.message(IssueMessage.connect())
			.code(HttpStatus.OK.value())
			.data(issue)
			.build();
	}

	@Override
	public IssueDto.Time.Response issueTime(IssueDto.Time.Request request, String issueId, UserDetails userDetails) {
		Issue issue = issueRepository.findById(issueId)
			.orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

		issue.setFocusTime(request.getFocusTime());
		issue.setActiveTime(request.getActiveTime());
		issue.setModifier(userDetails.getUsername());

		issueRepository.save(issue);

		return IssueDto.Time.Response.builder()
			.message(IssueMessage.time())
			.code(HttpStatus.OK.value())
			.data(issue)
			.build();
	}

	@Override
	public IssueDto.Sprint.Response issueSprint(IssueDto.Sprint.Request request, String issueId,
		UserDetails userDetails) {
		Issue issue = issueRepository.findById(issueId)
			.orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

		issue.setSprintId(request.getSprintId());
		issue.setModifier(userDetails.getUsername());

		issueRepository.save(issue);

		return IssueDto.Sprint.Response.builder()
			.message(IssueMessage.sprint())
			.code(HttpStatus.OK.value())
			.data(issue)
			.build();
	}

	@Override
	public Integer getSprintTotalFocusTime(Integer sprintId) throws Exception {
		List<Issue> totalFocusTimeList = issueRepository.getSprintIssueList(sprintId);
		Integer totalTime = 0;
		for(Issue i : totalFocusTimeList) {
			totalTime += i.getFocusTime();
		}
		return totalTime;
	}

	@Override
	public List<Issue> getSprintFocusIssueDoneAsc(Integer sprintId) throws Exception {
		Sort sort = Sort.by(Sort.Direction.ASC, "endDate");
		return issueRepository.findFocusTimeDone(sprintId, sort);
	}

	@Override
	public Map<String, Integer> getSprintFocusTime(Integer sprintId) throws Exception {
		List<Issue> focusTimeDoneList = issueRepository.findFocusTimeDone(sprintId);
		List<Issue> focusTimeUndoneList = issueRepository.findFocusTimeUndone(sprintId);

		Integer focusTimeDone = 0;
		for (Issue i : focusTimeDoneList) {
			focusTimeDone += i.getFocusTime();
		}

		Integer focusTimeUndone = 0;
		for (Issue i : focusTimeUndoneList) {
			focusTimeUndone += i.getFocusTime();
		}

		Map<String, Integer> result = new HashMap<>();
		result.put("done", focusTimeDone);
		result.put("undone", focusTimeUndone);

		return result;
	}

	@Override
	public Map<String, Integer> getSprintActiveTime(Integer sprintId) throws Exception {
		List<Issue> activeTimeDoneList = issueRepository.findActiveTimeDone(sprintId);
		List<Issue> activeTimeUndoneList = issueRepository.findActiveTimeUndone(sprintId);

		Integer activeTimeDone = 0;
		for (Issue i : activeTimeDoneList) {
			activeTimeDone += i.getActiveTime();
		}

		Integer activeTimeUndone = 0;
		for (Issue i : activeTimeUndoneList) {
			activeTimeUndone += i.getActiveTime();
		}

		Map<String, Integer> result = new HashMap<>();
		result.put("done", activeTimeDone);
		result.put("undone", activeTimeUndone);

		return result;
	}

	@Override
	public Map<String, Integer> getSprintTotalTime(Integer sprintId) throws Exception {
		Map<String, Integer> groundFocusTime = getSprintFocusTime(sprintId);
		Map<String, Integer> groundActiveTime = getSprintActiveTime(sprintId);
		Map<String, Integer> result = new HashMap<>();
		result.put("done", groundFocusTime.get("done") + groundActiveTime.get("done"));
		result.put("undone", groundFocusTime.get("undone") + groundActiveTime.get("undone"));
		return result;
	}

	@Override
	public Map<String, Integer> getSprintFocusTimeCount(Integer sprintId) throws Exception {
		Integer focusTimeDoneCount = issueRepository.findFocusTimeDoneCount(sprintId);
		Integer focusTimeUndoneCount = issueRepository.findFocusTimeUndoneCount(sprintId);

		Map<String, Integer> result = new HashMap<>();
		result.put("done", focusTimeDoneCount);
		result.put("undone", focusTimeUndoneCount);
		return result;
	}

	@Override
	public Map<String, Integer> getSprintActiveTimeCount(Integer sprintId) throws Exception {
		Integer focusTimeDoneCount = issueRepository.findActiveTimeDoneCount(sprintId);
		Integer focusTimeUndoneCount = issueRepository.findActiveTimeUndoneCount(sprintId);

		Map<String, Integer> result = new HashMap<>();
		result.put("done", focusTimeDoneCount);
		result.put("undone", focusTimeUndoneCount);
		return result;
	}

	@Override
	public Map<String, Integer> getSprintTotalTimeCount(Integer sprintId) throws Exception {
		Map<String, Integer> groundFocusTimeCount = getSprintFocusTimeCount(sprintId);
		Map<String, Integer> groundActiveTimeCount = getSprintActiveTimeCount(sprintId);
		Map<String, Integer> groundTotalTimeCount = new HashMap<>();
		groundTotalTimeCount.put("done", groundFocusTimeCount.get("done") + groundActiveTimeCount.get("done"));
		groundFocusTimeCount.put("undone", groundFocusTimeCount.get("undone") + groundActiveTimeCount.get("undone"));

		return groundTotalTimeCount;
	}

    @Override
    public IssueDto.Title.Response issueTitle(IssueDto.Title.Request request, String issueId, UserDetails userDetails) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException()));

        issue.setTitle(request.getTitle());
        issue.setModifier(userDetails.getUsername());

        issueRepository.save(issue);

        return IssueDto.Title.Response.builder()
                .message(IssueMessage.title())
                .code(HttpStatus.OK.value())
                .data(issue)
                .build();
    }

	@Override
	public IssueDto.List.Response issueTotalList(Integer groundId, String checkId) {
		ArrayList<Issue> issueList = issueRepository.findAllByGroundIdAndParentIdAndType(groundId, checkId, "issue");

		if (issueList.isEmpty()) {
			return IssueDto.List.Response.builder()
					.message(IssueMessage.emptyList())
					.code(HttpStatus.OK.value())
					.data(issueList)
					.build();
		}


		return IssueDto.List.Response.builder()
				.message(IssueMessage.list())
				.code(HttpStatus.OK.value())
				.data(issueList)
				.build();
	}

}