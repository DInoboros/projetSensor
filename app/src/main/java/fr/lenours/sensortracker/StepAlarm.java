package fr.lenours.sensortracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by Clemsbrowning on 23/05/2016.
 */


public class StepAlarm extends BroadcastReceiver {

    PowerManager pm;
    PowerManager.WakeLock wl;
    OnStepAlarmRingListener alarmListener;


    @Override
    public void onReceive(Context context, Intent intent) {

        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        alarmListener.onStepAlarmRing();
        Log.i("StepAlarm", "Toasted !");
        wl.release();
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, StepAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 60 * 24 /* 1 day */, pi);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, StepAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnStepAlarmRingListener(OnStepAlarmRingListener listener) {
        this.alarmListener = listener;
    }
}
