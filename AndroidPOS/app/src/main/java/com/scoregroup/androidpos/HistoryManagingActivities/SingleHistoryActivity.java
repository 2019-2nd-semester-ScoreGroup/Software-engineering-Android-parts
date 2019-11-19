package com.scoregroup.androidpos.HistoryManagingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data.HistoryItem;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.HistoryItemAdapter;
import com.scoregroup.androidpos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class SingleHistoryActivity extends AppCompatActivity {
    private Intent receivePack;
    private ListView listScrollArea;

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
    private ArrayList<HistoryItem> listView;
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
            HistoryItem item = new HistoryItem("키"+i,"이름"+i,100,i);
            listView.add(item);
            //TODO 총액계산
            totalPriceData += i * 100;
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
        listScrollArea.setAdapter(new HistoryItemAdapter(listView));

    }

}
