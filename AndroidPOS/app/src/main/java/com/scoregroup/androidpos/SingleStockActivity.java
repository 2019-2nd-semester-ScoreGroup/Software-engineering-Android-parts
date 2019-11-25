package com.scoregroup.androidpos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.BitSet;

public class SingleStockActivity extends AppCompatActivity {
    final int RESULT_OK = 100;
    final int RESULT_STORE = 0;
    final int RESULT_CANCLED = 50;
    final int REQUEST_BARCODE = 1;
    final int REQUEST_NAME = 2;
    final int REQUEST_PRICE=3;
    private ListView StockListView = null;
    ArrayList<StockListActivity.itemsale> oData = new ArrayList<>();

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.singlestock);
        Intent intent = getIntent();

        EditText s_p = (EditText) findViewById(R.id.modifyprice);
        EditText s_n = (EditText) findViewById(R.id.modifyname);
        EditText s_b = (EditText) findViewById(R.id.modifybarcode);

        StockListActivity.itemsale key = intent.getParcelableExtra("nextkey");
        StockListActivity.itemsale price = intent.getParcelableExtra("nextprice");
        StockListActivity.itemsale Name = intent.getParcelableExtra("nextName");
        StockListActivity.itemsale Count = intent.getParcelableExtra("nextCount");

        Button exitButton = findViewById(R.id.goback);
        Button nameButton = findViewById(R.id.namebutton);
        Button priceButton = findViewById(R.id.pricebutton);
        Button barcodeButton = findViewById(R.id.barcodebutton);

        nameButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendData.modified_name = s_n.getText().toString();
                intent.putExtra("Result_Name",s_n.getText().toString());
                setResult(REQUEST_NAME,intent);
            }

        });
        priceButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendData.modified_price= s_p.getText().toString();
                intent.putExtra("Result_Price", s_p.getText().toString());
                setResult(REQUEST_PRICE,intent);
            }

        });
        barcodeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendData.modified_barcode = s_b.getText().toString();
                intent.putExtra("Result_Barcode", s_b.getText().toString());
                setResult(REQUEST_BARCODE,intent);
            }


        });        ;;


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static class sendData{
        public static String modified_count;
        public static String modified_barcode;
        public static String  modified_name;
        public static String modified_price;

    }

}
