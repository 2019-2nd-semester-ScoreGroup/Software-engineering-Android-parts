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

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {
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

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                texts[0].setText("날짜");
                texts[1].setText("매출");
                texts[2].setText("상세");
                sales_list();
                Toast.makeText(getApplicationContext(), "검색완료!", Toast.LENGTH_LONG).show();
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(in);
            }
        });
    }

    public void sales_list(){ // DB예시로 어댑터와 리스트뷰 연결
        String[] strDate = {"2017-01-03", "2017-01-04", "2017-01-05", "2017-01-06", "2017-01-07",
                "2017-01-08", "2017-01-09", "2017-01-10", "2017-01-11", "2017-01-12"};
        String[] strMoney = {"2,212,300", "2,008,100", "3,272,900", "1,942,000", "5,892,000",
                "2,683,490", "1,694,400", "4,112,900", "1,772,000", "6,102,100"};

        int nDatCnt=0;
        ArrayList<itemsale> oData = new ArrayList<>();

        for (int i=0; i<100; ++i)
        {
            itemsale oItem = new itemsale();
            oItem.Money = "\t" + strMoney[nDatCnt] + "원";
            oItem.Date = "\t" + strDate[nDatCnt++];
            oData.add(oItem);
            if (nDatCnt >= strDate.length) nDatCnt = 0;
        }

        SaleListView = (ListView)findViewById(R.id.salelist);
        ListAdapter oAdapter = new ListAdapter(oData);
        SaleListView.setAdapter(oAdapter);
    }

    public class itemsale{ // 리스트뷰 데이터 클래스
        public String Date;
        public String Money;
    }

    public class ListAdapter extends BaseAdapter // 커스텀 리스트뷰 어댑터 구현
    {
        LayoutInflater inflater = null;
        private ArrayList<itemsale> m_oData = null;
        private int nListCnt = 0;

        public ListAdapter(ArrayList<itemsale> _oData)
        {
            m_oData = _oData;
            nListCnt = m_oData.size();
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

            oTextDate.setText(m_oData.get(position).Date);
            oTextMoney.setText(m_oData.get(position).Money);
            return convertView;
        }
    }
}
