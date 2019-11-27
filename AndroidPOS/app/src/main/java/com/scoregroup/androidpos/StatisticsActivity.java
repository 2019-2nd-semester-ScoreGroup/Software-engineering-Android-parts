package com.scoregroup.androidpos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scoregroup.androidpos.Client.ClientManger;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class StatisticsActivity extends AppCompatActivity {
    ClientManger cm = ClientManger.getInstance();
    private ListView SaleListView = null;
    Button buttons[] = new Button[2];
    TextView texts[] = new TextView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        buttons[0] = findViewById(R.id.Confirm);
        buttons[1] = findViewById(R.id.ExitButton);
        texts[0] = findViewById(R.id.S_date);
        texts[1] = findViewById(R.id.S_money);
        texts[2] = findViewById(R.id.S_detail);

        buttons[0].setOnClickListener(view -> {
            texts[0].setText("날짜");
            texts[1].setText("매출");
            texts[2].setText("비고");
            sales_list();
            Toast.makeText(getApplicationContext(), "검색완료!", Toast.LENGTH_LONG).show();
        });

        buttons[1].setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
        });
    }

    public void sales_list(){ // DB데이터로 어댑터와 리스트뷰 연결
        String ackMsg;

        ArrayList<itemsale> sData = new ArrayList<>();

        ackMsg = cm.getDB("sale");

        StringTokenizer stringTokenizer = new StringTokenizer(ackMsg, ",");
        while(stringTokenizer.hasMoreTokens()){
            String line = stringTokenizer.nextToken();
            StringTokenizer lineTokenizer = new StringTokenizer(line, " ");
            itemsale item  = new itemsale();

            String parsedAckMsg = lineTokenizer.nextToken();
            item.Date = parsedAckMsg;

            lineTokenizer.hasMoreTokens();
            item.Money = parsedAckMsg;

            sData.add(item);
        }
        SaleListView = (ListView)findViewById(R.id.salelist);
        ListAdapter sales_Adapter = new ListAdapter(sData);
        SaleListView.setAdapter(sales_Adapter);
    }

    public class itemsale{ // 리스트뷰 용 매출통계 데이터 클래스
        public String Date;
        public String Money;
    }

    public class ListAdapter extends BaseAdapter // 커스텀 리스트뷰 어댑터 구현
    {
        LayoutInflater inflater = null;
        private ArrayList<itemsale> SaleData = null;
        private int nListCnt = 0;

        public ListAdapter(ArrayList<itemsale> sData)
        {
            SaleData = sData;
            nListCnt = SaleData.size();
        }

        @Override
        public int getCount()
        {
            Log.i("TAG", "getCount");
            return nListCnt;
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                final Context context = parent.getContext();
                if (inflater == null)
                {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }
                convertView = inflater.inflate(R.layout.simple_sale_list, parent, false);
            }

            TextView oTextDate = (TextView) convertView.findViewById(R.id.date);
            TextView oTextMoney = (TextView) convertView.findViewById(R.id.sale);

            oTextDate.setText(SaleData.get(position).Date);
            oTextMoney.setText(SaleData.get(position).Money);
            return convertView;
        }
    }
}
