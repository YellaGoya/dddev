package com.d103.dddev.api.sprint.service;

import com.d103.dddev.api.ground.repository.GroundRepository;
import com.d103.dddev.api.ground.repository.entity.Ground;
import com.d103.dddev.api.sprint.repository.dto.SprintUpdateDto;
import com.d103.dddev.api.sprint.repository.entity.SprintEntity;
import com.d103.dddev.api.sprint.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.TransactionException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SprintServiceImpl implements SprintService{
    private final SprintRepository sprintRepository;
    private final GroundRepository groundRepository;

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
    public SprintEntity createSprint(int groundId) {
        Ground ground = groundRepository.findById(groundId).orElseThrow(() -> new NoSuchElementException("getGroundInfo :: 존재하지 않는 그라운드입니다."));

        LocalDate start, end;
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        int day = dayOfWeek.getValue();
        // 월요일부터 금요일
        System.out.println("day: " + day);
        if(day >= 1 && day <= 5){
            while(now.getDayOfWeek().getValue() != 1){
                now = now.minusDays(1);
            }
            System.out.println("before start: " + now);
            start = LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
            while(now.getDayOfWeek().getValue() != 5){
                now = now.plusDays(1);
            }
            System.out.println("before end: " + now);
            end = LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        }
        // 토요일 일요일
        else{
            while(now.getDayOfWeek().getValue() != 1){
                now = now.plusDays(1);
            }
            start = LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
            while(now.getDayOfWeek().getValue() != 5){
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
        try{
            sprintRepository.save(sprint);
        }catch(Exception e){
            throw new TransactionException("스프린트 저장에 실패했습니다.");
        }
        return sprint;
    }

    @Override
    public List<SprintEntity> loadSprintList(int groundId) {
        List<SprintEntity> sprintEntities = sprintRepository.findByGround_Id(groundId).orElseThrow(()-> new NoSuchElementException("스프린트 목록을 불러오는데 실패했습니다."));
        return sprintEntities;
    }

    @Override
    public void deleteSprint(int sprintId) {
        try{
            sprintRepository.deleteById(sprintId);
        }catch(Exception e){
            throw new TransactionException("스프린트 삭제에 실패 했습니다.");
        }
    }

    @Override
    public SprintEntity updateSprint(int sprintId, SprintUpdateDto sprintUpdateDto) {
        SprintEntity loadSprint = sprintRepository.findById(sprintId).orElseThrow(() -> new NoSuchElementException("getSprintInfo :: 존재하지 않는 스프린트입니다."));
        loadSprint.setName(sprintUpdateDto.getName());
        loadSprint.setGoal(sprintUpdateDto.getGoal());
        try{
            sprintRepository.save(loadSprint);
        }catch(Exception e){
            throw new TransactionException("스프린트 저장에 실패했습니다.");
        }
        return loadSprint;
    }

    @Override
    public void startSprint(int sprintId) {
        SprintEntity sprint = sprintRepository.findById(sprintId).orElseThrow(() -> new NoSuchElementException("getSprintInfo :: 존재하지 않는 스프린트입니다."));
        sprint.setStatus(1);
        try{
            sprintRepository.save(sprint);
        }catch(Exception e){
            throw new TransactionException("스프린트 저장에 실패했습니다.");
        }
    }

    @Override
    public void completeSprint(int sprintId) {
        SprintEntity sprint = sprintRepository.findById(sprintId).orElseThrow(() -> new NoSuchElementException("getSprintInfo :: 존재하지 않는 스프린트입니다."));
        sprint.setStatus(2);
        try{
            sprintRepository.save(sprint);
        }catch(Exception e){
            throw new TransactionException("스프린트 저장에 실패했습니다.");
        }
    }
}
