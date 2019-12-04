package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data.HistoryItem;
import com.scoregroup.androidpos.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HistoryItemAdapter extends BaseAdapter {
    ArrayList<HistoryItem> items;
    private DecimalFormat Cash_format = new DecimalFormat("###,###,###");
    int selected=-1;
    public HistoryItemAdapter(ArrayList<HistoryItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return (i >= 0 && i < getCount()) ? items.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return (i >= 0 && i < getCount()) ? i : -1;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context=viewGroup.getContext();
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_history_item_view, viewGroup, false);
        }
        TextView name = view.findViewById(R.id.name);
        TextView amount = view.findViewById(R.id.amount);
        TextView price = view.findViewById(R.id.price);

        HistoryItem item=items.get(i);
        view.setBackgroundColor(selected==i? Color.YELLOW:Color.GRAY);
        name.setText(item.getName());
        amount.setText(context.getString(R.string.empty) + item.getAmount());
        price.setText(context.getString(R.string.empty) + Cash_format.format(item.getAmount()*item.getPricePerItem()));
        view.setOnClickListener(v -> {
            HistoryItemAdapter.this.selected=i;
            HistoryItemAdapter.this.notifyDataSetChanged();
        });
        return view;
    }

    public void setSelected(int selected) {
        this.selected=selected;
    }

    public int getSelected() {
        return selected;
    }
}
