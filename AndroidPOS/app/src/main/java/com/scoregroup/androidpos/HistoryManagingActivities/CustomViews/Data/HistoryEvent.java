package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class HistoryEvent {
    private int totalPrice, mode;
    private String key, dateTime;

    public int getTotalPrice() {
        if(mode == SELL)
            return totalPrice * -1;
        else
            return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public HistoryEvent(int mode, String key, String dateTime, int totalPrice) {
        this.mode = mode;
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
