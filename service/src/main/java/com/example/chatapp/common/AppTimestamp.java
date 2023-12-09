package com.example.chatapp.common;

import java.sql.Timestamp;
import java.util.Calendar;

public class AppTimestamp extends Timestamp {

    public AppTimestamp(long time) {
        super(time);
    }

    public static AppTimestamp newInstance(){
        var now = Calendar.getInstance().getTimeInMillis();
        return new AppTimestamp(now);
    }
}
