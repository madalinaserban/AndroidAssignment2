package com.example.androidassignment2;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "alarm_channel";
    private static final String CHANNEL_NAME = "Alarm Channel";
    private static final String CHANNEL_DESC = "Channel for Alarm notifications";

    @Override
    public void onReceive(Context context, Intent intent) {

        long uniqueId = intent.getLongExtra("uniqueId", 0);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAlarms", Context.MODE_PRIVATE);
        int hour = sharedPreferences.getInt("alarm_hour_" + uniqueId, 0);
        int minute = sharedPreferences.getInt("alarm_minute_" + uniqueId, 0);
       boolean on = sharedPreferences.getBoolean("alarm_on_" + uniqueId, false);
        Log.d("Alarm", "Retrieving alarm with uniqueId " + uniqueId);
        if (on) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(CHANNEL_DESC);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            // create notification with a unique ID based on the alarm's unique ID
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Alarm")
                    .setContentText("Your alarm for " + hour + ":" + minute)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify((int) uniqueId, builder.build());

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            alarmIntent.putExtra("uniqueId", uniqueId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) uniqueId, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Ringtone ringtone = RingtoneManager.getRingtone(context, alarmSound);
            ringtone.play();

            Calendar alarmTime = Calendar.getInstance();
            alarmTime.set(Calendar.HOUR_OF_DAY, hour);
            alarmTime.set(Calendar.MINUTE, minute);
            alarmTime.set(Calendar.SECOND, 0);
            alarmTime.set(Calendar.MILLISECOND, 0);

            long timeInMillis = alarmTime.getTimeInMillis();

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC, timeInMillis, pendingIntent);
            alarmManager.cancel(pendingIntent);
           sharedPreferences.edit().putBoolean("alarm_on" + uniqueId, false).apply();
        }
    }
}


