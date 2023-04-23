package com.example.androidassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("Main", "A ajuns aici");

        if (id == R.id.action_set_timer) {
            openAddAlarmActivity();
            return true;
        }
        if (id == R.id.action_view_alarms) {
            openViewAlarmsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openAddAlarmActivity() {
        Intent intent = new Intent(this, SetAlarmActivity.class);
        startActivity(intent);
    }

    private void openViewAlarmsActivity() {
        Intent intent = new Intent(this, AlarmListActivity.class);
        startActivity(intent);

    }


}

