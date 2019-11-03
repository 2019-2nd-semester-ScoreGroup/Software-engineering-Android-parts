package com.scoregroup.androidpos.HistoryManagingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.HistoryListView;
import com.scoregroup.androidpos.R;

import java.util.ArrayList;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class HistoryListActivity extends AppCompatActivity {
    private ArrayList<HistoryListView> listView;
    private Intent receivePack;
    private LinearLayout listScrollArea;

    public static int GetLayoutId(int mode) {
        switch (mode) {
            case DELIVERY:
                return R.layout.activity_delivery_history_list;
            case SELL:
            default:
                return R.layout.activity_sell_history_list;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivePack = getIntent();
        setContentView(GetLayoutId(receivePack.getIntExtra("mode", SELL)));
        listScrollArea=findViewById(R.id.ScrollArea);
        listView = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            HistoryListView item=new HistoryListView(this);
            listView.add(item);
            listScrollArea.addView(item);
            item.setData("키" + i, "날짜" + i, i);
        }
    }

}
