package com.d103.dddev.api.common.interceptor;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.common.oauth2.utils.JwtService;
import com.d103.dddev.api.ground.repository.GroundRepository;
import com.d103.dddev.api.ground.repository.GroundUserRepository;
import com.d103.dddev.api.ground.repository.dto.GroundDto;
import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.user.repository.UserRepository;
import com.d103.dddev.api.user.repository.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.apache.coyote.Response;
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
	private GroundUserRepository groundUserRepository;
	private JwtService jwtService;
	private ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

	public GroundIdValidationInterceptor(GroundRepository groundRepository, GroundUserRepository groundUserRepository,
		JwtService jwtService) {
		this.groundRepository = groundRepository;
		this.groundUserRepository = groundUserRepository;
		this.jwtService = jwtService;
	}

	// preHandle 메서드를 오버라이딩하여 groundId 검사 로직을 구현합니다.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {

		ResponseVO<Object> responseVO;

		// groundId를 파라미터에서 추출합니다.
		String requestURI = request.getRequestURI();
		String[] parts = requestURI.split("/");
		int groundId = Integer.parseInt(parts[2].trim());

		// 그라운드 조회
		Optional<GroundDto> groundDtoOptional = groundRepository.findById(groundId);

		if (groundDtoOptional.isEmpty()) {
			responseVO = ResponseVO.builder()
				.code(HttpStatus.NOT_ACCEPTABLE.value())
				.message("잘못된 그라운드 아이디입니다.")
				.build();
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			String result = mapper.writeValueAsString(responseVO);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(result);
			return false;
		}


		ModelAndView mav = (ModelAndView)request.getAttribute("modelAndView");
		UserDto userDto = (UserDto)mav.getModel().get("userDto");

		// 해당 그라운드의 멤버인지 검증
		Optional<GroundUserDto> groundUserDtoOptional = groundUserRepository.findByGroundDto_IdAndUserDto_Id(
			groundId, userDto.getId());

		if (groundUserDtoOptional.isEmpty()) {
			responseVO = ResponseVO.builder()
				.code(HttpStatus.BAD_REQUEST.value())
				.message("사용자가 해당 그라운드의 멤버가 아닙니다.")
				.build();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String result = mapper.writeValueAsString(responseVO);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(result);
			return false;
		}

		GroundDto ground = groundDtoOptional.get();

		// ModelAndView modelAndView = new ModelAndView();
		mav.addObject("groundDto", ground);
		request.setAttribute("modelAndView", mav);
		return true; // 처리 방법에 따라 true 또는 false를 반환합니다.
	}
}

