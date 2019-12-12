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

import static com.scoregroup.androidpos.Client.Client.Diff;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY_ADD;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY_CHANGE;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class HistoryListActivity extends AppCompatActivity {
    ClientManger cm = ClientManger.getInstance(this);
    private ClientLoading task;
    private ArrayList<HistoryEvent> events;
    private Intent receivePack;
    private ListView listScrollArea;
    private Button createHistoryButton;
    private String Data;
    public int mode;
    public static int GetLayoutId(int mode) {
        switch (mode) {
            case DELIVERY_CHANGE:
            case DELIVERY_ADD:
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
        task.show();
        Client c = cm.getDB("getEventList" + Diff + mode);
        c.setOnReceiveListener((v)->{
            Data = v.getData();
            task.dismiss();
            view_list();
        }).send();
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
        Client c = cm.getDB("getEventList" + Diff + mode);
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

            if(Data == null){
                Toast.makeText(getApplicationContext(), "연결 실패", Toast.LENGTH_LONG).show();
                return;
            }
            // 데이터 추출
            StringTokenizer stringTokenizer = new StringTokenizer(Data, ",");
            while(stringTokenizer.hasMoreTokens()){
                String parsedAckMsg = "";
                String line = stringTokenizer.nextToken();
                StringTokenizer lineTokenizer = new StringTokenizer(line, Diff);

                if(lineTokenizer.hasMoreTokens())
                    parsedAckMsg = lineTokenizer.nextToken();
                String Key = parsedAckMsg;

                if(lineTokenizer.hasMoreTokens())
                    parsedAckMsg = lineTokenizer.nextToken();
                String Time = parsedAckMsg;

                if(lineTokenizer.hasMoreTokens())
                    parsedAckMsg = lineTokenizer.nextToken();
                Time = Time.concat(" " + parsedAckMsg);

                if(lineTokenizer.hasMoreTokens())
                    parsedAckMsg = lineTokenizer.nextToken();
                String Price = parsedAckMsg;

                events.add(new HistoryEvent(mode, Key , Time.substring(0, 18), Integer.parseInt(Price)));
            }
            HistoryListAdapter adapter = new HistoryListAdapter(events);
            listScrollArea.setAdapter(adapter);
        });
    }
}
