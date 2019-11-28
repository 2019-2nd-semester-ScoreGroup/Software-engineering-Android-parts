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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.BitSet;

public class SingleStockActivity extends AppCompatActivity {
    final int RESULT_OK = 100;
    final int RESULT_STORE = 0;
    final int RESULT_DELETE= 10;
    final int RESULT_CANCLED = 50;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.singlestock);
        Intent intent = getIntent();

        EditText s_p = (EditText) findViewById(R.id.modifyprice);
        EditText s_n = (EditText) findViewById(R.id.modifyname);
        EditText s_b = (EditText) findViewById(R.id.modifybarcode);
        TextView Title=(TextView) findViewById(R.id.productname);
        String product_name = intent.getStringExtra("nextName");
        String product_price = intent.getStringExtra("nextprice");
        String product_barcode= intent.getStringExtra("nextkey");
        String Count= intent.getStringExtra("nextCount");

        Title.setText(product_name);



        Button OKButton = findViewById(R.id.btnok);
        Button CancelButton = findViewById(R.id.btncancel);
        Button DeleteButton = findViewById(R.id.delete);
        Button AddButton = findViewById(R.id.add);

        OKButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();

            }

        });
        CancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }

        });
        AddButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
        DeleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
// TODO 버튼들 클릭했을때 서버와 통신

    }

    public  class sendData{
        public  String modified_count;
        public  String modified_barcode;
        public  String  modified_name;
        public  String modified_price;

    }

}