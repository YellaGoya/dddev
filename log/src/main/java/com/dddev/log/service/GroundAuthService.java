package com.dddev.log.service;

import com.dddev.log.config.AesUtil;
import com.dddev.log.entity.GroundAuth;
import com.dddev.log.repository.GroundAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroundAuthService {

    private final GroundAuthRepository groundAuthRepository;
    private final AesUtil aesUtil;

//    토큰 저장
    public void save(String token) throws Exception {
        String str = aesUtil.aes256Decrypt(token);
        Map<String, Integer> userInfo = aesUtil.getUserInfo(str);
        Integer groundId = userInfo.get("groundId");
        Integer userId = userInfo.get("userId");
        groundAuthRepository.save(new GroundAuth(userId, groundId, token));
    }

    //토큰으로 유효성 체크
    //    public void save(String token) throws Exception {
//        aesUtil.aes256Decrypt(token);
//        GroundAuth();
//        groundAuthRepository.save()
//    }
}
