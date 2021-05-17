package net.mbmedia.drunkmerlin.Notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import net.mbmedia.drunkmerlin.Activity.ActivityFunktionen;
import net.mbmedia.drunkmerlin.DB_online.Notification.DBgetNotification;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.LokalSpeichern;

import static net.mbmedia.drunkmerlin.Notification.NotificationService.LOGTAG;

public class NotificationManager extends BroadcastReceiver {

    public Handler handler = null;
    public static Runnable runnable = null;

    private Context context;
    private LokalSpeichern lokalSpeichern;


    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;
        lokalSpeichern = new LokalSpeichern(context);
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Log.i(LOGTAG, "Service is running");
                checkForNewNotification();
                //handler.postDelayed(runnable, Konfiguration.NOTIFICATION_TIME);
            }
        };

        handler.postDelayed(runnable, 1000);

        wl.release();
    }

    @SuppressLint("ShortAlarm")
    public void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, NotificationManager.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Konfiguration.NOTIFICATION_TIME, pi);
    }

    private void checkForNewNotification(){
        DBgetNotification db = new DBgetNotification(context, ActivityFunktionen.getHash(), lokalSpeichern.getUserID());
        db.execute();
    }


}
