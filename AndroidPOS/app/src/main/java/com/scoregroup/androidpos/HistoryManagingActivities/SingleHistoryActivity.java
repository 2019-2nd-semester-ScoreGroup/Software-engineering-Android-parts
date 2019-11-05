package com.scoregroup.androidpos.HistoryManagingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.HistoryItemView;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.HistoryListView;
import com.scoregroup.androidpos.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class SingleHistoryActivity extends AppCompatActivity {
    private Intent receivePack;
    private LinearLayout listScrollArea;

    public static int GetLayoutId(int mode) {
        switch (mode) {
            case DELIVERY:
                return R.layout.activity_delivery_history_view;
            case SELL:
            default:
                return R.layout.activity_sell_history_view;
        }
    }

    private int mode;
    private String eventKey;
    private ArrayList<HistoryItemView> listView;
    private TextView keyView, dateView, totalPrice;
    private Button cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivePack = getIntent();
        mode = receivePack.getIntExtra(getString(R.string.ModeIntentKey), SELL);
        eventKey = receivePack.getStringExtra(getString(R.string.EventIntentKey));
        setContentView(GetLayoutId(mode));
        listScrollArea = findViewById(R.id.scrollArea);
        listView = new ArrayList<>();
        int totalPriceData = 0;
        for (int i = 0; i < 100; i++) {
            HistoryItemView item = new HistoryItemView(this);
            listView.add(item);
            listScrollArea.addView(item);
            item.setData("키" + eventKey, i, i);
            //TODO 총액계산
            totalPriceData += i * i;
        }
        keyView = findViewById(R.id.keyNumber);
        keyView.setText(eventKey);
        //TODO 데이터 받아오기
        dateView = findViewById(R.id.dateTime);
        dateView.setText(new SimpleDateFormat("yyyy.mm.dd.hh.mm").format(Calendar.getInstance().getTime()));
        totalPrice = findViewById(R.id.totlaPrice);
        totalPrice.setText(getString(R.string.empty) + totalPriceData);
        cancelButton=findViewById(R.id.cancel);
        cancelButton.setOnClickListener((view)->finish());


    }

}
