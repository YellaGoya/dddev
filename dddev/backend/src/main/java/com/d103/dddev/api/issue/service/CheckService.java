package com.d103.dddev.api.issue.service;

import com.d103.dddev.api.issue.model.dto.CheckDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface CheckService {


    CheckDto.Create.Response createCheck(String groundId, CheckDto.Create.Request request, UserDetails userDetails);

    CheckDto.List.Response checkList(String groundId, String targetId);

    CheckDto.Detail.Response checkDetail(String groundId, String checkId);

    CheckDto.Delete.Response checkDelete(String groundId, String checkId);

    CheckDto.Update.Response checkUpdate(CheckDto.Update.Request request, String checkId, UserDetails userDetails);

    CheckDto.Connect.Response connectTarget(CheckDto.Connect.Request request, UserDetails userDetails);
}
