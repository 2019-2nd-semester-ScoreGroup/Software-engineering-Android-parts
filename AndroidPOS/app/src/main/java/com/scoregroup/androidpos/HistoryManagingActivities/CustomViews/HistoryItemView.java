package com.scoregroup.androidpos.HistoryManagingActivities.CustomViews;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scoregroup.androidpos.R;

public class HistoryItemView extends LinearLayout {
    private Context context;
    private TextView name, amount, price;
    private String nameValue;
    private int amountValue, pricePerItem;

    public HistoryItemView(Context context) {
        super(context);
        initialize();
    }

    public void setName(String newText) {
        nameValue = newText;
        refreshViewData();
    }

    public void setAmount(int newVal) {
        amountValue = newVal;
        refreshViewData();
    }

    public void setPricePerItem(int newVal) {
        pricePerItem = newVal;
        refreshViewData();
    }

    private void refreshViewData() {
        name.setText(nameValue);
        amount.setText(context.getString(R.string.empty) + amountValue);
        price.setText(context.getString(R.string.empty) + (amountValue * pricePerItem));
    }

    public void initialize() {
        context = getContext();
        inflate(context, R.layout.view_history_item_view, this);
        name = this.findViewById(R.id.name);
        amount = this.findViewById(R.id.amount);
        price = this.findViewById(R.id.price);
    }

    public void setData(String name, int amount, int pricePerItem) {
        setName(name);
        setAmount(amount);
        setPricePerItem(pricePerItem);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        super.onLayout(b, i, i1, i2, i3);
    }
}
