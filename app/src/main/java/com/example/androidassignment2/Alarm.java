package com.example.androidassignment2;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class Alarm extends BroadcastReceiver {

    private static final String CHANNEL_ID = "alarm_channel";
    private static final String CHANNEL_NAME = "Alarm Channel";
    private static final String CHANNEL_DESC = "Channel for Alarm notifications";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve the unique identifier of the alarm
        long uniqueId = intent.getLongExtra("uniqueId", 0); // Retrieve uniqueId from intent

        // Use uniqueId to retrieve the alarm data from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAlarms", Context.MODE_PRIVATE);
        int hour = sharedPreferences.getInt("alarm_hour_" + uniqueId, 0);
        int minute = sharedPreferences.getInt("alarm_minute_" + uniqueId, 0);
        Log.d("Alarm", "Retrieving alarm with uniqueId " + uniqueId);

        // Create the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create a notification with a unique ID based on the alarm's unique ID
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Alarm")
                .setContentText("Alarm set for " + hour + ":" + minute)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) uniqueId, builder.build());

        // Create a new PendingIntent to reschedule the alarm
        Intent alarmIntent = new Intent(context, Alarm.class);
        alarmIntent.putExtra("uniqueId", uniqueId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) uniqueId, alarmIntent, PendingIntent.FLAG_IMMUTABLE); // add FLAG_IMMUTABLE

        // Reschedule the alarm for the next day
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long timeInMillis = calendar.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }
}

