package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data;

import java.util.ArrayList;

public class HistoryEvent {
    private String key, dateTime;

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    private int totalPrice;

    public HistoryEvent(String key, String dateTime, int totalPrice) {
        this.key = key;
        this.dateTime = dateTime;
        this.totalPrice = totalPrice;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
