package com.scoregroup.androidpos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scoregroup.androidpos.Client.ClientManger;

public class OptionActivity extends AppCompatActivity {
    ClientManger cm = ClientManger.getInstance();
    String Data = "null";
    private static String IP = "", PASS = "";
    Button buttons[] = new Button[2];
    EditText E_IP, E_PASS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        E_IP.setText(IP); // 아이피 기억
        E_PASS.setText(PASS); // 비밀번호 기억

        buttons[0] = findViewById(R.id.Confirm);
        buttons[1] = findViewById(R.id.ExitButton);
        E_IP = findViewById(R.id.IPEdit);

        buttons[0].setOnClickListener(view -> {
            PASS = E_PASS.getText().toString(); // 비밀번호 입력 저장
            if(!PASS.equals("1234")) { // 비밀번호는 1234, 틀릴 시 리턴
                Toast.makeText(getApplicationContext(), "비밀번호가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            IP = E_IP.getText().toString(); // 아이피 입력 저장
//            Data = cm.getDB("hello");
            Toast.makeText(getApplicationContext(), Data, Toast.LENGTH_LONG).show(); // 임시, 받은 데이터 출력
        });

        buttons[1].setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
        });
    }
}
