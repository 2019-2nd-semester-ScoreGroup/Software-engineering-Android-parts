package com.scoregroup.androidpos;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scoregroup.androidpos.Client.Client;
import com.scoregroup.androidpos.Client.ClientLoading;
import com.scoregroup.androidpos.Client.ClientManger;
import com.scoregroup.androidpos.HistoryManagingActivities.HistoryCreateActivity;
import com.scoregroup.androidpos.HistoryManagingActivities.HistoryListActivity;
import com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging;

import java.io.IOError;
import java.security.cert.PKIXRevocationChecker;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import static com.scoregroup.androidpos.Client.Client.Diff;

//activity_stock.xml 레이아웃

public class StockListActivity extends AppCompatActivity {
    ClientManger clientManger = ClientManger.getInstance(this);
    final int _REQ = 100;
    final int RESULT_STORE = 0;
    final int RESULT_CANCLED = 50;
    final int REQUEST_BARCODE = 1;
    final int REQUEST_NAME = 2;
    final int REQUEST_PRICE= 3;
    final int REQUEST_ACT = 10;
    final int RESULT_NAN = 11;
    private ListView StockListView = null;
    private String Data;
    private Button addStockButton;
    private ImageButton Hbutton, Ibutton;
    private ClientLoading task;
    private ArrayList<itemsale> oData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        Hbutton = findViewById(R.id.toHistory);
        Ibutton = findViewById(R.id.refreshButton);
        addStockButton = findViewById(R.id.addStockButton);

        task = new ClientLoading(this);
        task.show();
        getStockList();

        addStockButton.setOnClickListener(view -> {
            Intent intent = new Intent(StockListActivity.this, SingleStockActivity.class);
            intent.putExtra("Mode", HistoryManaging.DELIVERY_ADD);
            startActivityForResult(intent, REQUEST_ACT);
        });

        Hbutton.setOnClickListener(view -> {
            Intent in = new Intent(StockListActivity.this, HistoryListActivity.class);
            in.putExtra("Mode", HistoryManaging.DELIVERY);
            startActivity(in);
        });

        Ibutton.setOnClickListener(view -> {
            task = new ClientLoading(this);
            task.show();
            getStockList();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCLED) {
            Toast.makeText(StockListActivity.this, "실패", Toast.LENGTH_SHORT).show();
        }

        if(resultCode == RESULT_OK)
            Toast.makeText(StockListActivity.this, "성공", Toast.LENGTH_SHORT).show();

        return;
    }

    private void getStockList()
    {
        connectClient();
    }

    private void connectClient()
    {
        Client client = clientManger.getDB("getStocks");

        client.setOnReceiveListener((v)->{
            Log.i("ju", "리스너 실행");
            Data = v.getData();
            task.dismiss();
            setStockList();
        }).send();
    }

    private void setStockList()
    {
        oData = new ArrayList<itemsale>();

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(()-> {
            // 데이터가 널 값일 시 리턴
            if (Data == null) {
                Toast.makeText(getApplicationContext(), "연결 실패", Toast.LENGTH_SHORT).show();
                return;
            }

            StringTokenizer stringTokenizer = new StringTokenizer(Data, ",");

            tokenizingAndAdd(stringTokenizer);

            StockListView = (ListView) findViewById(R.id.stocklist);
            ListAdapter oAdapter = new ListAdapter(oData);
            StockListView.setAdapter(oAdapter);
        });
    }

    private void tokenizingAndAdd(StringTokenizer stringTokenizer)
    {
        while (stringTokenizer.hasMoreTokens()) {
            String line = stringTokenizer.nextToken();
            StringTokenizer lineTokenizer = new StringTokenizer(line, Diff);
            String parsedAckMsg = lineTokenizer.nextToken();

            //예외 출력
            if (lineTokenizer.countTokens() == 1)
                Toast.makeText(this.getApplicationContext(), parsedAckMsg, Toast.LENGTH_SHORT).show();

            itemsale itemsale = new itemsale();
            itemsale.Code = parsedAckMsg;

            try {
                if (lineTokenizer.hasMoreTokens())
                    parsedAckMsg = lineTokenizer.nextToken();
                itemsale.Name = parsedAckMsg.replaceAll("_", " ");

                if (lineTokenizer.hasMoreTokens())
                    parsedAckMsg = lineTokenizer.nextToken();
                itemsale.Price = parsedAckMsg;

                if (lineTokenizer.hasMoreTokens())
                    parsedAckMsg = lineTokenizer.nextToken();
                itemsale.Count = parsedAckMsg;


            } catch(NoSuchElementException e)
            {
                Toast.makeText(this.getApplicationContext(), "DB error", Toast.LENGTH_SHORT).show();
            }
            oData.add(itemsale);
        }
    }

    public class itemsale { // 리스트뷰 데이터 클래스
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

        public ListAdapter(ArrayList<itemsale> _oData) {
            m_oData = _oData;
            nListCnt = m_oData.size();
        }

        @Override
        public int getCount() {
            Log.i("TAG", "getCount");
            return nListCnt;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                final Context context = parent.getContext();
                if (inflater == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }
                convertView = inflater.inflate(R.layout.simple_stock_list, parent, false);
            }

            TextView oTextCode = (TextView) convertView.findViewById(R.id.KeyCode);
            TextView oTextName = (TextView) convertView.findViewById(R.id.Name);
            TextView oTextPrice = (TextView) convertView.findViewById(R.id.Price);
            TextView oTextCount = (TextView) convertView.findViewById(R.id.Count);


            oTextCode.setText(m_oData.get(position).Code.replaceAll(" ", ""));
            oTextName.setText(m_oData.get(position).Name);
            oTextCount.setText(m_oData.get(position).Count.replaceAll(" ", ""));
            oTextPrice.setText(m_oData.get(position).Price.replaceAll(" ", ""));

            Button f = convertView.findViewById(R.id.detail);
            f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StockListActivity.this, SingleStockActivity.class);
                    intent.putExtra("Mode", HistoryManaging.DELIVERY_CHANGE);
                    intent.putExtra("nextKey", m_oData.get(position).Code);
                    intent.putExtra("nextCount", m_oData.get(position).Count);
                    intent.putExtra("nextName", m_oData.get(position).Name);
                    intent.putExtra("nextPrice", m_oData.get(position).Price);
                    startActivityForResult(intent, REQUEST_ACT);
                }

            });

            return convertView;
        }

    }


}
