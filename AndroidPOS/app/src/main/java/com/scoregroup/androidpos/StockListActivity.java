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

import com.scoregroup.androidpos.HistoryManagingActivities.HistoryCreateActivity;
import com.scoregroup.androidpos.HistoryManagingActivities.HistoryListActivity;
import com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging;

import java.util.ArrayList;

public class StockListActivity extends AppCompatActivity {
    final int _REQ = 100;
    final int RESULT_STORE = 0;
    final int RESULT_CANCLED = 50;
    private ListView StockListView = null;
    Button buttons[] = new Button[3];
    TextView texts[] = new TextView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        buttons[0] = findViewById(R.id.f5);
        buttons[1] = findViewById(R.id.exit);
        buttons[2] = findViewById(R.id.toHistory);
        texts[0] = findViewById(R.id.keycode);
        texts[1] = findViewById(R.id.name);
        texts[2] = findViewById(R.id.Price);
        texts[3] = findViewById(R.id.Count);

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                texts[0].setText("코드");
                texts[1].setText("품명");
                texts[2].setText("가격");
                stock_list();
                Toast.makeText(getApplicationContext(), "갱신", Toast.LENGTH_LONG).show();
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(in);
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(StockListActivity.this, HistoryListActivity.class);
                in.putExtra(getString(R.string.ModeIntentKey),HistoryManaging.DELIVERY);
                startActivity(in);
            }
        });
    }



    public void stock_list(){ // DB예시로 어댑터와 리스트뷰 연결
        String[] strDate = {"A12","A13","A15","C23"};
        int nDatCnt=0;
        ArrayList<itemsale> oData = new ArrayList<>();

        for (int i=0; i<100; ++i)
        {
            itemsale oItem = new itemsale();
            oItem.Count = (i+1) + " ";
            oItem.Code = strDate[nDatCnt++];
            //oltem.Price =

            oData.add(oItem);
            if (nDatCnt >= strDate.length) nDatCnt = 0;
        }

        StockListView = (ListView)findViewById(R.id.stocklist);
        ListAdapter oAdapter = new ListAdapter(oData);
        StockListView.setAdapter(oAdapter);
    }

    public class itemsale{ // 리스트뷰 데이터 클래스
        public String Code;
        public String Count;
        public String Name;
        public String Price;
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
                convertView = inflater.inflate(R.layout.simple_stock_list, parent, false);
            }

            TextView oTextCode = (TextView) convertView.findViewById(R.id.KeyCode);
            TextView oTextName = (TextView) convertView.findViewById(R.id.name);
            TextView oTextPrice = (TextView) convertView.findViewById(R.id.Price);
            TextView oTextCount = (TextView) convertView.findViewById(R.id.Count);


            oTextCode.setText(m_oData.get(position).Code);
            oTextCount.setText(m_oData.get(position).Count);
            oTextPrice.setText(m_oData.get(position).Count+"000");

            Button f = convertView.findViewById(R.id.detail);
            f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(StockListActivity.this,SingleStockActivity.class);
                    intent.putExtra("nextKey",m_oData.get(position).Code);
                    intent.putExtra("nextCount",m_oData.get(position).Count);
                    intent.putExtra("nextName",m_oData.get(position).Name);
                    intent.putExtra("nextPrice",m_oData.get(position).Price);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}
