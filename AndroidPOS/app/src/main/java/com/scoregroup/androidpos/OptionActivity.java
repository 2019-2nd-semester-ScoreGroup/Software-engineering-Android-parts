package com.scoregroup.androidpos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scoregroup.androidpos.Client.ClientManger;

public class OptionActivity extends AppCompatActivity {
    ClientManger cm = ClientManger.getInstance();
    String Data = null;
    private static String IP = "", PASS = "";
    Button buttons[] = new Button[2];
    EditText E_IP, E_PASS;

    public void SaveIPPW(String ip, String pw){ // sp에 아이피, 비번 저장
        SharedPreferences pref = getSharedPreferences("temp", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("IP", ip);
        editor.putString("PW", pw);
        editor.commit();
    }

    public void ImportIPPW(){
        SharedPreferences pref = getSharedPreferences("temp", MODE_PRIVATE);
        IP = pref.getString("IP", "");
        PASS = pref.getString("PW", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        ImportIPPW();

        buttons[0] = findViewById(R.id.Confirm);
        buttons[1] = findViewById(R.id.ExitButton);
        E_IP = findViewById(R.id.IPEdit);
        E_PASS = findViewById(R.id.PWEdit);

        E_IP.setText(IP); // 아이피 기억
        E_PASS.setText(PASS); // 비밀번호 기억

        buttons[0].setOnClickListener(view -> {
            PASS = E_PASS.getText().toString(); // 비밀번호 입력 저장

            if(!PASS.equals("1234")) { // 초기 비밀번호는 1234, 틀릴 시 리턴
                PASS = ""; // 패스워드 초기화
                Toast.makeText(getApplicationContext(), "비밀번호가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            // 성공 시
            IP = E_IP.getText().toString(); // 아이피 입력 저장
            SaveIPPW(IP, PASS); // sp에 저장
            ClientManger.set_IP(IP); // 클라이언트 매니저에 IP 저장
            // DB와 통신 시작
            Data = cm.getDB("hello");
            // 성공된 데이터 출력
            Toast.makeText(getApplicationContext(), Data, Toast.LENGTH_LONG).show(); // 임시, 받은 데이터 출력
        });

        buttons[1].setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
        });
    }
}
