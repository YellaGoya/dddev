package com.d103.dddev.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormat {

    public static LocalDateTime getCurrentTime(){
        LocalDateTime curTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        curTime.format(formatter);
        return curTime;
    }

}
