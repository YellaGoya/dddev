package com.d103.dddev.api.issue.controller;

import com.d103.dddev.api.issue.model.dto.IssueDto;
import com.d103.dddev.api.issue.service.IssueService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issue")
@AllArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody IssueDto.Request request){
        IssueDto.Response response = issueService.create(request);

        return ResponseEntity.status(200).body(response);
    }
}
