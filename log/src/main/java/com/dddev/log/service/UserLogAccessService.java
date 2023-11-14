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
    public void count(String ground_id){
        Optional<UserLogAccess> temp = userLogAccessRepository.findById(ground_id);
        //만약 groupt_id가 존재하지 않으면 redis에 생성
        if(!temp.isPresent()){
            userLogAccessRepository.save( UserLogAccess.builder()
                                                                    .groundId(ground_id)
                                                                    .count(1)
                                                                    .index(0)
                                                                    .expiration(60L)
                                                                    .build());
            return;
        }
        //만약 group_id가 존재하면 1분당 로그 수 + 1
        UserLogAccess userLogAccess = temp.get();
        userLogAccess.increase();
        log.info("{}의 접근은 60초 당 {}개", ground_id, userLogAccess.getCount());
        //비정상적인 로그 요청 횟수(특정 횟수)라는 판단이 들면 삭제
        if(userLogAccess.getCount() >= 1000) {
            //만약 첫 시도이면 -> docker로 첫 실행 시 로그 한 번에 많이 들어올 경우 대비
            if(userLogAccess.getIndex() == 0) {
                userLogAccess.setCount(0);
                userLogAccess.setIndex(1);
            }
            else{
                //토큰에서 삭제, ela에서 인덱스 삭제
                groundAuthService.deleteToken(ground_id);
                elasticSearchLogService.deleteIndex(ground_id);
                throw new UserUnAuthException.UnusualRequest("비정상적으로 많은 로그를 요청 중입니다. token을 재발급해주세요.");
            }
        }
        userLogAccessRepository.save(userLogAccess);
    }
}
