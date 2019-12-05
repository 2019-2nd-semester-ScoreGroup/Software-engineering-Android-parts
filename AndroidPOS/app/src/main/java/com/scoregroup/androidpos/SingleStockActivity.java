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

import com.scoregroup.androidpos.Client.ClientManger;

import java.util.ArrayList;
import java.util.BitSet;

public class SingleStockActivity extends AppCompatActivity {
    final int RESULT_OK = 100;
    final int RESULT_STORE = 0;
    final int RESULT_DELETE= 10;
    final int RESULT_CANCLED = 50;
    private ListView StockListView = null;
    ArrayList<StockListActivity.itemsale> oData = new ArrayList<>();
    private ClientManger clientManager;
    private EditText s_p;
    private EditText s_n;
    private EditText s_b;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.singlestock);
        Intent intent = getIntent();

        s_p = (EditText) findViewById(R.id.modifyprice);
        s_n = (EditText) findViewById(R.id.modifyname);
        s_b = (EditText) findViewById(R.id.modifybarcode);

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

    protected void sendData()
    {
        //TODO 석빈이한테 UI 변경 요소 물어보고 다시 작업
        String modified_Count;
        String modified_Code = s_b.getText().toString();
        String modified_Name = s_n.getText().toString();
        String modified_Price = s_p.getText().toString();;

        clientManager = ClientManger.getInstance();
        clientManager.getDB("editStock");
    }

}