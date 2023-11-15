package com.dddev.log.controller;

import com.dddev.log.dto.ResponseVO;
import com.dddev.log.dto.req.ChatGptReq;
import com.dddev.log.exception.ChatGptException;
import com.dddev.log.exception.ElasticSearchException;
import com.dddev.log.exception.UserUnAuthGptException;
import com.dddev.log.service.ChatService;
import com.dddev.log.service.UserGptAccessService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"CHAT GPT 관련 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class gptcontroller {

    private final ChatService chatService;
    private final UserGptAccessService userGptAccessService;

    @ApiOperation(value = "일반 질문을 chat gpt가 대답해주는 API // 프롬프터 적용 X")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "GPT 요청 과다"),
                    @ApiResponse(code = 401, message = "header의 group_id가 존재하지 않을 때"),
                    @ApiResponse(code = 404, message = "저장된 로그가 없을 때"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @PostMapping("")
    public ResponseEntity<?> chat(
            @ApiParam(value = "그라운드 ID", required = true)  @RequestHeader String groundId,
            @ApiParam(value = "GPT에게 할 질문", required = true)  @RequestBody ChatGptReq chatGptReq) {
            if (chatGptReq.getQuestion().equals("")) {
                throw new ChatGptException.IncorrectQuestion("질문을 입력해주세요.");
            }
            try {
                userGptAccessService.count(groundId);
                String result = "일반 질문 분석 완료!";
                String response = chatService.chatGpt(groundId, chatGptReq.getQuestion()).trim().replaceAll("\n", "");
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                        result, response));
            }catch (UserUnAuthGptException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVO<>(HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(), null));
            }catch (ElasticSearchException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                        e.getMessage(), null));
            } catch (NoSuchIndexException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                        "잘못된 ground id 접근입니다.", null));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        e.getMessage(), null));
            }
        }

    //사용자가 로그를 입력해서 chat gpt가 자동으로 분석하는 API
    @ApiOperation(value = "사용자가 로그를 입력해서 chat gpt가 자동으로 분석하는 API // 로그 프롬프터 적용")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "GPT 요청 과다"),
                    @ApiResponse(code = 401, message = "header의 ground_id가 존재하지 않을 때"),
                    @ApiResponse(code = 404, message = "저장된 로그가 없을 때"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @PostMapping("/analyze")
    public ResponseEntity<?> analzeLog(
            @ApiParam(value = "그라운드 ID", required = true)  @RequestHeader String groundId,
            @ApiParam(value = "GPT에게 할 질문", required = true)  @RequestBody ChatGptReq chatGptReq) {
        if (chatGptReq.getQuestion().equals("")) {
            throw new ChatGptException.IncorrectQuestion("질문을 입력해주세요.");
        }
        try{
            userGptAccessService.count(groundId);
            String result = "사용자 로그 분석 완료!";
            String response = chatService.chatGptLog(groundId, chatGptReq.getQuestion()).trim().replaceAll("\n", "");
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, response));
        }catch (UserUnAuthGptException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVO<>(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(), null));
        }catch (ElasticSearchException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (NoSuchIndexException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    "잘못된 ground id 접근입니다.", null));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }

    //최근 로그 20줄 불러와서 분석하기
    @ApiOperation(value = "최근 저장된 로그 20줄을 불러와서 chat gpt가 자동으로 분석하는 API // 테스트")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "GPT 요청 과다"),
                    @ApiResponse(code = 401, message = "header의 group_id가 존재하지 않을 때"),
                    @ApiResponse(code = 404, message = "저장된 로그가 없을 때"),
                    @ApiResponse(code = 500, message = "서버 내부 오류")})
    @GetMapping("/analyze")
    public ResponseEntity<?> analyzeLogAuto(
            @ApiParam(value = "그라운드 ID", required = true)  @RequestHeader String groundId) {
        try{
            userGptAccessService.count(groundId);
            String result = "최근 로그를 불러 분석 완료";
            String response = chatService.chatGptLogAuto(groundId).trim().replaceAll("\n", "");
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO<>(HttpStatus.OK.value(),
                    result, response));
        }catch (ElasticSearchException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVO<>(HttpStatus.NOT_FOUND.value(),
                    e.getMessage(), null));
        }catch (NoSuchIndexException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(),
                    "잘못된 ground id 접근입니다.", null));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(), null));
        }
    }
}
