package com.scoregroup.androidpos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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

import com.scoregroup.androidpos.Client.Client;
import com.scoregroup.androidpos.Client.ClientLoading;
import com.scoregroup.androidpos.Client.ClientManger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class StatisticsActivity extends AppCompatActivity {
    ClientManger cm = ClientManger.getInstance();
    private ClientLoading task;
    private ListView SaleListView = null;
    private Button buttons[] = new Button[4];
    private TextView texts[] = new TextView[3];
    private String Data, startymd, endymd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        buttons[0] = findViewById(R.id.Confirm);
        buttons[1] = findViewById(R.id.StartDate);
        buttons[2] = findViewById(R.id.EndDate);
        buttons[3] = findViewById(R.id.ExitButton);
        texts[0] = findViewById(R.id.S_date);
        texts[1] = findViewById(R.id.S_money);
        texts[2] = findViewById(R.id.S_detail);

        DatePickerDialog.OnDateSetListener start = (view, year, month, dayOfMonth) -> {
            buttons[1].setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            startymd = buttons[1].getText().toString();
        };

        DatePickerDialog.OnDateSetListener end = (view, year, month, dayOfMonth) -> {
            buttons[2].setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            endymd = buttons[2].getText().toString();
        };

        buttons[0].setOnClickListener(view -> {
            task = new ClientLoading(this);
            task.show();
            texts[0].setText("날짜");
            texts[1].setText("매출");
            texts[2].setText("비고");
            Client c = cm.getDB("getSelling"+ " " + startymd + " " + endymd);
            c.setOnReceiveListener((v)->{
                Data = v.getData();
                task.dismiss();
                sales_list();
            }).send();
            Log.i("ymd", startymd + " and " + endymd);
        });

        buttons[1].setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog StrDialog = new DatePickerDialog(this, start, cal.get(cal.YEAR), cal.get(cal.MONTH) , cal.get(cal.DATE));
            StrDialog.show();
        });

        buttons[2].setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog EndDialog = new DatePickerDialog(this, end, cal.get(cal.YEAR), cal.get(cal.MONTH) , cal.get(cal.DATE));
            EndDialog.show();
        });

        buttons[3].setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
        });
    }

    public void sales_list(){ // DB데이터로 어댑터와 리스트뷰 연결
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(()->{
            ArrayList<item_selling> sData = new ArrayList<>();

            if(Data == null){
                Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_LONG).show();
                return;
            }

            StringTokenizer stringTokenizer = new StringTokenizer(Data, ",");
            while(stringTokenizer.hasMoreTokens()){
                String line = stringTokenizer.nextToken();
                StringTokenizer lineTokenizer = new StringTokenizer(line, " ");
                item_selling item  = new item_selling();

                String parsedAckMsg = lineTokenizer.nextToken();
                item.Date = parsedAckMsg;

                lineTokenizer.hasMoreTokens();
                item.Money = parsedAckMsg;

                sData.add(item);
            }
            SaleListView = (ListView)findViewById(R.id.salelist);
            ListAdapter sales_Adapter = new ListAdapter(sData);
            SaleListView.setAdapter(sales_Adapter);
        });
    }

    public class item_selling{ // 리스트뷰 용 매출통계 데이터 클래스
        public String Date;
        public String Money;
    }

    public class ListAdapter extends BaseAdapter // 커스텀 리스트뷰 어댑터 구현
    {
        LayoutInflater inflater = null;
        private ArrayList<item_selling> SaleData = null;
        private int nListCnt = 0;

        public ListAdapter(ArrayList<item_selling> sData)
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
