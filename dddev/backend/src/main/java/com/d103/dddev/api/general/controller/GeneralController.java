package com.d103.dddev.api.general.controller;

import com.d103.dddev.api.common.ResponseDto;
import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.repository.dto.requestDto.*;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralStepResponseDto;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralTreeResponseDto;
import com.d103.dddev.api.general.service.GeneralServiceImpl;
import com.d103.dddev.common.exception.document.DocumentNotFoundException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.management.InvalidAttributeValueException;
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
    public ResponseEntity<ResponseDto<General>> insertGeneral(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                              @ApiParam(value = "step1문서 생성할 때 parentId 필요없음\n" +
                                                                "미분류로 생성할 때 parentId 미분류 문서 id\n" +
                                                                "title -> not required 없으면 빈 문자열 \"\"로 생성")@RequestBody GeneralInsertOneDto generalInsertOneDto,
                                                              @ApiParam(value = "인증 정보")@RequestHeader String Authorization,
                                                              @AuthenticationPrincipal UserDetails userDetails){
        ResponseDto<General> responseDto;

        try{
            General general = generalService.insertGeneral(groundId, generalInsertOneDto, userDetails);
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서가 생성되었습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<General>builder()
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
    public ResponseEntity<ResponseDto<List<General>>> insertGeneralsWithTitles(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                                               @RequestBody GeneralInsertManyDto generalInsertManyDto,
                                                                               @ApiParam(value = "인증 정보") @RequestHeader String Authorization,
                                                                               @AuthenticationPrincipal UserDetails userDetails){
        ResponseDto<List<General>> responseDto;

        try{
            List<General> generalList = generalService.insertGeneralsWithTitles(groundId, generalInsertManyDto, userDetails);
            responseDto = ResponseDto.<List<General>>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서들이 생성되었습니다.")
                    .data(generalList)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<General>>builder()
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
    public ResponseEntity<ResponseDto<General>> getGeneral(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                          @ApiParam(value = "문서 아이디") @PathVariable("generalId") String generalId,
                                                          @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<General> responseDto;

        try{
            General general = generalService.getGeneral(groundId, generalId);
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서를 불러왔습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            responseDto = ResponseDto.<General>builder()
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
    public ResponseEntity<ResponseDto<List<GeneralStepResponseDto>>> getStep1Generals(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                                                      @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<GeneralStepResponseDto>> responseDto;

        try{
            List<GeneralStepResponseDto> generals = generalService.getStep1Generals(groundId);
            responseDto = ResponseDto.<List<GeneralStepResponseDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step1 문서들을 불러왔습니다.")
                    .data(generals)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<GeneralStepResponseDto>>builder()
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
    public ResponseEntity<ResponseDto<List<General>>> getStep2Generals(@ApiParam(value = "그라운드 아이디") @PathVariable("groundId") int groundId,
                                                                       @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<List<General>> responseDto;

        try{
            List<General> generals = generalService.getStep2Generals(groundId);
            responseDto = ResponseDto.<List<General>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step2 문서들을 불러왔습니다.")
                    .data(generals)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(Exception e){
            responseDto = ResponseDto.<List<General>>builder()
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
    public ResponseEntity<ResponseDto<General>> updateGeneral(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                              @ApiParam(value = "문서 아이디")@PathVariable("generalId") String generalId,
                                                              @ApiParam(value = "title(필수 x), content(필수 x)")@RequestBody GeneralUpdateDto generalUpdateDto,
                                                              @AuthenticationPrincipal UserDetails userDetails,
                                                              @ApiParam(value = "인증 정보") @RequestHeader String Authorization){
        ResponseDto<General> responseDto;

        try{
            General general = generalService.updateGeneral(groundId, generalId, generalUpdateDto, userDetails);
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 수정했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<General>builder()
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
    public ResponseEntity<ResponseDto<General>> moveGeneral(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                            @ApiParam(value = "문서 아이디")@PathVariable("generalId") String generalId,
                                                            @ApiParam(value= "parentId -> 목적지 부모의 아이디") @RequestBody GeneralMoveDto GeneralMoveDto,
                                                            @ApiParam(value = "인증 정보")@RequestHeader String Authorization) {
        ResponseDto<General> responseDto;

        try{
            General general = generalService.moveGeneral(groundId, generalId, GeneralMoveDto);
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 이동했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<General>builder()
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
    public ResponseEntity<ResponseDto<General>> updateGeneral(@ApiParam(value = "그라운드 아이디")@PathVariable("groundId") int groundId,
                                                              @ApiParam(value = "문서 아이디")@PathVariable("generalId") String generalId,
                                                              @ApiParam(value = "변경하고 싶은 제목\n" +
                                                                      "title(필수) 없으면 422에러")@RequestBody GeneralTitleDto generalTitleDto,
                                                              @AuthenticationPrincipal UserDetails userDetails,
                                                              @ApiParam(value = "인증 정보")@RequestHeader String Authorization){
        ResponseDto<General> responseDto;

        try{
            General general = generalService.titleGeneral(groundId, generalId, generalTitleDto, userDetails);
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서의 제목을 수정했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch(DocumentNotFoundException e){
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }catch(InvalidAttributeValueException e){
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(Exception e){
            responseDto = ResponseDto.<General>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
