package com.scoregroup.androidpos.HistoryManagingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data.HistoryItem;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.HistoryItemAdapter;
import com.scoregroup.androidpos.MainActivity;
import com.scoregroup.androidpos.PaymentActivity;
import com.scoregroup.androidpos.R;

import java.util.ArrayList;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class HistoryCreateActivity extends AppCompatActivity {
    private static final int
            ADD = 0, PLUS = 1, MINUS = 2, MULTIPLY = 3, DIVIDE = 4, DELETE = 5, CANCEL = 6, ACC = 7, DCC = 8;
    private static final String[] symbols = new String[]{
            "", "+", "-", "×", "÷", "DEL", "CAN"
    };
    private final int[] numPadKeys =
            new int[]{R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
                    R.id.doubleO, R.id.plus, R.id.minus, R.id.multiply, R.id.divide, R.id.enter, R.id.del, R.id.cancel, R.id.add1, R.id.add2};

    public static int GetLayoutId(int mode) {
        switch (mode) {
            case DELIVERY:
                return R.layout.activity_delivery_history_creating;
            case SELL:
            default:
                return R.layout.activity_sell_history_creating;
        }
    }

    private int status;
    private String value = "";
    private int mode;
    private Intent receivePack;
    private TextView calcStatus;
    private ListView scrollArea;
    private ArrayList<HistoryItem> itemList;
    private Button payButton,historyButton;
    private int selected=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivePack = getIntent();

        mode = receivePack.getIntExtra(getString(R.string.ModeIntentKey), SELL);
        setContentView(GetLayoutId(mode));
        scrollArea=findViewById(R.id.scrollArea);
        calcStatus = findViewById(R.id.nowStatus);
        for (int i = 0; i < numPadKeys.length; i++) {
            Button numButton = findViewById(numPadKeys[i]);
            numButton.setOnClickListener((view) -> ProcessNumpadButton(view));
        }
        itemList=new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            itemList.add(new HistoryItem("키"+i,"이름"+i,100,i));
        }
        scrollArea.setAdapter(new HistoryItemAdapter(itemList));
        payButton=findViewById(R.id.createButton);
        payButton.setOnClickListener((view)->{
            String newKey="새로운 키";
            //TODO DB로 전송 후 새로 생긴 이벤트 키 받기(newKey)
            if(newKey==null){
                Log.e("POS","DB registering Failed");
            }else {
                if(mode==SELL){
                    //TODO 결재 액티비티로 변경
                    Intent t = new Intent(HistoryCreateActivity.this, PaymentActivity.class);

                    t.putExtra(getString(R.string.ModeIntentKey), mode);
                    t.putExtra(getString(R.string.EventIntentKey), newKey);
                    startActivity(t);
                }else if(mode==DELIVERY){
                    //TODO 액티비티 나가기
                    finish();
                }

            }
        });
        if(mode==SELL){
            historyButton=findViewById(R.id.historyButton);
            historyButton.setOnClickListener((view)->{
                Intent t=new Intent(HistoryCreateActivity.this,HistoryListActivity.class);
                t.putExtra(getString(R.string.ModeIntentKey),mode);
                startActivity(t);
            });
        }
    }

    private void refreshCalcStatus() {
        calcStatus.setText(symbols[status] + value);
    }

    private void ProcessNumpadButton(View view) {
        switch (view.getId()) {
            case R.id.one:
                value += 1;
                break;
            case R.id.two:
                value += 2;
                break;
            case R.id.three:
                value += 3;
                break;
            case R.id.four:
                value += 4;
                break;
            case R.id.five:
                value += 5;
                break;
            case R.id.six:
                value += 6;
                break;
            case R.id.seven:
                value += 7;
                break;
            case R.id.eight:
                value += 8;
                break;
            case R.id.nine:
                value += 9;
                break;
            case R.id.zero:
                value += 0;
                break;
            case R.id.doubleO:
                value += "00";
                break;
            case R.id.plus:
                if (status == ADD) status = PLUS;
                else if (status == PLUS) {
                    status = ACC;
                } else if (status == ACC) {
                    value = String.valueOf(Integer.parseInt(value) + 1);
                }
                break;
            case R.id.minus:
                break;
            case R.id.multiply:
                break;
            case R.id.divide:
                break;
            case R.id.enter:
                break;
            case R.id.cancel:
                if (status == ACC) {

                }
                break;
            case R.id.del:
                break;
            case R.id.add1:
                break;
            case R.id.add2:
                break;
            default:
                Log.e("POS", "Unavailable button Id");
                break;
        }
        refreshCalcStatus();
    }


}
