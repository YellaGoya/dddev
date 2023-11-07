package com.d103.dddev.api.general.controller;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.repository.dto.*;
import com.d103.dddev.api.general.service.GeneralServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ground/{groundId}/general")
@RequiredArgsConstructor
@Api(tags = {"일반 문서 API"})
public class GeneralController {
    private final GeneralServiceImpl generalService;

    @PostMapping
    @ApiOperation(value="일반 문서 생성")
    public ResponseEntity<?> insertGeneral(@PathVariable("groundId") int groundId,
                                                @ApiParam(value = "step -> required\n"+
                                                        "step값이 1일때는 parentId 필요없음\n" +
                                                        "title -> not required")@RequestBody GeneralInsertOneDto generalInsertOneDto,
                                           @RequestHeader String Authorization){
        ResponseVO<General> responseVo;

        try{
            General general = generalService.insertGeneral(groundId, generalInsertOneDto);
            responseVo = ResponseVO.<General>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서가 생성되었습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/titles")
    @ApiOperation(value="제목들로 step1 일반 문서들 생성")
    public ResponseEntity<?> insertGeneralsWithTitles(@PathVariable("groundId") int groundId,
                                                     @RequestBody GeneralInsertManyDto generalInsertManyDto,
                                                      @RequestHeader String Authorization){
        ResponseVO<List<General>> responseVo;

        try{
            List<General> generalList = generalService.insertGeneralsWithTitles(groundId, generalInsertManyDto);
            responseVo = ResponseVO.<List<General>>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서들이 생성되었습니다.")
                    .data(generalList)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<List<General>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{generalId}")
    @ApiOperation(value="문서 아이디로 일반 문서 가져오기")
    public ResponseEntity<?> getGeneral(@PathVariable("groundId") int groundId, @PathVariable("generalId") String generalId,
                                        @RequestHeader String Authorization){
        ResponseVO<General> responseVo;

        try{
            General general = generalService.getGeneral(groundId, generalId);
            responseVo = ResponseVO.<General>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서를 불러왔습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/step1")
    @ApiOperation(value="step1 문서들 불러오기")
    public ResponseEntity<?> getStep1Generals(@PathVariable("groundId") int groundId,
                                              @RequestHeader String Authorization){
        ResponseVO<List<General>> responseVo;

        try{
            List<General> generals = generalService.getStep1Generals(groundId);
            responseVo = ResponseVO.<List<General>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step1 문서들을 불러왔습니다.")
                    .data(generals)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<List<General>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/step2")
    @ApiOperation(value="step2 문서들 불러오기")
    public ResponseEntity<?> getStep2Generals(@PathVariable("groundId") int groundId,
                                              @RequestHeader String Authorization){
        ResponseVO<List<General>> responseVo;

        try{
            List<General> generals = generalService.getStep2Generals(groundId);
            responseVo = ResponseVO.<List<General>>builder()
                    .code(HttpStatus.OK.value())
                    .message("step2 문서들을 불러왔습니다.")
                    .data(generals)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<List<General>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping
    @ApiOperation(value="문서 수정하기")
    public ResponseEntity<?> updateGeneral(@PathVariable("groundId") int groundId, @RequestBody GeneralUpdateDto generalUpdateDto,
                                           @RequestHeader String Authorization){
        ResponseVO<General> responseVo;

        try{
            General general = generalService.updateGeneral(groundId, generalUpdateDto);
            responseVo = ResponseVO.<General>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 수정했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @PutMapping("/move")
    @ApiOperation(value="일반 문서 위치이동하기")
    public ResponseEntity<?> moveGeneral(@PathVariable("groundId") int groundId,
                                          @ApiParam(value="id -> 옮기려는 문서의 아이디\n" +
                                                  "parentId -> 목적지 부모의 아이디") @RequestBody GeneralMoveDto GeneralMoveDto,
                                         @RequestHeader String Authorization) {
        ResponseVO<General> responseVo;

        try{
            General general = generalService.moveGeneral(groundId, GeneralMoveDto);
            responseVo = ResponseVO.<General>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 이동했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    @ApiOperation(value="일반 문서 삭제하기")
    public ResponseEntity<?> deleteGeneral(@PathVariable("groundId") int groundId, @RequestBody GeneralDeleteDto generalDeleteDto,
                                           @RequestHeader String Authorization){

        ResponseVO<General> responseVo;

        try{
            generalService.deleteGeneral(groundId, generalDeleteDto);
            responseVo = ResponseVO.<General>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
