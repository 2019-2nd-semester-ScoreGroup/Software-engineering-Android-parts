package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data;

public class HistoryItem {
    public String key,name;
    public int pricePerItem,amount;


    public HistoryItem(String key, String name, int pricePerItem, int amount) {
        this.key = key;
        this.name = name;
        this.pricePerItem = pricePerItem;
        this.amount = amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPricePerItem() {
        return pricePerItem;
    }

    public void setPricePerItem(int pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
