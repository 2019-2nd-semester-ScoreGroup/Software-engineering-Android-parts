package com.scoregroup.androidpos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scoregroup.androidpos.Client.Client;
import com.scoregroup.androidpos.Client.ClientLoading;
import com.scoregroup.androidpos.Client.ClientManger;

public class OptionActivity extends AppCompatActivity {
    ClientManger cm = ClientManger.getInstance();
    private ClientLoading task;
    private String Data = null;
    private static String IP = "", PASS = "";
    private Button buttons[] = new Button[2];
    private EditText E_IP, E_PASS;

    /**쉐얼드 프리퍼런스 아이피, 패스워드 저장*/
    private void SaveIPPW(String ip, String pw){ // sp에 아이피, 비번 저장
        SharedPreferences pref = getSharedPreferences("temp", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("IP", ip);
        editor.putString("PW", pw);
        editor.commit();
    }

    /**쉐얼드 프리퍼런스 아이피, 패스워드 불러오기*/
    private void ImportIPPW(){
        SharedPreferences pref = getSharedPreferences("temp", MODE_PRIVATE);
        IP = pref.getString("IP", "localhost");
        PASS = pref.getString("PW", "12142");
    }

    /**리시브 리스너로 UI 실행 시 다음과 같은 핸들러를 사용해야 함
     * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
    private void Login(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(() -> {
            if(Data == null)
                Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_SHORT).show();
            else if(Data.equals("pass"))
                Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        buttons[0] = findViewById(R.id.Confirm);
        buttons[1] = findViewById(R.id.ExitButton);
        E_IP = findViewById(R.id.IPEdit);
        E_PASS = findViewById(R.id.PWEdit);

        ImportIPPW();
        E_IP.setText(IP); // 아이피 기억
        E_PASS.setText(PASS); // 비밀번호 기억

        buttons[0].setOnClickListener(view -> {
            task = new ClientLoading(this);
            task.show();
            IP = E_IP.getText().toString(); // 아이피 입력
            PASS = E_PASS.getText().toString(); // 비밀번호 입력
            SaveIPPW(IP, PASS); // 아이피, 비밀번호 저장
            // 서버에 로그인 예시
            ClientManger.getIP(IP); // 클라이언트 매니저에 IP 저장
            ClientManger.getPORT(Integer.parseInt(PASS));
            /**@중요 NEW DB요청 방식*/
            /**1.클라이언트 매니저의 겟디비로 클라이언트를 생성, 포맷은 기존과 동일
             * 2.리시브 리스너(.setOnReceiveListener)로 DB로 온 데이터를 받고 원하는 액티비티 메소드를 실행
             * 3.리시브 리스너 후(.send)필수임! 아니면 동작안함.
             * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
            Client c = cm.getDB("login");
            c.setOnReceiveListener((v)->{
                Log.i("ju", "리스너 실행");
                Data = v.getData();
                task.dismiss();
                Login();
            }).send();
        });

        buttons[1].setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
        });
    }
}
