package com.dddev.log.service;

import com.dddev.log.config.AesUtil;
import com.dddev.log.entity.GroundAuth;
import com.dddev.log.exception.UserUnAuthException;
import com.dddev.log.repository.GroundAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class GroundAuthService {

    private final GroundAuthRepository groundAuthRepository;
    private final AesUtil aesUtil;

    //토큰 저장
    public void save(String token) throws Exception {
        String str = aesUtil.aes256Decrypt(token);
        Map<String, Integer> userInfo = aesUtil.getUserInfo(str);
        Integer groundId = userInfo.get("groundId");
        Integer userId = userInfo.get("userId");
        List<GroundAuth> groundAuths = groundAuthRepository.findByGroundId(groundId);
        //Group 당 3개의 토큰 이상을 발급 불가
        if(groundAuths.size() >= 3) {
            groundAuthRepository.delete(groundAuths.get(0));
        }
        GroundAuth groundAuth = new GroundAuth(userId, groundId, token);
        groundAuth.setLocalDateTime(LocalDateTime.now());
        groundAuthRepository.save(groundAuth);
    }

     // 토큰으로 유효성 체크 및 그라운드 아이디 반환
    public String  checkValid(String token)  {
        try{
            String str = aesUtil.aes256Decrypt(token);
            Map<String, Integer> userInfo = aesUtil.getUserInfo(str);
            Integer groundId = userInfo.get("groundId");
            Integer userId = userInfo.get("userId");
            List<String> tempToken = groundAuthRepository.findToken(groundId, userId);
            if(tempToken == null){
                throw new UserUnAuthException("토큰이 유효하지 않습니다.");
            }
            return String.valueOf(groundId);
        } catch (Exception e){
            throw new UserUnAuthException("토큰이 유효하지 않습니다.");
        }
    }

    //비정상적인 요청으로 Group_id와 관련 된 토큰 삭제
    public void  deleteToken(String groundId) {
        List<GroundAuth> GroundAuths = groundAuthRepository.findByGroundId(Integer.parseInt(groundId));
        GroundAuths.forEach(a -> {
            groundAuthRepository.delete(a);
        });
    }
}
