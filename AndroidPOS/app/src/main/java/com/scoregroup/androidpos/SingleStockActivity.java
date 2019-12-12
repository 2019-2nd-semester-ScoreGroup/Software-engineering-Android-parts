package com.scoregroup.androidpos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scoregroup.androidpos.Client.Client;
import com.scoregroup.androidpos.Client.ClientLoading;
import com.scoregroup.androidpos.Client.ClientManger;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data.HistoryItem;
import com.scoregroup.androidpos.HistoryManagingActivities.HistoryCreateActivity;
import com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.BitSet;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY_ADD;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY_CHANGE;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;

public class SingleStockActivity extends AppCompatActivity {

    private ClientManger cm = ClientManger.getInstance(this);
    private ClientLoading task;
    private static Context context = null;
    private String Data, amount;
    private Intent intent, resultIntent;
    private int mode;
    private Button OKButton, addButton;
    private Client c;
    private EditText amountText;


    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        intent = getIntent();
        mode = intent.getIntExtra("Mode", DELIVERY_ADD);

        switch (mode) {
            case DELIVERY_ADD:
                setContentView(R.layout.addsinglestock);
                break;
            default:
                setContentView(R.layout.singlestock);
        }

        sendData oItem = new sendData();
        resultIntent = new Intent();

        EditText s_p = (EditText) findViewById(R.id.modifyprice);
        EditText s_n = (EditText) findViewById(R.id.modifyname);
        EditText s_b = (EditText) findViewById(R.id.modifybarcode);
        String product_name = intent.getStringExtra("nextName");
        String product_barcode = intent.getStringExtra("nextKey");
        String product_price = intent.getStringExtra("nextPrice");
        TextView Title = (TextView) findViewById(R.id.productname);
        Title.getText().toString();
        Title.setText(product_name);
        s_n.setText(product_name);
        s_b.setText(product_barcode);
        s_p.setText(product_price);

        Button CancelButton = findViewById(R.id.btncancel);
        task = new ClientLoading(this);

        if (mode == HistoryManaging.DELIVERY_CHANGE) {
            OKButton = findViewById(R.id.btnok);

            OKButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    oItem.modified_price = s_p.getText().toString().replace(",", "");
                    oItem.modified_barcode = s_b.getText().toString().replace(" ", "");
                    oItem.modified_name = s_n.getText().toString().replace(" ", "_");

                    if (oItem.modified_price.equals("") && oItem.modified_barcode.equals("") && oItem.modified_name.equals("")) {
                        setResult(RESULT_FIRST_USER, resultIntent);
                        finish();
                        return;
                    }

                    if (oItem.modified_name.equals("")) {
                        Toast.makeText(SingleStockActivity.this, "품명을 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (oItem.modified_price.equals("")) {
                        Toast.makeText(SingleStockActivity.this, "가격을 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (oItem.modified_barcode.equals("")) {
                        Toast.makeText(SingleStockActivity.this, "바코드(키)를 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long newkey = Long.valueOf(oItem.modified_price);
                    c = cm.getDB(String.format("editStock %s %s %s", oItem.modified_barcode, oItem.modified_name, oItem.modified_price));
                    Log.i("pnb", "바코드" + oItem.modified_barcode + " 이름 " + oItem.modified_name + " 가격  " + oItem.modified_price);
                    Title.setText(oItem.modified_name);
                    task.show();
                    c.setOnReceiveListener((client) -> {
                        Data = client.getData();
                        task.dismiss();

                        if (Boolean.parseBoolean(Data))
                            setResult(RESULT_OK, resultIntent);

                        else
                            setResult(RESULT_CANCELED, resultIntent);

                        finish();
                    }).send();
                }
            });
        } else {
            addButton = findViewById(R.id.btnok);
            amountText = findViewById(R.id.addStockAmountText);

            addButton.setOnClickListener((view) ->
            {
                oItem.modified_price = s_p.getText().toString().replace(",", "");
                oItem.modified_barcode = s_b.getText().toString().replace(" ", "");
                oItem.modified_name = s_n.getText().toString().replace(" ", "_");
                amount = amountText.getText().toString();

                if (oItem.modified_price.equals("") && oItem.modified_barcode.equals("") && oItem.modified_name.equals("")) {
                    setResult(RESULT_FIRST_USER, resultIntent);
                    finish();
                    return;
                }

                if (oItem.modified_name.equals("")) {
                    Toast.makeText(SingleStockActivity.this, "품명을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (oItem.modified_price.equals("")) {
                    Toast.makeText(SingleStockActivity.this, "가격을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (oItem.modified_barcode.equals("")) {
                    Toast.makeText(SingleStockActivity.this, "바코드(키)를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (amount.equals("")) {
                    Toast.makeText(SingleStockActivity.this, "수량을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                cm.getDB(String.format(String.format("editStock %s %s %s", oItem.modified_barcode, oItem.modified_name, oItem.modified_price)))
                        .setOnReceiveListener((client) -> {
                            if (!client.isReceived()) {
                                runOnUiThread(() -> {
                                    Toast.makeText(SingleStockActivity.this, "제품 추가 실패", Toast.LENGTH_SHORT).show();
                                });
                                return;
                            }
                            cm.getDB(String.format("addEvent %d %s %d %s", DELIVERY, Timestamp.valueOf(LocalDateTime.now().toString().replace('T', ' ')), 0, "addingStackForInitialize"))
                                    .setOnReceiveListener((clientEv) -> {
                                        if (!clientEv.isReceived()) {
                                            runOnUiThread(() -> {
                                                Toast.makeText(SingleStockActivity.this, "기록 추가 실패", Toast.LENGTH_SHORT).show();
                                            });
                                            return;
                                        }

                                        String newKey = clientEv.getData();
                                        long newKeyLong = Long.valueOf(newKey);
                                        cm.getDB(String.format("addChange %d %s %d", newKeyLong, oItem.modified_barcode, Integer.parseInt(amount))).send();

                                        finish();
                                    }).send();
                        }).send();
            });
        }


        CancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_FIRST_USER, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_FIRST_USER, resultIntent);
        finish();
    }


    protected class sendData {
        public String modified_count;
        public String modified_barcode;
        public String modified_name;
        public String modified_price;
    }
}