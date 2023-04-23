package com.example.androidassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AlarmListActivity extends AppCompatActivity {

    private ListView mAlarmListView;
    private List<AlarmObject> mAlarmsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms_list);

        mAlarmListView = findViewById(R.id.list_view_alarms);
        mAlarmsList = new ArrayList<>();

        // Get all the alarms from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAlarms", Context.MODE_PRIVATE);
        Map<String, ?> alarms = sharedPreferences.getAll();

        // Loop through all the alarms and add them to the list
        for (Map.Entry<String, ?> entry : alarms.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // check if it s time or on/off method
            if (value instanceof Integer) {
                int intValue = (Integer) value;
                if (key.contains("hour")) {
                    String uniquekey = key.substring(key.lastIndexOf("_") + 1);
                    long uniqueId = Long.parseLong(uniquekey);
                    AlarmObject alarm = getAlarmById(uniqueId);
                    if (alarm != null) {
                        alarm.setHour(intValue);
                    } else {
                        alarm = new AlarmObject(uniqueId, intValue, 0, true);
                        mAlarmsList.add(alarm);
                    }
                } else if (key.contains("minute")) {
                    String uniquekey = key.substring(key.lastIndexOf("_") + 1);
                    long uniqueId = Long.parseLong(uniquekey);
                    AlarmObject alarm = getAlarmById(uniqueId);
                    if (alarm != null) {
                        alarm.setMinute(intValue);
                    } else {
                        alarm = new AlarmObject(uniqueId, 0, intValue, true);
                        mAlarmsList.add(alarm);
                    }
                }
            } else if (value instanceof Boolean) {
                boolean boolValue = (Boolean) value;
                String uniquekey = key.substring(key.lastIndexOf("_") + 1);
                long uniqueId = Long.parseLong(uniquekey);
                AlarmObject alarm = getAlarmById(uniqueId);
                if (alarm != null) {
                    alarm.setActive(boolValue);
                } else {
                    alarm = new AlarmObject(uniqueId, 0, 0, boolValue);
                    mAlarmsList.add(alarm);
                }
            }
        }


        Collections.sort(mAlarmsList, new Comparator<AlarmObject>() {
            @Override
            public int compare(AlarmObject a1, AlarmObject a2) {
                return Long.compare(a1.getUniqueId(), a2.getUniqueId());
            }
        });
        ListView listView = findViewById(R.id.list_view_alarms);
        AlarmListAdapter adapter = new AlarmListAdapter(this, (ArrayList<AlarmObject>) mAlarmsList,sharedPreferences);

        // set adapter for ListView
        listView.setAdapter(adapter);

    }


    private AlarmObject getAlarmById(long id) {
        for (AlarmObject alarm : mAlarmsList) {
            if (alarm.getUniqueId() == id) {
                return alarm;
            }
        }
        return null;
    }
}
