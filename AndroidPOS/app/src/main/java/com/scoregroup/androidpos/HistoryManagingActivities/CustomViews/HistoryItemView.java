package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scoregroup.androidpos.R;

public class HistoryItemView extends LinearLayout {
    private Context context;
    private TextView name,amount,price;
    public HistoryItemView(Context context) {
        super(context);
        initialize();
    }
    public void initialize(){
        context=getContext();
        inflate(context, R.layout.view_history_item_view,this);
        name=this.findViewById(R.id.name);
        amount=this.findViewById(R.id.amount);
        price=this.findViewById(R.id.price);
    }
    public void setData(String name,int amount,int pricePerItem){
        this.name.setText(name);
        this.amount.setText(""+amount);
        price.setText(""+amount*pricePerItem);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        super.onLayout(b,i,i1,i2,i3);
    }
}
