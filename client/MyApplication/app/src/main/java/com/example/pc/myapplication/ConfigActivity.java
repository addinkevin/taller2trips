package com.example.pc.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pc.myapplication.application.TripTP;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(((TripTP)getApplication()).getUrl());
    }

    @Override
    public void onClick(View v) {
        String ip = editText.getText().toString();
        ((TripTP) getApplication()).setUrl(ip);
        this.finish();
    }
}
