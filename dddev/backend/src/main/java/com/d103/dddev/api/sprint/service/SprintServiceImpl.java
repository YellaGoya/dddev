package com.d103.dddev.api.sprint.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;

import org.hibernate.TransactionException;
import org.springframework.stereotype.Service;

import com.d103.dddev.api.ground.repository.GroundRepository;
import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.service.IssueService;
import com.d103.dddev.api.sprint.TimeType;
import com.d103.dddev.api.sprint.repository.BurnDownRepository;
import com.d103.dddev.api.ground.repository.ChartDataRepository;
import com.d103.dddev.api.sprint.repository.SprintRepository;
import com.d103.dddev.api.sprint.repository.dto.requestDto.SprintUpdateDto;
import com.d103.dddev.api.sprint.repository.dto.responseDto.SprintResponseDto;
import com.d103.dddev.api.sprint.repository.entity.BurnDown;
import com.d103.dddev.api.ground.repository.entity.ChartData;
import com.d103.dddev.api.sprint.repository.entity.SprintEntity;
import com.d103.dddev.common.exception.sprint.SprintNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SprintServiceImpl implements SprintService {
	private final SprintRepository sprintRepository;
	private final GroundRepository groundRepository;
	private final BurnDownRepository burnDownRepository;
	private final ChartDataRepository chartDataRepository;
	private final IssueService issueService;

	private final Integer OPEN = 1;
	private final Integer CLOSE = 3;

	private final String FOCUS = "focus";
	private final String ACTIVE = "active";

	/**
	 * 스프린트를 생성하는 함수이다.<br/>
	 * name은 자동으로 ground.name + ?주차<br/>
	 *
	 * <p></p>startDate는 생성한 날짜 기준 월요일<br/>
	 * endDate는 생성한 날짜 기준 금요일<br/>
	 * 주말에 생성하면 다음주 월요일, 금요일 기준<br/></p>
	 * @param groundId 그라운드 Id
	 * @return {@link SprintEntity}
	 * @author KG
	 * @throws NoSuchElementException 그라운드가 존재하지 않을때
	 * @throws TransactionException 스프린트 저장에 실패 했을때
	 */
	@Override
	public SprintResponseDto createSprint(int groundId) {
		Ground ground = groundRepository.findById(groundId)
			.orElseThrow(() -> new NoSuchElementException("getGroundInfo :: 존재하지 않는 그라운드입니다."));

		LocalDate start, end;
		LocalDate now = LocalDate.now();
		DayOfWeek dayOfWeek = now.getDayOfWeek();
		int day = dayOfWeek.getValue();
		// 월요일부터 금요일
		if (day >= 1 && day <= 5) {
			while (now.getDayOfWeek().getValue() != 1) {
				now = now.minusDays(1);
			}
			start = LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
			while (now.getDayOfWeek().getValue() != 5) {
				now = now.plusDays(1);
			}
			end = LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
		}
		// 토요일 일요일
		else {
			while (now.getDayOfWeek().getValue() != 1) {
				now = now.plusDays(1);
			}
			start = LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
			while (now.getDayOfWeek().getValue() != 5) {
				now = now.plusDays(1);
			}
			end = LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
		}

		int week = start.get(WeekFields.of(Locale.KOREA).weekOfMonth());

		SprintEntity sprint = SprintEntity.builder()
			.name(ground.getName() + " " + week + "주차")
			.status(0)
			.startDate(start)
			.endDate(end)
			.ground(ground)
			.build();
		try {
			sprintRepository.save(sprint);
		} catch (Exception e) {
			throw new TransactionException("스프린트 저장에 실패했습니다.");
		}

		return convertToSprintResponseDto(sprint);
	}

	@Override
	public List<SprintResponseDto> loadSprintList(int groundId) {
		List<SprintEntity> sprintEntities = sprintRepository.findByGround_Id(groundId)
			.orElseThrow(() -> new TransactionException("스프린트 목록을 불러오는데 실패했습니다."));
		List<SprintResponseDto> sprintResponseDtoList = new ArrayList<>();
		for (SprintEntity entity : sprintEntities) {
			SprintResponseDto sprintResponseDto = convertToSprintResponseDto(entity);
			sprintResponseDtoList.add(sprintResponseDto);
		}
		return sprintResponseDtoList;
	}

	@Override
	public SprintResponseDto loadSprint(int sprintId) throws Exception {
		SprintEntity sprintEntity = sprintRepository.findById(sprintId)
			.orElseThrow(() -> new SprintNotFoundException("존재하지 않는 스프린트입니다."));
		return convertToSprintResponseDto(sprintEntity);
	}

	@Override
	public List<SprintResponseDto> loadRecentSprint(int sprintId) throws Exception {
		List<SprintEntity> sprintEntityList = sprintRepository.findByGround_IdOrderByIdDesc(sprintId)
			.orElseThrow(() -> new TransactionException("스프린트들을 불러오는데 실패했습니다."));
		if (sprintEntityList.isEmpty())
			return null;
		List<SprintResponseDto> sprintResponseDtoList = new ArrayList<>();
		SprintEntity recentSprint = sprintEntityList.get(0);
		sprintResponseDtoList.add(convertToSprintResponseDto(recentSprint));
		return sprintResponseDtoList;
	}

	@Override
	public void deleteSprint(int sprintId) throws Exception {

		issueService.changeIssuesStatusWhenSprintDelete(sprintId);

		try {
			sprintRepository.deleteById(sprintId);
		} catch (Exception e) {
			throw new TransactionException("스프린트 삭제에 실패 했습니다.");
		}
	}

	@Override
	public SprintResponseDto updateSprint(int sprintId, SprintUpdateDto sprintUpdateDto) throws Exception {
		SprintEntity loadSprint = sprintRepository.findById(sprintId)
			.orElseThrow(() -> new SprintNotFoundException("존재하지 않는 스프린트입니다."));
		String name = sprintUpdateDto.getName();
		String goal = sprintUpdateDto.getGoal();
		if (name == null && goal == null)
			return convertToSprintResponseDto(loadSprint);
		if (name != null) {
			loadSprint.setName(sprintUpdateDto.getName());
		}
		if (goal != null) {
			loadSprint.setGoal(sprintUpdateDto.getGoal());
		}
		try {
			sprintRepository.save(loadSprint);
		} catch (Exception e) {
			throw new TransactionException("스프린트 저장에 실패했습니다.");
		}
		return convertToSprintResponseDto(loadSprint);
	}

	@Override
	public void startSprint(int groundId, int sprintId) throws Exception {
		Optional<SprintEntity> proceedSprintOptional = sprintRepository.findByGround_IdAndStatus(groundId, 1);
		if (proceedSprintOptional.isPresent())
			throw new IllegalAccessException("이미 진행중인 스프린트가 있습니다.");
		SprintEntity sprint = sprintRepository.findById(sprintId)
			.orElseThrow(() -> new SprintNotFoundException("존재하지 않는 스프린트입니다."));
		sprint.setStatus(1);

		// 스프린트에 올라간 이슈들의 집중시간 총 합을 불러온다
		Integer totalFocusTime = 0;
		try {
			totalFocusTime = issueService.getSprintTotalFocusTime(sprintId);
		} catch (Exception e) {
			throw new TransactionException("startSprint :: 스프린트 전체 집중시간 계산에 실패했습니다.");
		}

		sprint.setTotalFocusTime(totalFocusTime);

		// 스프린트에 올라간 이슈들의 총 시간 합을 불러온다
		Integer totalTime = totalFocusTime;
		try {
			totalTime += issueService.getSprintTotalActiveTime(sprintId);
		} catch (Exception e) {
			throw new TransactionException("startSprint :: 스프린트 전체 시간 계산에 실패했습니다");
		}

		sprint.setTotalTime(totalTime);
		try {
			sprintRepository.save(sprint);
		} catch (Exception e) {
			throw new TransactionException("스프린트 저장에 실패했습니다.");
		}
	}

	@Override
	public void completeSprint(int sprintId) throws Exception {
		SprintEntity sprint = sprintRepository.findById(sprintId)
			.orElseThrow(() -> new SprintNotFoundException("존재하지 않는 스프린트입니다."));
		sprint.setStatus(2);

		Ground ground = sprint.getGround();

		// 번다운 차트 데이터 db에 저장하기
		try {
			// 완료된 이슈 리스트 (종료된 시간 순서대로) 불러오기
			List<Issue> issueDone = issueService.getSprintFocusIssueDoneAsc(sprint.getId());

			// 초기 시간 입력
			burnDownRepository.save(BurnDown.builder()
				.ground(ground)
				.sprint(sprint)
				.endDate(sprint.getStartDate())
				.time(sprint.getTotalFocusTime())
				.build());

			// 차트 db에 저장
			for (Issue i : issueDone) {
				burnDownRepository.save(BurnDown.builder()
					.ground(ground)
					.sprint(sprint)
					.endDate(i.getEndDate().toLocalDate())
					.time(i.getFocusTime())
					.build());
			}

		} catch (Exception e) {
			throw new TransactionException("completeSprint :: 번다운차트 저장 실패");
		}

		// 이 외 차트 데이터 db에 저장하기
		try {
			// 완료/미완료된 집중시간 합
			Map<String, Integer> sprintFocusTime = issueService.getSprintFocusTime(sprintId);

			// 완료/미완료된 연구시간 합
			Map<String, Integer> sprintActiveTime = issueService.getSprintActiveTime(sprintId);


			// 완료 집중시간 차트 데이터
			makeChartData(ground, sprint, TimeType.FOCUS, sprintFocusTime.get("done"));

			// 완료 연구시간 차트 데이터
			makeChartData(ground, sprint, TimeType.ACTIVE, sprintActiveTime.get("done"));


		} catch (Exception e) {
			throw new TransactionException("completeSprint :: 차트 데이터 저장 실패");
		}

		// 이슈 상태 변경하기
		issueService.changeIssuesStatusWhenSprintComplete(sprintId);

		try {
			sprintRepository.save(sprint);
		} catch (Exception e) {
			throw new TransactionException("스프린트 저장에 실패했습니다.");
		}
	}

	private void makeChartData(Ground ground, SprintEntity sprint, TimeType timeType, Integer time) {
		ChartData chartData = ChartData.builder()
			.ground(ground)
			.sprint(sprint)
			.timeType(timeType)
			.time(time)
			.build();

		chartDataRepository.saveAndFlush(chartData);
	}

	@Override
	public void deleteAllSprintWhenGroundDelete(int groundId) throws Exception {
		try {
			sprintRepository.deleteByGround_Id(groundId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new TransactionException("스프린트들을 삭제하는데 실패했습니다.");
		}
	}

	@Override
	public Map<String, Integer> getSprintFocusTime(Integer sprintId) throws
		Exception {
		return issueService.getSprintFocusTime(sprintId);
	}

	@Override
	public Map<String, Integer> getSprintActiveTime(Integer sprintId) throws
		Exception {
		return issueService.getSprintActiveTime(sprintId);
	}

	@Override
	public Map<String, Integer> getSprintTotalTime(Integer sprintId) throws Exception {
		return issueService.getSprintTotalTime(sprintId);
	}

	@Override
	public Map<String, Integer> getSprintFocusTimeCount(Integer sprintId) throws Exception {
		return issueService.getSprintFocusTimeCount(sprintId);
	}

	@Override
	public Map<String, Integer> getSprintActiveTimeCount(Integer sprintId) throws Exception {
		return issueService.getSprintActiveTimeCount(sprintId);
	}

	@Override
	public Map<String, Integer> getSprintTotalTimeCount(Integer sprintId) throws Exception {
		return issueService.getSprintTotalTimeCount(sprintId);
	}

	@Override
	public Map<LocalDate, Integer> getSprintBurnDownChart(Integer sprintId) throws Exception {

		SprintEntity sprintEntity = sprintRepository.findById(sprintId)
			.orElseThrow(() -> new SprintNotFoundException("존재하지 않는 스프린트입니다."));

		Map<LocalDate, Integer> burnDown = new TreeMap<>();

		if (sprintEntity.getStatus() == OPEN) {        // 진행중
			burnDown = makeBurnDownChart(sprintEntity);
		} else if (sprintEntity.getStatus() == CLOSE) {    // 종료된 스프린트
			List<BurnDown> burnDownList = burnDownRepository.findBySprint_Id(sprintId);
			burnDown = makeBurnDownChart(burnDownList);
		}

		return burnDown;
	}

	private Map<LocalDate, Integer> makeBurnDownChart(SprintEntity sprint) throws Exception {
		Map<LocalDate, Integer> burnDown = new TreeMap<>();

		// 시작점
		LocalDate startDate = LocalDate.of(1970, 1, 1);
		Integer totalFocusTime = sprint.getTotalFocusTime();
		burnDown.put(startDate, totalFocusTime);

		// 완료된 이슈 리스트 (종료된 시간 순서대로) 불러오기
		List<Issue> issueDone = issueService.getSprintFocusIssueDoneAsc(sprint.getId());

		// 완료된 이슈 데이터 추가
		LocalDate key = null;
		Integer time = 0;
		for (Issue i : issueDone) {
			System.out.println(i.getEndDate().toLocalDate() + " " + i.getFocusTime());
			if (key == null) {
				key = i.getEndDate().toLocalDate();
				time = i.getFocusTime();
			} else if (key.equals(i.getEndDate().toLocalDate())) {
				time += i.getFocusTime();
			} else {
				burnDown.put(key, time);
				key = i.getEndDate().toLocalDate();
				time = i.getFocusTime();
			}
		}
		burnDown.put(key, time);
		return burnDown;
	}

	private Map<LocalDate, Integer> makeBurnDownChart(List<BurnDown> burnDownList) throws Exception {
		Map<LocalDate, Integer> burnDown = new TreeMap<>();
		LocalDate key = null;
		Integer time = 0;
		for (BurnDown b : burnDownList) {
			if (key == null) {
				key = b.getEndDate();
				time = b.getTime();
			} else if (key.equals(b.getEndDate())) {
				time += b.getTime();
			} else {
				burnDown.put(key, time);
				key = b.getEndDate();
				time = b.getTime();
			}
		}
		burnDown.put(key, time);
		return burnDown;
	}

	private SprintResponseDto convertToSprintResponseDto(SprintEntity sprintEntity) {
		SprintResponseDto sprintResponseDto = new SprintResponseDto();
		sprintResponseDto.setId(sprintEntity.getId());
		sprintResponseDto.setName(sprintEntity.getName());
		sprintResponseDto.setGoal(sprintEntity.getGoal());
		sprintResponseDto.setStatus(sprintEntity.getStatus());
		sprintResponseDto.setStartDate(sprintEntity.getStartDate());
		sprintResponseDto.setEndDate(sprintEntity.getEndDate());
		sprintResponseDto.setTotalFocusTime(sprintEntity.getTotalFocusTime());
		return sprintResponseDto;
	}

}
