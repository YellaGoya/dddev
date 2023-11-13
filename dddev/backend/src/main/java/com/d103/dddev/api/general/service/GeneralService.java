package com.d103.dddev.api.general.service;

import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.repository.dto.requestDto.*;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralResponseDto;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralTitleResponseDto;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralTreeResponseDto;
import com.d103.dddev.api.user.repository.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface GeneralService {
    GeneralResponseDto insertGeneral(int groundId, GeneralInsertOneDto generalInsertOneDto, UserDto userDto) throws Exception;
    List<GeneralResponseDto> insertGeneralsWithTitles(int groundId, GeneralInsertManyDto generalInsertManyDto, UserDto userDto);
    General getGeneral(int groundId, String generalId) throws Exception;
    List<GeneralTreeResponseDto> getTreeGenerals(int groundId);
    List<GeneralTitleResponseDto> getStep1Generals(int groundId);
    List<General> getStep2Generals(int groundId);
    GeneralResponseDto updateGeneral(int groundId, String generalId, GeneralUpdateDto generalUpdateDto, UserDto userDto) throws Exception;

    General moveGeneral(int groundId, String generalId, GeneralMoveDto GeneralMoveDto) throws Exception;
    void deleteGeneral(int groundId, String generalId) throws Exception;
    General changeTemplate(int groundId, String generalId) throws Exception;
    General titleGeneral(int groundId, String generalId, GeneralTitleDto generalTitleDto, UserDetails userDetails) throws Exception;
}
