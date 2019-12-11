package com.scoregroup.androidpos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.scoregroup.androidpos.Client.Client;
import com.scoregroup.androidpos.Client.ClientLoading;
import com.scoregroup.androidpos.Client.ClientManger;

import java.util.ArrayList;
import java.util.BitSet;

public class SingleStockActivity extends AppCompatActivity {

    ClientManger cm = ClientManger.getInstance(this);
    private ClientLoading task;
    private static Context context = null;
    public String Data;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.singlestock);
        Intent intent = getIntent();
        sendData oItem = new sendData();

        EditText s_p = (EditText) findViewById(R.id.modifyprice);
        EditText s_n = (EditText) findViewById(R.id.modifyname);
        EditText s_b = (EditText) findViewById(R.id.modifybarcode);
        String product_name = intent.getStringExtra("nextName");
        String product_barcode= intent.getStringExtra("nextKey");
        String product_price = intent.getStringExtra("nextPrice");
        TextView Title=(TextView) findViewById(R.id.productname);
        Title.getText().toString();
        Title.setText(product_name);
        s_n.setText(product_name);
        s_b.setText(product_barcode);
        s_p.setText(product_price);
        Button OKButton = findViewById(R.id.btnok);
        Button CancelButton = findViewById(R.id.btncancel);
        task = new ClientLoading(this);

        OKButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oItem.modified_price = s_p.getText().toString();
                oItem.modified_barcode = s_b.getText().toString();
                oItem.modified_name = s_n.getText().toString();
                long newkey = Long.valueOf(oItem.modified_price);
                Client c = cm.getDB(String.format("editStock %s %s %s",oItem.modified_barcode,oItem.modified_name,oItem.modified_price));
                Log.i("pnb", "바코드" + oItem.modified_barcode + " 이름 " + oItem.modified_name + " 가격  " + oItem.modified_price);
                Title.setText(oItem.modified_name);
                task.show();
                c.setOnReceiveListener((client) -> {
                    Data = client.getData();
                    task.dismiss();
                }).send();
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

    }
    protected  class sendData{
        public  String modified_count;
        public  String modified_barcode;
        public  String  modified_name;
        public  String modified_price;
    }
}