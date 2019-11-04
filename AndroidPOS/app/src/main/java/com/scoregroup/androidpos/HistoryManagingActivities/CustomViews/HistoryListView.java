package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scoregroup.androidpos.HistoryManagingActivities.HistoryListActivity;
import com.scoregroup.androidpos.HistoryManagingActivities.SingleHistoryActivity;
import com.scoregroup.androidpos.R;

public class HistoryListView extends LinearLayout {

    public HistoryListView(HistoryListActivity context) {
        super(context);
        initialize(context);
    }
    private HistoryListActivity context;
    private TextView keyCode, dateTime,price;
    private Button detailButton;
    public void initialize(HistoryListActivity context){
        this.context=context;
        inflate(context, R.layout.view_history_list,this);
        keyCode =this.findViewById(R.id.keyCode);
        dateTime =this.findViewById(R.id.dateTime);
        price=this.findViewById(R.id.price);
        detailButton=this.findViewById(R.id.detailButton);
        detailButton.setOnClickListener((view)->{
            Intent t=new Intent(context, SingleHistoryActivity.class);
            t.putExtra(context.getString(R.string.ModeIntentKey),context.mode);
            t.putExtra(context.getString(R.string.EventIntentKey),keyCode.getText().toString());
            context.startActivity(t);
        });

    }
    public void setData(String keyCode,String dateTime,int price){
        this.keyCode.setText(keyCode);
        this.dateTime.setText(dateTime);
        this.price.setText(""+price);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        super.onLayout(b,i,i1,i2,i3);
    }
}

