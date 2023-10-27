package com.d103.dddev.api.general.service;

import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.general.repository.GeneralRepository;
import com.d103.dddev.common.TimeFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneralServiceImpl implements GeneralService{

    private final GeneralRepository generalRepository;
    @Override
    public void insertGeneral(General general) {
        // create_at 추가하기
        generalRepository.save(general);
    }
    @Override
    public General getGeneral(String title){
        return generalRepository.findByTitle(title);
    }
    @Override
    public List<General> getGeneralList(){
        return generalRepository.findAll();
    }
}
