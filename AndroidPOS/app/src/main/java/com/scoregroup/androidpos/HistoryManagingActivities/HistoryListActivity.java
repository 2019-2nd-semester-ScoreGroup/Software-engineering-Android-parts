package com.scoregroup.androidpos.HistoryManagingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.scoregroup.androidpos.Client.Client;
import com.scoregroup.androidpos.Client.ClientLoading;
import com.scoregroup.androidpos.Client.ClientManger;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data.HistoryEvent;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.HistoryListAdapter;
import com.scoregroup.androidpos.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class HistoryListActivity extends AppCompatActivity {
    ClientManger cm = ClientManger.getInstance();
    private ClientLoading task;
    private ArrayList<HistoryEvent> events;
    private Intent receivePack;
    private ListView listScrollArea;
    private Button createHistoryButton;
    private String Data;
    public int mode;
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
    protected void onResume() {
        super.onResume();
        //HisotryEvent 새로고침
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivePack = getIntent();
        mode = receivePack.getIntExtra(getString(R.string.ModeIntentKey), SELL);
        setContentView(GetLayoutId(mode));

        listScrollArea = findViewById(R.id.scrollArea);
        task = new ClientLoading(this);
        task.show();
        Client c = cm.getDB("getEventList" + " " + mode);
        c.setOnReceiveListener((v)->{
            Data = v.getData();
            task.dismiss();
            view_list();
        }).send();

        if(mode == DELIVERY){
            createHistoryButton=findViewById(R.id.createButton);
            createHistoryButton.setOnClickListener((view)->{
                Intent t=new Intent(HistoryListActivity.this,HistoryCreateActivity.class);
                t.putExtra(getString(R.string.ModeIntentKey),mode);
                startActivity(t);
            });
        }
    }

    private void view_list(){ // DB데이터로 어댑터와 리스트뷰 연결
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(()->{
            events = new ArrayList<>();
            // 데이터가 널 값일 시 리턴
            Data = "0 2019.12.15 100000, 1 2019.12.16 200000, 2 2019.12.17 300000, 3 2019.12.18 400000";
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
                String Time = parsedAckMsg;

                lineTokenizer.hasMoreTokens();
                parsedAckMsg = lineTokenizer.nextToken();
                String Price = parsedAckMsg;

                events.add(new HistoryEvent("키" + Key , Time, Integer.parseInt(Price)));
            }
            HistoryListAdapter adapter = new HistoryListAdapter(events);
            listScrollArea.setAdapter(adapter);
        });
    }
}
