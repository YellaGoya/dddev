package com.d103.dddev.api.general.controller;

import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.service.GeneralServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/general")
@RequiredArgsConstructor
public class GeneralController {
    private final GeneralServiceImpl generalService;

    @PostMapping("/insert")
    public ResponseEntity<?> putGeneral(@RequestBody General general){
        System.out.println("--------------------------------------");
        System.out.println(general);
        System.out.println("--------------------------------------");
        HashMap<String, String> result = new HashMap<>();

        try{
            generalService.insertGeneral(general);
            result.put("message", "등록에 성공했습니다.");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(Exception e){
            result.put("message", "등록에 실패했습니다");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{title}")
    public ResponseEntity<?> getGeneral(@PathVariable("title") String title){
        System.out.println("--------------------------------------");
        System.out.println(title);
        System.out.println("--------------------------------------");
        HashMap<String, Object> result = new HashMap<>();

        try{
            General returnGeneral = generalService.getGeneral(title);
            result.put("message", "등록에 성공했습니다.");
            result.put("general", returnGeneral);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(Exception e){
            result.put("message", "등록에 실패했습니다");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/list")
    public ResponseEntity<?> getGeneralList(){
        HashMap<String, Object> result = new HashMap<>();

        try{
            List<General> returnGeneral = generalService.getGeneralList();
            result.put("message", "등록에 성공했습니다.");
            result.put("general", returnGeneral);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(Exception e){
            result.put("message", "등록에 실패했습니다");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
