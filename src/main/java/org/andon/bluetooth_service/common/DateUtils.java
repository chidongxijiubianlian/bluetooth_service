package org.andon.bluetooth_service.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Date getNow()
    {
        return new Date();
    }

    public static String getDateTimeStr(Date date) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return  sdf.format(date);
    }
}
