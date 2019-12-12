package com.scoregroup.androidpos.HistoryManagingActivities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HistoryManaging {
    public static final int SELL=1;
    public static final int DELIVERY=2;
    public static final int DELIVERY_ADD=3;
    public static final int DELIVERY_CHANGE=4;
    public static String getNowTime(){
        return new SimpleDateFormat("yyyy.mm.dd.hh.mm").format(Calendar.getInstance().getTime());
    }
}
