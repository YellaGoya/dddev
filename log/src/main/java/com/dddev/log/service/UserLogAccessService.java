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
            log.info("생성!");
            userLogAccessRepository.save( UserLogAccess.builder()
                                                                    .groundId(ground_id)
                                                                    .count(1)
                                                                    .expiration(60L)
                                                                    .build());
            return;
        }
        //만약 group_id가 존재하면 1분당 로그 수 + 1
        UserLogAccess userLogAccess = temp.get();
        userLogAccess.increase();
        //비정상적인 로그 요청 횟수라는 판단이 들면 삭제
        if(userLogAccess.getCount() >= 10000000L) {
            //토큰에서 삭제, ela에서 인덱스 삭제
            groundAuthService.deleteToken(ground_id);
            elasticSearchLogService.deleteIndex(ground_id);
            throw new UserUnAuthException.UnusualRequest("비정상적으로 많은 로그를 요청 중입니다. token을 재발급해주세요.");
        }
        userLogAccessRepository.save(userLogAccess);
        log.info(userLogAccess.getCount().toString());
    }
}
