package com.scoregroup.androidpos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class PaymentActivity extends AppCompatActivity {
    private Intent receiveIntent;
    private TextView currentPaymentState;

    /*
    받을 데이터 선언
    */

    private int mod;
    private int key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_process);

        currentPaymentState = (TextView)findViewById(R.id.textView2);
        receiveIntent = getIntent();

        /*
        전 액티비티에서 넘긴 값 저장
        */

        mod = receiveIntent.getIntExtra(getString(R.string.ModeIntentKey), SELL);
        key = receiveIntent.getIntExtra(getString(R.string.EventIntentKey), 0000000000000);




        /*
        기능 구현 전 1.5초 대기 후 전환
        */


        new Thread(()->{
            try
            {
                Thread.sleep(1500);
                currentPaymentState.append(".");
                Thread.sleep(1500);
                currentPaymentState.append(".");

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            finish();
        }).start();


    }
}
