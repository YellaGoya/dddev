package com.d103.dddev.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MongoTime {

    public static LocalDateTime convert(LocalDateTime localDateTime){
        return localDateTime.minusHours(9);
    }

}
