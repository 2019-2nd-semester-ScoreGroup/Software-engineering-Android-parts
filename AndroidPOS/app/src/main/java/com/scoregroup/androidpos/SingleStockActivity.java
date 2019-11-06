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
    final int _REQ = 100;
    final int RESULT_STORE = 0;
    final int RESULT_CANCLED = 50;
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


    }

    public class sendData{
        public String modified_Count;
        public String modified_Code;
        public String modified_Name;
        public String modified_Price;

    }

}
