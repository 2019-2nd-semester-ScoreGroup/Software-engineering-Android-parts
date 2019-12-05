package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class HistoryEvent {
    private int totalPrice, mode;
    private String key, dateTime;

    public int getTotalPrice() {
        if(mode == SELL)
            return Math.abs(totalPrice);
        else if(mode == DELIVERY)
            return totalPrice;
        else
            return 99999; //debug 용
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
