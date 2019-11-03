package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews;

import android.content.Context;
import android.widget.LinearLayout;

import com.scoregroup.androidpos.R;

public class HistoryItemView extends LinearLayout {
    private Context context;
    public HistoryItemView(Context context) {
        super(context);
        Initialize();
    }
    public void Initialize(){
        context=getContext();
        inflate(context, R.layout.view_history_item_view,this);
        
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        super.onLayout(b,i,i1,i2,i3);
    }
}
