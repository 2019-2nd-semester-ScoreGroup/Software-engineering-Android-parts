package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scoregroup.androidpos.R;

public class ChangeHistoryListView extends LinearLayout {

    public ChangeHistoryListView(Context context) {
        super(context);
        initialize();
    }
    private Context context;
    private TextView keyCode, dateTime,price;
    public void initialize(){
        context=getContext();
        inflate(context, R.layout.view_history_list,this);
        keyCode =this.findViewById(R.id.name);
        dateTime =this.findViewById(R.id.amount);
        price=this.findViewById(R.id.price);
    }
    public void setData(String keyCode,String dateTime,int price){
        this.keyCode.setText(keyCode);
        this.dateTime.setText(dateTime);
        this.price.setText(price);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        super.onLayout(b,i,i1,i2,i3);
    }
}

