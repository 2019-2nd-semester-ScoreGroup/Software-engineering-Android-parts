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

import com.scoregroup.androidpos.Client.ClientManger;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.Data.HistoryItem;
import com.scoregroup.androidpos.HistoryManagingActivities.CustomViews.HistoryItemAdapter;
import com.scoregroup.androidpos.PaymentActivity;
import com.scoregroup.androidpos.R;
import com.scoregroup.androidpos.util.EAN13;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.DELIVERY;
import static com.scoregroup.androidpos.HistoryManagingActivities.HistoryManaging.SELL;


public class HistoryCreateActivity extends AppCompatActivity {
    private static final int INPUT_MODE_ADD = 0;
    private static final int INPUT_MODE_PLUS = 1;
    private static final int INPUT_MODE_MINUS = 2;
    private static final int INPUT_MODE_MULTIPLY = 3;
    private static final int INPUT_MODE_DIVIDE = 4;
    private static final int INPUT_MODE_CANCEL = 5;
    private static final String[] symbols = new String[]{
            "", "+", "-", "×", "÷", "CANCEL"
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
    private Button payButton, historyButton;
    private int selected = -1;
    private TextView totalPrice;

    private String lastText;
    private DecoratedBarcodeView barcodeScanner;

    private ClientManger clientManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivePack = getIntent();
        clientManager = ClientManger.getInstance();
        mode = receivePack.getIntExtra(getString(R.string.ModeIntentKey), SELL);
        setContentView(GetLayoutId(mode));
        scrollArea = findViewById(R.id.scrollArea);
        calcStatus = findViewById(R.id.nowStatus);
        for (int i = 0; i < numPadKeys.length; i++) {
            Button numButton = findViewById(numPadKeys[i]);
            numButton.setOnClickListener((view) -> ProcessNumpadButton(view));
        }
        itemList = new ArrayList<>();
        adapter = new HistoryItemAdapter(itemList);
        scrollArea.setAdapter(adapter);
        payButton = findViewById(R.id.createButton);
        totalPrice = findViewById(R.id.totalPrice);
        payButton.setOnClickListener((view) -> {
            //status 0:Normal, 1:Cancel, 2:Nan
            clientManager.getDB(String.format("addEvent %s %s %d %s", mode == SELL ? "sell" : "delivery", Timestamp.valueOf(LocalDateTime.now().toString().replace('T',' ')), 0, mode == SELL ? "selling" : "delivering"))
                    .setOnReceiveListener((client) -> {
                        if (!client.isReceived()) {
                            runOnUiThread(() -> {
                                Toast.makeText(HistoryCreateActivity.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();

                            });
                            return;
                        }
                        String newKey = client.getData();
                        long newKeyLong = Long.valueOf(newKey);
                        for (HistoryItem t : itemList) {
                            clientManager.getDB(String.format("addChange %d %s %d", newKeyLong, t.getKey(), t.getAmount()*(mode==SELL?-1:1))).send();
                        }
                        if (mode == SELL) {
                            //TODO 결재 액티비티로 변경
                            Intent t = new Intent(HistoryCreateActivity.this, PaymentActivity.class);
                            t.putExtra(getString(R.string.ModeIntentKey), mode);
                            t.putExtra(getString(R.string.EventIntentKey), newKeyLong);
                            startActivity(t);
                            reInitialize();
                        } else if (mode == DELIVERY) {
                            //TODO 액티비티 나가기
                            finish();
                        }
                    }).send();
        });
        if (mode == SELL) {
            historyButton = findViewById(R.id.historyButton);
            historyButton.setOnClickListener((view) -> {
                Intent t = new Intent(HistoryCreateActivity.this, HistoryListActivity.class);
                t.putExtra(getString(R.string.ModeIntentKey), mode);
                startActivity(t);
            });


            //TODO 판매중일 때 UI 이벤트 바인딩
        } else if (mode == DELIVERY) {
            //TODO 납품중일 때 UI 이벤트 바인딩
        }
        //바코드 스캐너
        barcodeScanner = findViewById(R.id.barcodeScanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeScanner.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeScanner.initializeFromIntent(getIntent());
        barcodeScanner.decodeContinuous(callback);
        Log.d("DEBUG", Boolean.toString(barcodeScanner != null));


    }

    private void refreshCalcStatus() {
        calcStatus.setText(symbols[status] + value);
        int totP = 0;
        for (HistoryItem t : itemList) {
            totP += t.getAmount() * t.getPricePerItem();
        }
        totalPrice.setText("" + totP);

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
        selected = adapter.getSelected();
        if (status == INPUT_MODE_CANCEL) status = INPUT_MODE_ADD;
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
                selected = -1;
                break;
            case R.id.plus:
                if (status == INPUT_MODE_PLUS)
                    value = "" + (ToInteger(value) + 1);
                else status = INPUT_MODE_PLUS;
                break;
            case R.id.minus:
                if (status == INPUT_MODE_MINUS)
                    value = "" + (ToInteger(value) + 1);
                else status = INPUT_MODE_MINUS;
                break;
            case R.id.multiply:
                status = INPUT_MODE_MULTIPLY;
                break;
            case R.id.divide:
                status = INPUT_MODE_DIVIDE;
                break;
            case R.id.enter:
                HistoryItem item;
                switch (status) {
                    case INPUT_MODE_ADD:
                        boolean added = false;
                        item=findItemByKey(value);
                        if(item!=null){
                            added=true;
                            item.setAmount(item.getAmount()+1);
                        }
                        if (!added) {
                            item = new HistoryItem(value, "NaN",-1, 1);
                            itemList.add(item);
                            selected=itemList.size()-1;
                            clientManager.getDB("getStock " + value).setOnReceiveListener((client) -> {
                                if (!client.isReceived()) {
                                    runOnUiThread(() -> {
                                        Toast.makeText(HistoryCreateActivity.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();
                                    });
                                    return;
                                }
                                String[] msgs = client.getData().split(" ");
                                HistoryItem tItem=findItemByKey(msgs[0]);
                                tItem.setName(msgs[1]);
                                tItem.setPricePerItem(Integer.valueOf(msgs[2]));
                                value="";
                                refreshCalcStatus();
                            }).send();
                        }
                        break;
                    case INPUT_MODE_PLUS:
                        item = itemList.get(selected);
                        item.setAmount(item.getAmount() + ToInteger(value));
                        status = INPUT_MODE_ADD;
                        value = "";
                        break;
                    case INPUT_MODE_MINUS:
                        item = itemList.get(selected);
                        item.setAmount(item.getAmount() - ToInteger(value));
                        if(item.getAmount()==0){
                            itemList.remove(item);
                        }
                        status = INPUT_MODE_ADD;
                        value = "";
                        break;
                    case INPUT_MODE_MULTIPLY:
                        item = itemList.get(selected);
                        item.setAmount(item.getAmount() * ToInteger(value));
                        status = INPUT_MODE_ADD;
                        value = "";
                        break;
                    case INPUT_MODE_DIVIDE:
                        item = itemList.get(selected);
                        item.setAmount(item.getAmount() / ToInteger(value));
                        status = INPUT_MODE_ADD;
                        value = "";
                        break;
                }
                break;
            case R.id.cancel:
                status = INPUT_MODE_ADD;
                value = "";
                break;
            case R.id.del:
                if (value.length() > 1) {
                    value = value.substring(0, value.length() - 1);
                } else {
                    value = "";
                }
                break;
            case R.id.add1:
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

    private int ToInteger(String input) {
        int ret = 0;
        if (input.length() > 0)
            ret = Integer.valueOf(input);
        return ret;
    }

    public void reInitialize() {
        status = INPUT_MODE_ADD;
        value = "";
        itemList.clear();
        selected = -1;
        adapter.setSelected(-1);
        runOnUiThread(()->{
            adapter.notifyDataSetChanged();
        });

        refreshCalcStatus();
    }

    public synchronized void inputDataByBarcode(String data) {
        status = INPUT_MODE_ADD;
        value = data;
        findViewById(numPadKeys[15]).callOnClick();
    }

    private HistoryItem findItemByKey(String key){
        for(HistoryItem t :itemList){
            if(t.getKey().equals(key))return t;
        }
        return null;
    }

    //바코드
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            Log.v("Barcode", lastText);
            barcodeScanner.setStatusText(result.getText());
            if(lastText.substring(0,7).equals("bundle\n")){
                String[] lines=lastText.substring(7).split("\n");
                for(int i=0;i<lines.length;i++){
                    String[] cells=lines[i].split(";");
                    inputDataByBarcode(cells[0]);
                    itemList.get(selected).setAmount(Integer.valueOf(cells[1]));
                }
            }
            if (EAN13.checkAvailability(lastText)) {
                Log.d("EAN13", "CORRECT MSG : " + lastText);
                inputDataByBarcode(lastText);
                Toast.makeText(getApplicationContext(), lastText, Toast.LENGTH_SHORT).show();
            } else {
                Log.d("EAN13", "WRONG MSG");
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };
}
