package com.starkexport.common;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AppUtil {
    private static SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String getUTCDate(int timestamp){
        Date date = new Date((long)timestamp*1000);
        sdfFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdfFormat.format(date);
    }
}
