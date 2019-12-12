package com.scoregroup.androidpos.HistoryManagingActivities;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

import static com.scoregroup.androidpos.Client.Client.Diff;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class SingleHistoryActivity extends AppCompatActivity {
    ClientManger cm = ClientManger.getInstance(this);
    private ClientLoading task;
    private Intent receivePack;
    private ListView listScrollArea;
    private int mode;
    private String  time, Data;
    private long eventKey;
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
        eventKey = receivePack.getLongExtra(getString(R.string.EventIntentKey),0);
        time = receivePack.getStringExtra(getString(R.string.TimeIntentKey));
        setContentView(GetLayoutId(mode));

        keyView = findViewById(R.id.keyNumber);
        dateView = findViewById(R.id.dateTime);
        totalPrice = findViewById(R.id.totalPrice);
        cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener((view)->{
            cm.getDB("tryChangeEvent"+Diff+eventKey+Diff+"1").setOnReceiveListener((client)->{
                if(!client.isReceived()){
                    Log.i("Network","Network Failed");
                    return;
                }
                boolean result=Boolean.valueOf(client.getData());
                if(result){
                    finish();
                }else{
                    runOnUiThread(()->Toast.makeText(this,"연결 실패",Toast.LENGTH_SHORT).show());
                }
            }).send();
        });
        listView = new ArrayList<>();
        listScrollArea = findViewById(R.id.scrollArea);
        adapter=new HistoryItemAdapter(listView);
        adapter.setSelectable(false);
        listScrollArea.setAdapter(adapter);
        task = new ClientLoading(this);
        task.show();
        cm.getDB("getEvent"+Diff + eventKey).setOnReceiveListener((v) -> {
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
            keyView.setText(""+eventKey);
            dateView.setText(time);


            //Data=" DELIVERY_2019-12-05 14:40:31.0_delivering_1_1_2_6,2_1_2_7,3_1_2_8,4_1_2_9,";
            String memoText=Data.split(Diff)[2];
            Data=Data.substring(Data.indexOf(memoText)+memoText.length()+1);
            String[] changes=Data.split(",");

            for(String change :changes){
                String[] word=change.split(Diff);
                HistoryItem temp=new HistoryItem(word[0],"NaN",-1,0);
                temp.setAmount(Integer.valueOf(word[1]));

                listView.add(temp);
                ClientManger.getInstance(this).getDB("getStock"+Diff+word[0]).setOnReceiveListener((client)->{
                    if(!client.isReceived())return;
                    String[] msgs=client.getData().split(Diff);
                    HistoryItem t=findItemByKey(msgs[0]);
                    t.setName(msgs[1]);
                    t.setPricePerItem(Integer.valueOf(msgs[2]));
                    t.setAmount(t.getAmount()*(mode==SELL?-1:1));
                    synchronized (this){
                        totalPriceData+=t.getPricePerItem()*t.getAmount();
                    }
                    runOnUiThread(()->{
                        totalPrice.setText("\t총 가격 : " + getString(R.string.empty) + totalPriceData);
                        adapter.notifyDataSetChanged();
                    });

                }).send();

            }


        });
    }
    private HistoryItem findItemByKey(String key){
        for(HistoryItem t : listView){
            if(t.getKey().equals(key))return t;
        }
        return null;
    }

}
