package com.scoregroup.androidpos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.scoregroup.androidpos.Client.Client;

public class OptionActivity extends AppCompatActivity {
    Client c = Client.getInstance();
    String dbsource = "null!";
    Button buttons[] = new Button[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        buttons[0] = findViewById(R.id.Confirm);
        buttons[1] = findViewById(R.id.ExitButton);

        buttons[0].setOnClickListener(view -> {
            c.getDB("hello");

            Toast.makeText(getApplicationContext(), dbsource, Toast.LENGTH_LONG).show();
        });

        buttons[1].setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
        });
    }
}
