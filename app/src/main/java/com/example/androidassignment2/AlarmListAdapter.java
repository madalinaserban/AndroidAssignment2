package com.example.androidassignment2;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmListAdapter extends ArrayAdapter<AlarmObject> {
    private SharedPreferences sharedPreferences;

    public AlarmListAdapter(Context context, ArrayList<AlarmObject> alarmObjects, SharedPreferences sharedPreferences) {
        super(context, 0, alarmObjects);
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlarmObject alarmObject = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_alarm, parent, false);
        }
        TextView timeTextView = convertView.findViewById(R.id.tv_alarm_time);
        Switch alarmSwitch = convertView.findViewById(R.id.switch_alarm);
        alarmSwitch.setChecked(alarmObject.isActive());

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Modify the corresponding AlarmObject in the mAlarmsList
                alarmObject.setActive(isChecked);
                if (isChecked == true) {
                    alarmSwitch.setText("On");
                } else {
                    alarmSwitch.setText("Off");
                }
                // Update the SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("alarm_on_" + alarmObject.getUniqueId(), isChecked);
                editor.apply();
            }
        });
        // Populate the data into the template view using the data object
        timeTextView.setText(alarmObject.getHour() + ":" + alarmObject.getMinute());
        return convertView;
    }
}
