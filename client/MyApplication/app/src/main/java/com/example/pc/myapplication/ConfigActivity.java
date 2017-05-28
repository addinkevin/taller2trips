package com.example.pc.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    EditText editText;
    TextView distance;
    private TripTP tripTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(this);
        tripTP = (TripTP) getApplication();
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(((TripTP)getApplication()).getUrl());
        distance = (TextView) findViewById(R.id.distance);
        SeekBar seekBar = (SeekBar) findViewById(R.id.intensitySlider);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(tripTP.getRadio());
    }

    @Override
    public void onClick(View v) {
        String ip = editText.getText().toString();
        String[] info = distance.getText().toString().split(" "); // 25 Km
        tripTP.setUrl(ip);
        int dist = Consts.DEF_RADIO;
        try {
            dist = Integer.valueOf(info[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        tripTP.setRadio(dist);
        this.finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        distance.setText(progress + " Km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
