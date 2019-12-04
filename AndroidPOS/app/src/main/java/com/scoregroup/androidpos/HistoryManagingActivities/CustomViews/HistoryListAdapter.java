package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data.HistoryEvent;
import com.scoregroup.androidpos.HistoryManagingActivities.HistoryListActivity;
import com.scoregroup.androidpos.HistoryManagingActivities.SingleHistoryActivity;
import com.scoregroup.androidpos.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class HistoryListAdapter extends BaseAdapter {
    private ArrayList<HistoryEvent> data;
    private DecimalFormat Cash_format = new DecimalFormat("###,###,###");

    public HistoryListAdapter(ArrayList<HistoryEvent> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return (i >= 0 && i < getCount()) ? data.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return (i >= 0 && i < getCount()) ? i : -1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final HistoryListActivity context = (HistoryListActivity) viewGroup.getContext();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_history_list, viewGroup, false);
        }
        HistoryEvent nowData=data.get(i);
        TextView keyCode = view.findViewById(R.id.keyCode);
        TextView dateTime = view.findViewById(R.id.dateTime);
        TextView price = view.findViewById(R.id.price);

        keyCode.setText(nowData.getKey());
        dateTime.setText(nowData.getDateTime());
        price.setText(context.getString(R.string.empty)+Cash_format.format(nowData.getTotalPrice()));

        Button detailButton = view.findViewById(R.id.detailButton);
        detailButton.setOnClickListener((button) -> {
            Intent t = new Intent(context, SingleHistoryActivity.class);
            t.putExtra(context.getString(R.string.ModeIntentKey), context.mode);
            t.putExtra(context.getString(R.string.EventIntentKey), keyCode.getText().toString());
            t.putExtra(context.getString(R.string.TimeIntentKey), dateTime.getText().toString());
            context.startActivity(t);
        });
        return view;


    }
}
