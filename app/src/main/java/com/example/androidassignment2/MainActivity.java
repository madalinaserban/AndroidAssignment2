package com.example.androidassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private CustomClockView mCustomClockView;
    private TextView mDigitalTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCustomClockView = findViewById(R.id.customClock);
        mDigitalTimeTextView = findViewById(R.id.tv_digitalTime);

        // Start the clock animation thread
        Thread clockThread = new Thread(mCustomClockView);
        clockThread.start();


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateDigitalTime();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    private void updateDigitalTime() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String time = sdf.format(new Date());
                mDigitalTimeTextView.setText(time);
            }
        });
    }
}
