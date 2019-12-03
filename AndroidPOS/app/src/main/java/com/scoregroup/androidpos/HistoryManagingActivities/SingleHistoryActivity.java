package com.scoregroup.androidpos.HistoryManagingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scoregroup.androidpos.Client.Client;
import com.scoregroup.androidpos.Client.ClientLoading;
import com.scoregroup.androidpos.Client.ClientManger;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data.HistoryItem;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.HistoryItemAdapter;
import com.scoregroup.androidpos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class SingleHistoryActivity extends AppCompatActivity {
    ClientManger cm = ClientManger.getInstance();
    private ClientLoading task;
    private Intent receivePack;
    private ListView listScrollArea;
    private int mode;
    private String eventKey, time, Data;
    private ArrayList<HistoryItem> listView;
    private TextView keyView, dateView, totalPrice;
    private Button cancelButton;

    public static int GetLayoutId(int mode) {
        switch (mode) {
            case DELIVERY:
                return R.layout.activity_delivery_history_view;
            case SELL:
            default:
                return R.layout.activity_sell_history_view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivePack = getIntent();
        mode = receivePack.getIntExtra(getString(R.string.ModeIntentKey), SELL);
        eventKey = receivePack.getStringExtra(getString(R.string.EventIntentKey));
        time = receivePack.getStringExtra(getString(R.string.TimeIntentKey));
        setContentView(GetLayoutId(mode));

        keyView = findViewById(R.id.keyNumber);
        dateView = findViewById(R.id.dateTime);
        totalPrice = findViewById(R.id.totlaPrice);
        cancelButton=findViewById(R.id.cancel);
        cancelButton.setOnClickListener((view)->finish());

        listScrollArea = findViewById(R.id.scrollArea);
        task = new ClientLoading(this);
        task.show();
        Client c = cm.getDB("getEvent" + " " + eventKey);
        c.setOnReceiveListener((v)->{
            Data = v.getData();
            task.dismiss();
            view_list();
        }).send();
    }

    private void view_list(){ // DB데이터로 어댑터와 리스트뷰 연결
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(()->{
            keyView.setText(eventKey);
            dateView.setText(time);

            listView = new ArrayList<>();
            Data = "0 박카스 1000 20, 1 비타오백 1000 30, 2 새우깡 1500 10, 3 뭐시기 2000 10";
            int totalPriceData = 0;
            // 데이터가 널 값일 시 리턴
            if(Data == null){
                Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_LONG).show();
                return;
            }
            // 데이터 추출
            StringTokenizer stringTokenizer = new StringTokenizer(Data, ",");
            while(stringTokenizer.hasMoreTokens()){
                String line = stringTokenizer.nextToken();
                StringTokenizer lineTokenizer = new StringTokenizer(line, " ");

                lineTokenizer.hasMoreTokens();
                String parsedAckMsg = lineTokenizer.nextToken();
                String Key = parsedAckMsg;

                lineTokenizer.hasMoreTokens();
                parsedAckMsg = lineTokenizer.nextToken();
                String Name = parsedAckMsg;

                lineTokenizer.hasMoreTokens();
                parsedAckMsg = lineTokenizer.nextToken();
                String Price = parsedAckMsg;

                lineTokenizer.hasMoreTokens();
                parsedAckMsg = lineTokenizer.nextToken();
                String Amount = parsedAckMsg;

                totalPriceData += Integer.parseInt(Amount) * Integer.parseInt(Price);
                HistoryItem item = new HistoryItem(Key, Name, Integer.parseInt(Price), Integer.parseInt(Amount));
                listView.add(item);
            }
            totalPrice.setText(getString(R.string.empty) + totalPriceData);
            listScrollArea.setAdapter(new HistoryItemAdapter(listView));
        });
    }
}
