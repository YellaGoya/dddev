package com.d103.dddev.api.general.controller;

import com.d103.dddev.api.common.ResponseDto;
import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.repository.dto.requestDto.*;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralResponseDto;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralTitleResponseDto;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralTreeResponseDto;
import com.d103.dddev.api.general.service.GeneralServiceImpl;
import com.d103.dddev.api.user.repository.dto.UserDto;
import com.d103.dddev.api.user.repository.entity.User;
import com.d103.dddev.common.exception.document.DocumentNotFoundException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.management.InvalidAttributeValueException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/ground/{groundId}/general")
@RequiredArgsConstructor
@Api(tags = {"일반 문서 API"})
public class GeneralController {
    private final GeneralServiceImpl generalService;

    @PostMapping("/create")
    @ApiOperation(value="일반 문서 생성")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<GeneralResponseDto>> insertGeneral(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                                         @ApiParam(value = "step1문서 생성할 때 parentId 필요없음\n" +
                                                                "미분류로 생성할 때 parentId 미분류 문서 id\n" +
                                                                "title -> not required 없으면 빈 문자열 \"\"로 생성")@RequestBody GeneralInsertOneDto generalInsertOneDto,
                                                                         @ApiParam(value = "인증 정보")@RequestHeader String Authorization,
                                                                         HttpServletRequest request){
        ResponseDto<GeneralResponseDto> responseDto;
        ModelAndView modelAndView = (ModelAndView) request.getAttribute("modelAndView");
        User user = (User) modelAndView.getModel().get("user");
        UserDto userDto = user.convertToDto();

        try{
            GeneralResponseDto general = generalService.insertGeneral(groundId, generalInsertOneDto, userDto);
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서가 생성되었습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/titles")
    @ApiOperation(value="제목들로 step1 일반 문서들 생성")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<GeneralResponseDto>>> insertGeneralsWithTitles(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                                                          @RequestBody GeneralInsertManyDto generalInsertManyDto,
                                                                                          @ApiParam(value = "인증 정보") @RequestHeader String Authorization,
                                                                                          HttpServletRequest request){
        ResponseDto<List<GeneralResponseDto>> responseDto;
        ModelAndView modelAndView = (ModelAndView) request.getAttribute("modelAndView");
        User user  = (User) modelAndView.getModel().get("user");
        UserDto userDto = user.convertToDto();

        try{
            List<GeneralResponseDto> generalList = generalService.insertGeneralsWithTitles(groundId, generalInsertManyDto, userDto);
            responseDto = ResponseDto.<List<GeneralResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서들이 생성되었습니다.")
                    .data(generalList)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<GeneralResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{generalId}")
    @ApiOperation(value="일반 문서 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<GeneralResponseDto>> getGeneral(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                                      @ApiParam(value = "문서 아이디") @PathVariable("generalId") String generalId,
                                                                      @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<GeneralResponseDto> responseDto;

        try{
            GeneralResponseDto general = generalService.getGeneral(groundId, generalId);
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서를 불러왔습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total")
    @ApiOperation(value="문서트리 구조로 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<GeneralTreeResponseDto>>> getTreeGenerals(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                                                     @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<GeneralTreeResponseDto>> responseDto;

        try{
            List<GeneralTreeResponseDto> generals = generalService.getTreeGenerals(groundId);
            responseDto = ResponseDto.<List<GeneralTreeResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서 트리를 불러왔습니다.")
                    .data(generals)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<GeneralTreeResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/list")
    @ApiOperation(value="step1 문서들 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<GeneralTitleResponseDto>>> getStep1Generals(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                                                       @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<GeneralTitleResponseDto>> responseDto;

        try{
            List<GeneralTitleResponseDto> generals = generalService.getStep1Generals(groundId);
            responseDto = ResponseDto.<List<GeneralTitleResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step1 문서들을 불러왔습니다.")
                    .data(generals)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<GeneralTitleResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/step2")
    @ApiOperation(value="step2 문서들 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<List<GeneralResponseDto>>> getStep2Generals(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                                                  @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<GeneralResponseDto>> responseDto;

        try{
            List<GeneralResponseDto> generals = generalService.getStep2Generals(groundId);
            responseDto = ResponseDto.<List<GeneralResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step2 문서들을 불러왔습니다.")
                    .data(generals)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<GeneralResponseDto>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{generalId}")
    @ApiOperation(value="일반 문서 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<GeneralResponseDto>> updateGeneral(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                         @ApiParam(value = "문서 아이디")@PathVariable("generalId") String generalId,
                                                                         @ApiParam(value = "title(필수 x), content(필수 x)")@RequestBody GeneralUpdateDto generalUpdateDto,
                                                                         @ApiParam(value = "인증 정보") @RequestHeader String Authorization,
                                                                         HttpServletRequest request){
        ResponseDto<GeneralResponseDto> responseDto;
        ModelAndView modelAndView = (ModelAndView) request.getAttribute("modelAndView");
        User user = (User) modelAndView.getModel().get("user");
        UserDto userDto = user.convertToDto();


        try{
            GeneralResponseDto general = generalService.updateGeneral(groundId, generalId, generalUpdateDto, userDto);
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 수정했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @PutMapping("/{generalId}/connect")
    @ApiOperation(value="일반 문서 카테고리 이동")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<GeneralResponseDto>> moveGeneral(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                       @ApiParam(value = "문서 아이디")@PathVariable("generalId") String generalId,
                                                                       @ApiParam(value= "parentId -> 목적지 부모의 아이디") @RequestBody GeneralMoveDto generalMoveDto,
                                                                       @ApiParam(value = "인증 정보")@RequestHeader String Authorization) {
        ResponseDto<GeneralResponseDto> responseDto;

        try{
            GeneralResponseDto general = generalService.moveGeneral(groundId, generalId, generalMoveDto);
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 이동했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{generalId}")
    @ApiOperation(value="일반 문서 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<Void>> deleteGeneral(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                           @ApiParam(value = "문서 아이디")@PathVariable("generalId") String generalId,
                                                           @ApiParam(value = "인증 정보")@RequestHeader String Authorization){

        ResponseDto<Void> responseDto;

        try{
            generalService.deleteGeneral(groundId, generalId);
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<Void>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{generalId}/title")
    @ApiOperation(value="일반 문서 제목 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<GeneralResponseDto>> updateGeneral(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                         @ApiParam(value = "문서 아이디")@PathVariable("generalId") String generalId,
                                                                         @ApiParam(value = "변경하고 싶은 제목\n" +
                                                                         "title(필수) 없으면 422에러")@RequestBody GeneralTitleDto generalTitleDto,
                                                                         @AuthenticationPrincipal UserDetails userDetails,
                                                                         @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<GeneralResponseDto> responseDto;

        try{
            GeneralResponseDto general = generalService.titleGeneral(groundId, generalId, generalTitleDto, userDetails);
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서의 제목을 수정했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{generalId}/template")
    @ApiOperation(value="일반 문서 템플릿 여부 변경", notes = "template 속성 여부 변경")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "문서 존재하지 않음"),
            @ApiResponse(code = 422, message = "잘못된 요청 데이터"),
            @ApiResponse(code = 500, message = "서버 or 데이터베이스 에러")
    })
    public ResponseEntity<ResponseDto<GeneralResponseDto>> changeTemplate(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                                          @ApiParam(value = "문서 아이디")@PathVariable("generalId") String generalId,
                                                                          @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<GeneralResponseDto> responseDto;

        try{
            GeneralResponseDto general = generalService.changeTemplate(groundId, generalId);
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서 템플릿 여부 수정 완료")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<GeneralResponseDto>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
