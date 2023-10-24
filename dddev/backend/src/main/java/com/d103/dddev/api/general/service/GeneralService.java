package com.d103.dddev.api.general.service;

import com.d103.dddev.api.general.collection.General;

public interface GeneralService {
    public void insertGeneral(General general);
    public General getGeneral(String name);
}
