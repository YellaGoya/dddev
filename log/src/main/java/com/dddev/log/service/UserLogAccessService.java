package com.dddev.log.service;

import com.dddev.log.entity.UserLogAccess;
import com.dddev.log.exception.UserUnAuthException;
import com.dddev.log.repository.UserLogAccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLogAccessService {

    private final UserLogAccessRepository userLogAccessRepository;
    private final GroundAuthService groundAuthService;
    private final ElasticSearchLogService elasticSearchLogService;

    //로그 요청에 따른 카운트 캐시
    public void count(String groundId){
        Optional<UserLogAccess> temp = userLogAccessRepository.findById(groundId);
        //만약 groupt_id가 존재하지 않으면 redis에 생성
        if(!temp.isPresent()){
            userLogAccessRepository.save( UserLogAccess.builder()
                                                                    .groundId(groundId)
                                                                    .count(1)
                                                                    .index(0)
                                                                    .expiration(60L)
                                                                    .build());
            log.info("GROUND ID: {} 로그 등록 URI 접근", groundId);
            return;
        }
        //만약 group_id가 존재하면 1분당 로그 수 + 1
        UserLogAccess userLogAccess = temp.get();
        userLogAccess.increase();
        log.info("GROUND ID: {}, INDEX: {}, 로그 등록 URI 접근 최근 60초 {}번 접근, 남은 시간 {}초", groundId, userLogAccess.getIndex(), userLogAccess.getCount(), userLogAccess.getExpiration());
        //비정상적인 로그 요청 횟수(특정 횟수)라는 판단이 들면 삭제
        if(userLogAccess.getCount() >= 50) {
            //만약 첫 시도이면 -> docker로 첫 실행 시 로그 한 번에 많이 들어올 경우 대비
            if(userLogAccess.getIndex() == 0) {
                userLogAccess.setCount(0);
                userLogAccess.setIndex(1);
                userLogAccess.setExpiration(60L);
            }
            else{
                //토큰에서 삭제, ela에서 인덱스 삭제
                groundAuthService.deleteToken(groundId);
                elasticSearchLogService.deleteIndex(groundId);
                log.info("GROUND ID: {}, 로그 등록 URI 접근 최근 60초 1000번 이상 접근으로 비정상접근으로 판단, 등록된 모든 로그 삭제", groundId);
                throw new UserUnAuthException.UnusualRequest("비정상적으로 많은 로그를 요청 중입니다. token을 재발급해주세요.");
            }
        }
        userLogAccessRepository.save(userLogAccess);
    }
}
