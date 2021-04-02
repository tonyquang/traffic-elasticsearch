package com.traffic.report.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class TimeUtil {
    private final String TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public Date getDateFromString(String strDate){
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+7"));
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getHour(String timeStamp){
        return String.valueOf(getDateFromString(timeStamp).getHours());
    }

}
