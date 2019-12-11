package com.scoregroup.androidpos;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OptionActivity extends AppCompatActivity {
    private String IP = "", PASS = "";
    private EditText E_IP, E_PASS;
    private Button[] btn = new Button[4];
    private AlertDialog.Builder Notice, QNA, Authority;

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
        btn[0] = findViewById(R.id.i1);
        btn[1] = findViewById(R.id.i2);
        btn[2] = findViewById(R.id.i3);
        btn[3] = findViewById(R.id.i4);
        Notice = new AlertDialog.Builder(this);
        QNA = new AlertDialog.Builder(this);
        Authority = new AlertDialog.Builder(this);

        ImportIPPW(); // 임시 데이터에 아이피, 포트 불러오기
        E_IP.setText(IP); // 아이피 보여주기
        E_PASS.setText(PASS); // 비밀번호 보여주기

        btn[0].setOnClickListener(view -> Toast.makeText(getApplicationContext(), "앱이 최신 버전입니다.", Toast.LENGTH_SHORT).show());

        btn[1].setOnClickListener(view -> {
            Notice.setTitle("공지사항");
            Notice.setMessage("1.0.0 버전 런칭!");
            Notice.show();
        });

        btn[2].setOnClickListener(view -> {
            QNA.setTitle("문의하기");
            QNA.setMessage("wnalsals12@naver.com");
            QNA.show();
        });

        btn[3].setOnClickListener(view -> {
            Authority.setTitle("앱 권한정보");
            Authority.setMessage("카메라");
            Authority.show();
        });
    }
}
