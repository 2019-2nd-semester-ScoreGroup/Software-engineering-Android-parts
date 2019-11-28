package com.scoregroup.androidpos.HistoryManagingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data.HistoryItem;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.HistoryItemAdapter;
import com.scoregroup.androidpos.PaymentActivity;
import com.scoregroup.androidpos.R;
import com.scoregroup.androidpos.util.EAN13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;


public class HistoryCreateActivity extends AppCompatActivity {
    private static final int
            ADD = 0, PLUS = 1, MINUS = 2, MULTIPLY = 3, DIVIDE = 4, DELETE = 5, CANCEL = 6;
    private static final String[] symbols = new String[]{
            "", "+", "-", "×", "÷", "DEL", "CANCEL"
    };
    private final int[] numPadKeys =
            new int[]{R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
                    R.id.remove, R.id.plus, R.id.minus, R.id.multiply, R.id.divide, R.id.enter, R.id.del, R.id.cancel, R.id.add1, R.id.add2};

    public static int GetLayoutId(int mode) {
        switch (mode) {
            case DELIVERY:
                return R.layout.activity_delivery_history_creating;
            case SELL:
            default:
                return R.layout.activity_sell_history_creating;
        }
    }
    private HistoryItemAdapter adapter;
    private int status;
    private String value = "";
    private int mode;
    private Intent receivePack;
    private TextView calcStatus;
    private ListView scrollArea;
    private ArrayList<HistoryItem> itemList;
    private Button payButton,historyButton;
    private int selected=-1;
    private TextView totalPrice;

    private String lastText;
    private DecoratedBarcodeView barcodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivePack = getIntent();

        mode = receivePack.getIntExtra(getString(R.string.ModeIntentKey), SELL);
        setContentView(GetLayoutId(mode));
        scrollArea=findViewById(R.id.scrollArea);
        calcStatus = findViewById(R.id.nowStatus);
        for (int i = 0; i < numPadKeys.length; i++) {
            Button numButton = findViewById(numPadKeys[i]);
            numButton.setOnClickListener((view) -> ProcessNumpadButton(view));
        }
        itemList=new ArrayList<>();
         adapter=new HistoryItemAdapter(itemList);
        scrollArea.setAdapter(adapter);
        payButton=findViewById(R.id.createButton);
        payButton.setOnClickListener((view)->{
            String newKey="새로운 키";
            //TODO DB로 전송 후 새로 생긴 이벤트 키 받기(newKey)


            if(newKey==null){
                Log.e("POS","DB registering Failed");
            }else {
                if(mode==SELL){
                    //TODO 결재 액티비티로 변경
                    Intent t = new Intent(HistoryCreateActivity.this, PaymentActivity.class);
                    t.putExtra(getString(R.string.ModeIntentKey), mode);
                    t.putExtra(getString(R.string.EventIntentKey), newKey);
                    startActivity(t);
                    reInitialize();
                }else if(mode==DELIVERY){
                    //TODO 액티비티 나가기
                    finish();
                }

            }
        });
        if(mode==SELL){
            historyButton=findViewById(R.id.historyButton);
            historyButton.setOnClickListener((view)->{
                Intent t=new Intent(HistoryCreateActivity.this,HistoryListActivity.class);
                t.putExtra(getString(R.string.ModeIntentKey),mode);
                startActivity(t);
            });
            //TODO 판매중일 때 UI 이벤트 바인딩
        }else if(mode==DELIVERY){
            //TODO 납품중일 때 UI 이벤트 바인딩
        }

        totalPrice=findViewById(R.id.totalPrice);


        //바코드 스캐너
        barcodeScanner= findViewById(R.id.barcodeScanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeScanner.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeScanner.initializeFromIntent(getIntent());
        barcodeScanner.decodeContinuous(callback);
        Log.d("DEBUG",Boolean.toString(barcodeScanner!=null));
    }

    private void refreshCalcStatus() {
        calcStatus.setText(symbols[status] + value);
        int totP=0;
        for(HistoryItem t:itemList){
            totP+=t.getAmount()*t.getPricePerItem();
        }
        totalPrice.setText(""+totP);

    }
    @Override
    protected void onResume() {
        super.onResume();

        barcodeScanner.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeScanner.pause();
    }
    public void triggerScan(View view) {
        barcodeScanner.decodeSingle(callback);
    }



    public void pause(View view) {
        barcodeScanner.pause();
    }

    public void resume(View view) {
        barcodeScanner.resume();
    }
    private void ProcessNumpadButton(View view) {//TODO 넘패드 버튼 이벤트
        selected=adapter.getSelected();
        if(status==CANCEL)status=ADD;
        switch (view.getId()) {
            case R.id.one:
                value += "1";
                break;
            case R.id.two:
                value += "2";
                break;
            case R.id.three:
                value += "3";
                break;
            case R.id.four:
                value += "4";
                break;
            case R.id.five:
                value += "5";
                break;
            case R.id.six:
                value += "6";
                break;
            case R.id.seven:
                value += "7";
                break;
            case R.id.eight:
                value += "8";
                break;
            case R.id.nine:
                value += "9";
                break;
            case R.id.zero:
                value += "0";
                break;
            case R.id.remove:
                itemList.remove(selected);
                selected=-1;
                break;
            case R.id.plus:
                if(status==PLUS)
                    value=""+(ToInteger(value)+1);
                else status=PLUS;
                break;
            case R.id.minus:
                if(status==MINUS)
                    value=""+(ToInteger(value)+1);
                else status=MINUS;
                break;
            case R.id.multiply:
                status=MULTIPLY;
                break;
            case R.id.divide:
                status=DIVIDE;
                break;
            case R.id.enter:
                HistoryItem item;
                switch(status){
                    case ADD:
                        boolean added=false;
                        for(HistoryItem t :itemList){
                            if(t.getKey().equals(value)){
                                t.setAmount(t.getAmount()+1);
                                added=true;
                            }
                        }
                        if(!added){
                            //TODO 네트워크
                            item=new HistoryItem(value,"example",100,1);
                            itemList.add(item);
                            selected=itemList.indexOf(item);
                        }
                        value="";
                        break;
                    case PLUS:
                        item=itemList.get(selected);
                        item.setAmount(item.getAmount()+ToInteger(value));
                        status=ADD;
                        value="";
                        break;
                    case MINUS:
                        item=itemList.get(selected);
                        item.setAmount(item.getAmount()-ToInteger(value));
                        status=ADD;
                        value="";
                        break;
                    case MULTIPLY:
                        item=itemList.get(selected);
                        item.setAmount(item.getAmount()*ToInteger(value));
                        status=ADD;
                        value="";
                        break;
                    case DIVIDE:
                        item=itemList.get(selected);
                        item.setAmount(item.getAmount()/ToInteger(value));
                        status=ADD;
                        value="";
                        break;
                }
                break;
            case R.id.cancel:
                status=ADD;
                value="";
                break;
            case R.id.del:
                if(value.length()>1){
                    value=value.substring(0,value.length()-1);
                }else{
                    value="";
                }
                break;
            case R.id.add1:
                break;
            case R.id.add2:
                break;
            default:
                Log.e("POS", "Unavailable button Id");
                break;
        }
        refreshCalcStatus();
        adapter.setSelected(selected);
        adapter.notifyDataSetChanged();

    }
    private int ToInteger(String input){
        int ret=0;
        if(input.length()>0)
            ret=Integer.valueOf(input);
        return ret;
    }
    public void reInitialize(){
        status=ADD;
        value="";
        itemList.clear();
        selected=-1;
        adapter.setSelected(-1);
        adapter.notifyDataSetChanged();
        refreshCalcStatus();
    }

    public synchronized void inputDataByBarcode(String data){
        status=ADD;
        value=data;
        findViewById(numPadKeys[15]).callOnClick();
    }


    //바코드
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            barcodeScanner.setStatusText(result.getText());
            if(EAN13.checkAvailability(lastText)) {
                Log.d("EAN13", "CORRECT MSG : " + lastText);
                inputDataByBarcode(lastText);
                Toast.makeText(getApplicationContext(), lastText, Toast.LENGTH_SHORT).show();
            }else{
                Log.d("EAN13","WRONG MSG");
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };
}
