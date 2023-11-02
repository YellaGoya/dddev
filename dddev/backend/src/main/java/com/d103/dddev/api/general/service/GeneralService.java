package com.d103.dddev.api.general.service;

import com.d103.dddev.api.general.collection.General1;
import com.d103.dddev.api.general.collection.General2;
import com.d103.dddev.api.general.repository.dto.General1InsertDto;
import com.d103.dddev.api.general.repository.dto.General2InsertDto;
import com.d103.dddev.api.general.repository.dto.General2MoveDto;
import com.d103.dddev.api.general.repository.dto.GeneralUpdateDto;

import java.util.List;

public interface GeneralService {
    General1 insertGeneral1(int groundId, General1InsertDto general1InsertDto);
    General2 insertGeneral2(int groundId, General2InsertDto general2InsertDto);
    General1 getGeneral1(int groundId, String generalId);
    General2 getGeneral2(int groundId, String generalId);
    General1 updateGeneral1(int groundId, GeneralUpdateDto generalUpdateDto);
    General2 updateGeneral2(int groundId, GeneralUpdateDto generalUpdateDto);
    General2 moveGeneral2(int groundId, General2MoveDto general2MoveDto);
    void deleteGeneral1(int groundId, String generalId);
    void deleteGeneral2(int groundId, String generalId);

}
