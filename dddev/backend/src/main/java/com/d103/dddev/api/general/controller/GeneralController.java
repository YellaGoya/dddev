package com.d103.dddev.api.general.controller;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.general.collection.General1;
import com.d103.dddev.api.general.collection.General2;
import com.d103.dddev.api.general.repository.dto.General1InsertDto;
import com.d103.dddev.api.general.repository.dto.General2MoveDto;
import com.d103.dddev.api.general.repository.dto.GeneralUpdateDto;
import com.d103.dddev.api.general.repository.dto.General2InsertDto;
import com.d103.dddev.api.general.service.GeneralServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ground/{groundId}/general")
@RequiredArgsConstructor
@Api(tags = {"일반 문서 API"})
public class GeneralController {
    private final GeneralServiceImpl generalService;

    @PostMapping("/step1")
    @ApiOperation(value="step 1 일반 문서 생성")
    public ResponseEntity<?> insertGeneral1(@PathVariable("groundId") int groundId){
        ResponseVO<General1> responseVo;

        try{
            General1 general = generalService.insertGeneral1(groundId);
            responseVo = ResponseVO.<General1>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서가 생성되었습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General1>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/step2")
    @ApiOperation(value="step 2 일반 문서 생성")
    public ResponseEntity<?> insertGeneral2(@PathVariable("groundId") int groundId, @RequestBody String parentId){
        ResponseVO<General2> responseVo;

        try{
            General2 general = generalService.insertGeneral2(groundId, parentId);
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서가 생성되었습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/step1/title")
    @ApiOperation(value="step 1 제목으로 일반 문서 생성")
    public ResponseEntity<?> insertGeneral1WithTitle(@PathVariable("groundId") int groundId,
                                           @ApiParam(value="title 필수") @Valid @RequestBody General1InsertDto general1InsertDto){
        ResponseVO<General1> responseVo;

        try{
            General1 general = generalService.insertGeneral1WithTitle(groundId, general1InsertDto);
            responseVo = ResponseVO.<General1>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서가 생성되었습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General1>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/step2/title")
    @ApiOperation(value="step 2 제목으로 일반 문서 생성")
    public ResponseEntity<?> insertGeneral2WithTitle(@PathVariable("groundId") int groundId,
                                            @ApiParam(value="title 필수") @Valid @RequestBody General2InsertDto general2InsertDto){
        ResponseVO<General2> responseVo;

        try{
            General2 general = generalService.insertGeneral2WithTitle(groundId, general2InsertDto);
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서가 생성되었습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/step1/titles")
    @ApiOperation(value="step 1 제목들로 일반 문서들 생성")
    public ResponseEntity<?> insertGeneral1WithTitles(@PathVariable("groundId") int groundId,
                                                     @RequestBody String[] titles){
        ResponseVO<List<General1>> responseVo;

        try{
            List<General1> generalList = generalService.insertGeneral1WithTitles(groundId, titles);
            responseVo = ResponseVO.<List<General1>>builder()
                    .code(HttpStatus.OK.value())
                    .message("일반 문서들이 생성되었습니다.")
                    .data(generalList)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<List<General1>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/step1/{generalId}")
    @ApiOperation(value="step1 문서 Id로 불러오기")
    public ResponseEntity<?> getGeneral1(@PathVariable("groundId") int groundId, @PathVariable("generalId") String generalId){
        ResponseVO<General1> responseVo;

        try{
            General1 general = generalService.getGeneral1(groundId, generalId);
            responseVo = ResponseVO.<General1>builder()
                    .code(HttpStatus.OK.value())
                    .message("step1 문서를 불러왔습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General1>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/step2/{generalId}")
    @ApiOperation(value="step2 문서 Id로 불러오기")
    public ResponseEntity<?> getGeneral2(@PathVariable("groundId") int groundId, @PathVariable("generalId") String generalId){
        ResponseVO<General2> responseVo;

        try{
            General2 general = generalService.getGeneral2(groundId, generalId);
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.OK.value())
                    .message("step1 문서를 불러왔습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/step1")
    @ApiOperation(value="step1 문서 수정하기")
    public ResponseEntity<?> updateGeneral1(@PathVariable("groundId") int groundId, @RequestBody GeneralUpdateDto generalUpdateDto){
        ResponseVO<General1> responseVo;

        try{
            General1 general = generalService.updateGeneral1(groundId, generalUpdateDto);
            responseVo = ResponseVO.<General1>builder()
                    .code(HttpStatus.OK.value())
                    .message("step1 문서를 수정했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General1>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/step2")
    @ApiOperation(value="step2 문서 수정하기")
    public ResponseEntity<?> updateGeneral2(@PathVariable("groundId") int groundId, @RequestBody GeneralUpdateDto generalUpdateDto){
        ResponseVO<General2> responseVo;

        try{
            General2 general = generalService.updateGeneral2(groundId, generalUpdateDto);
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.OK.value())
                    .message("step2 문서를 수정했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/step2/move")
    @ApiOperation(value="step2 문서 위치이동하기")
    public ResponseEntity<?> moveGeneral2(@PathVariable("groundId") int groundId,
                                          @ApiParam(value="id -> 옮기려는 문서의 아이디\n" +
                                                  "parentId -> 목적지 부모의 아이디") @RequestBody General2MoveDto general2MoveDto){
        ResponseVO<General2> responseVo;

        try{
            General2 general = generalService.moveGeneral2(groundId, general2MoveDto);
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 이동했습니다.")
                    .data(general)
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/step1/{generalId}")
    @ApiOperation(value="step1 문서 삭제하기")
    public ResponseEntity<?> deleteGeneral1(@PathVariable("groundId") int groundId, @PathVariable("generalId") String generalId){

        ResponseVO<General1> responseVo;

        try{
            generalService.deleteGeneral1(groundId, generalId);
            responseVo = ResponseVO.<General1>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General1>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/step2/{generalId}")
    @ApiOperation(value="step2 문서 삭제하기")
    public ResponseEntity<?> deleteGeneral2(@PathVariable("groundId") int groundId, @PathVariable("generalId") String generalId){

        ResponseVO<General2> responseVo;

        try{
            generalService.deleteGeneral2(groundId, generalId);
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.OK.value())
                    .message("문서를 삭제했습니다.")
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.OK);
        }catch(Exception e){
            responseVo = ResponseVO.<General2>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(responseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
