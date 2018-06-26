package org.andon.bluetooth_service.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date getNow() {
        return new Date();
    }

    public static String getDateTimeStr(Date date) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String forwardNow(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        //往前
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)-day);
        Date updateDate = calendar.getTime();
        return sdf.format(updateDate);

    }
}
