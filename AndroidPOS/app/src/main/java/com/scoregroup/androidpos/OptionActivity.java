package com.scoregroup.androidpos;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class OptionActivity extends AppCompatActivity {
    private String IP = "", PASS = "";
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

    /**아이피, 비밀번호(포트) 설정하기*/
    @Override
    protected void onPause() {
        super.onPause();
        IP = E_IP.getText().toString(); // 아이피 입력
        PASS = E_PASS.getText().toString(); // 비밀번호 입력
        SaveIPPW(IP, PASS); // 아이피, 비밀번호 임시 데이터에 저장
        Log.i("ju", "Set: " + IP + ":" + PASS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        E_IP = findViewById(R.id.IPEdit);
        E_PASS = findViewById(R.id.PWEdit);

        ImportIPPW(); // 임시 데이터에 아이피, 포트 불러오기
        E_IP.setText(IP); // 아이피 보여주기
        E_PASS.setText(PASS); // 비밀번호 보여주기
    }
}
