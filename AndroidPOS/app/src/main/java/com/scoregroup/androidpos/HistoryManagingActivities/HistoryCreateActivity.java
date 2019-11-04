package com.scoregroup.androidpos.HistoryManagingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.scoregroup.androidpos.R;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class HistoryCreateActivity extends AppCompatActivity {


    public static int GetLayoutId(int mode) {
        switch (mode) {
            case DELIVERY:
                return R.layout.activity_delivery_history_creating;
            case SELL:
            default:
                return R.layout.activity_sell_history_creating;
        }
    }
    private int mode;
    private Intent receivePack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivePack=getIntent();
        mode=receivePack.getIntExtra(getString(R.string.ModeIntentKey), SELL);
        setContentView(GetLayoutId(mode));

    }

}
