package com.scoregroup.androidpos.HistoryManagingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private HistoryItemAdapter adapter;
    private int totalPriceData=0;
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
        cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener((view) -> finish());
        listView = new ArrayList<>();
        listScrollArea = findViewById(R.id.scrollArea);
        adapter=new HistoryItemAdapter(listView);
        listScrollArea.setAdapter(adapter);
        task = new ClientLoading(this);
        task.show();
        Client c = cm.getDB("getEvent " + eventKey);
        c.setOnReceiveListener((v) -> {
            if (!v.isReceived()) {
                Log.e("CLIENT", "Failed to connect");
                runOnUiThread(()->{
                    Toast.makeText(SingleHistoryActivity.this, "연결 실패", Toast.LENGTH_SHORT).show();
                });

            }
            Data = v.getData();
            task.dismiss();
            view_list();
        }).send();
    }

    private void view_list() { // DB데이터로 어댑터와 리스트뷰 연결
        runOnUiThread(() -> {
            keyView.setText(eventKey);
            dateView.setText(time);


            Data="SELL 2019-12-04 10:58:55.0 Testing 1 12 6 1,2 31 5 2,3 23 4 3,4 86 3 2,5 10 1 5,6 23 1 6";
            String memoText=Data.split(" ")[3];
            Data=Data.substring(Data.indexOf(memoText)+memoText.length()+1);
            String[] changes=Data.split(",");

            for(String change :changes){
                String[] word=change.split(" ");
                HistoryItem temp=new HistoryItem(word[0],"NaN",-1,0);
                temp.setAmount(Integer.valueOf(word[1]));

                listView.add(temp);
                ClientManger.getInstance().getDB("getStock "+word[0]).setOnReceiveListener((client)->{
                    if(!client.isReceived())return;
                    String[] msgs=client.getData().split(" ");
                    HistoryItem t=findItemByKey(msgs[0]);
                    t.setName(msgs[1]);
                    t.setPricePerItem(Integer.valueOf(msgs[2]));
                    synchronized (this){
                        totalPriceData+=t.getPricePerItem()*t.getAmount();
                    }
                    runOnUiThread(()->totalPrice.setText(getString(R.string.empty) + totalPriceData));
                }).send();

            }

            adapter.notifyDataSetChanged();
        });
    }
    private HistoryItem findItemByKey(String key){
        for(HistoryItem t : listView){
            if(t.getKey().equals(key))return t;
        }
        return null;
    }

}
