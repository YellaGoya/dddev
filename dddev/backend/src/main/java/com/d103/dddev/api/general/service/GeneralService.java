package com.d103.dddev.api.general.service;

import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.repository.dto.requestDto.*;
import com.d103.dddev.api.general.repository.dto.responseDto.GeneralResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.InvalidAttributeValueException;
import java.util.List;

public interface GeneralService {
    General insertGeneral(int groundId, GeneralInsertOneDto generalInsertOneDto, UserDetails userDetails) throws InvalidAttributeValueException;
    List<General> insertGeneralsWithTitles(int groundId, GeneralInsertManyDto generalInsertManyDto, UserDetails userDetails);
    General getGeneral(int groundId, String generalId);
    List<GeneralResponseDto> getStep1Generals(int groundId);
    List<General> getStep2Generals(int groundId);
    General updateGeneral(int groundId, String generalId, GeneralUpdateDto generalUpdateDto, UserDetails userDetails);

    General moveGeneral(int groundId, String generalId, GeneralMoveDto GeneralMoveDto) throws InvalidAttributeValueException;
    void deleteGeneral(int groundId, String generalId);

}
