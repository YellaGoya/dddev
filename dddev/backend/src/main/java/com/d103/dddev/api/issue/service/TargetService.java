package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.issue.model.dto.TargetDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface TargetService {

    TargetDto.Create.Response createTarget(String groundId, UserDetails userDetails);

    TargetDto.List.Response targetList(String groundId);

    TargetDto.Detail.Response targetDetail(String groundId, String targetId);

    TargetDto.Delete.Response targetDelete(String groundId, String targetId);

    TargetDto.Update.Response targetUpdate(TargetDto.Update.Request request, String targetId, UserDetails userDetails);
}
