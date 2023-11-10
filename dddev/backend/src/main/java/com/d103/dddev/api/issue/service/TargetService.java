package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.issue.model.dto.IssueDto;
import com.d103.dddev.api.issue.model.dto.TargetDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface TargetService {

    TargetDto.Create.Response createTarget(Integer groundId, UserDetails userDetails);

    TargetDto.List.Response targetList(Integer groundId);

    TargetDto.Detail.Response targetDetail(Integer groundId, String targetId);

    TargetDto.Delete.Response targetDelete(Integer groundId, String targetId);

    TargetDto.Update.Response targetUpdate(TargetDto.Update.Request request, String targetId, UserDetails userDetails);

    TargetDto.Tree.Response Tree(Integer groundId);

    TargetDto.Title.Response targetTitle(TargetDto.Title.Request request, String targetId, UserDetails userDetails);
}
