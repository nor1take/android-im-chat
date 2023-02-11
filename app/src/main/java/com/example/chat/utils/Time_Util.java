package com.example.chat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time_Util {
    public static String setTime(Date time1, Date time2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm");
        long t1 = time1.getTime();
        long t2 = time2.getTime();
        long d = (t1 - t2) / 1000;
        if (d < 30) return "刚刚";
        else if (d < 60) return (d + " 秒前");
        else if (d < 60 * 60) return (d / 60 + " 分钟前");
        else if (d < 60 * 60 * 24) return (d / 60 / 60 + " 小时前");
        else if (d < 60 * 60 * 24 * 2) return "昨天";
        else if (d < 60 * 60 * 24 * 3) return "前天";
        else if (d < 60 * 60 * 24 * 30) return (d / 60 / 60 / 24 + " 天前");
        else return dateFormat.format(time2.getTime());
    }
}
