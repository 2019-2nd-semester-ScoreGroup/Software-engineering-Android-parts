package com.scoregroup.androidpos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scoregroup.androidpos.HistoryManagingActivities.HistoryCreateActivity;
import com.scoregroup.androidpos.HistoryManagingActivities.HistoryListActivity;
import com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging;

public class MainActivity extends AppCompatActivity {
    //과제용 임시코드
    Button[] buttons = new Button[3];
    TextView title;
    //과제용 임시코드 끝

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //과제용 임시코드

        buttons[0] = findViewById(R.id.toCalc);
        buttons[1] = findViewById(R.id.toOffer);
        buttons[2] = findViewById(R.id.toOption);
        title=findViewById(R.id.titleTextView);
        buttons[0].setOnClickListener((view)->{
            Intent t=new Intent(MainActivity.this, HistoryListActivity.class);
            t.putExtra(getString(R.string.ModeIntentKey), HistoryManaging.SELL);
            startActivity(t);
        });
        buttons[1].setOnClickListener((view)->{
            Intent t=new Intent(MainActivity.this, HistoryListActivity.class);
            t.putExtra(getString(R.string.ModeIntentKey), HistoryManaging.DELIVERY);
            startActivity(t);
        });
        buttons[2].setOnClickListener((view)->{
           Intent t=new Intent(MainActivity.this, HistoryCreateActivity.class);
            t.putExtra(getString(R.string.ModeIntentKey),HistoryManaging.SELL);
            startActivity(t);
        });

    }

}
