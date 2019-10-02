package com.scoregroup.androidpos;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //과제용 임시코드
    Button[] buttons = new Button[3];
    TextView title;
    public boolean onTouchForAssignment(View view, MotionEvent motionEvent) {
        return false;
    }
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
        for(Button button:buttons){
            button.setOnTouchListener((View view,MotionEvent motionEvent)->{
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        title.setText(((Button)view).getText());
                        break;
                    case MotionEvent.ACTION_UP:
                        title.setText(getString(R.string.app_name));
                        break;
                }
                return false;
            });

        }
        //과제용 임시코드 끝


    }

}
