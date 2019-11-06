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
    Button[] buttons = new Button[4];


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttons[0] = findViewById(R.id.toCalc);
        buttons[1] = findViewById(R.id.toOffer);
        buttons[2] = findViewById(R.id.toOption);
        buttons[3] = findViewById(R.id.toStatis);

        buttons[0].setOnClickListener((view)->{
            Intent t=new Intent(MainActivity.this, HistoryCreateActivity.class);
            t.putExtra(getString(R.string.ModeIntentKey), HistoryManaging.SELL);
            startActivity(t);
        });
        buttons[1].setOnClickListener((view)->{
            Intent t=new Intent(MainActivity.this, StockListActivity.class);
            startActivity(t);
        });
        buttons[2].setOnClickListener((view)->{
            Intent t=new Intent(MainActivity.this, OptionActivity.class);
            //t.putExtra(getString(R.string.ModeIntentKey), HistoryManaging.DELIVERY);
            startActivity(t);
        });
        buttons[3].setOnClickListener((view)->{
            Intent t=new Intent(MainActivity.this, StatisticsActivity.class);
            //t.putExtra(getString(R.string.ModeIntentKey), HistoryManaging.DELIVERY);
            startActivity(t);
        });
    }
}
