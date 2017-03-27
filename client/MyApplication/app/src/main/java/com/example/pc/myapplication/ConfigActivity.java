package com.example.pc.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pc.myapplication.application.MyApplication;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.editText);
    }

    @Override
    public void onClick(View v) {
        String ip = editText.getText().toString();

        if (ip.isEmpty()) {
            ip = "";
        }

        ((MyApplication) getApplication()).setUrl(ip);
    }
}
