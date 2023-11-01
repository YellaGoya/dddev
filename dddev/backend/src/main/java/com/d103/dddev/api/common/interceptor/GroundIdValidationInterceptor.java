package com.d103.dddev.api.common.interceptor;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.ground.repository.GroundRepository;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
@NoArgsConstructor
@AllArgsConstructor
public class GroundIdValidationInterceptor implements HandlerInterceptor {
    private GroundRepository groundRepository;
    private ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public GroundIdValidationInterceptor(GroundRepository groundRepository){
        this.groundRepository = groundRepository;
    }

    // preHandle 메서드를 오버라이딩하여 groundId 검사 로직을 구현합니다.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // groundId를 파라미터에서 추출합니다.
        String requestURI = request.getRequestURI();
        String[] parts = requestURI.split("/");
        int groundId = Integer.parseInt(parts[2].trim());

        Optional<GroundDto> groundDtoOptional = groundRepository.findById(groundId);
        if(groundDtoOptional.isEmpty()){
            ResponseVO<Object> responseVO = ResponseVO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("잘못된 그라운드 아이디입니다.")
                    .build();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            String result = mapper.writeValueAsString(responseVO);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(result);
            return false;
        }
        GroundDto ground = groundDtoOptional.get();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ground", ground);
        request.setAttribute("modelAndView", modelAndView);

        return true; // 처리 방법에 따라 true 또는 false를 반환합니다.
    }
}

