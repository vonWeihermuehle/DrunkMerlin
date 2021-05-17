package net.mbmedia.drunkmerlin.Notification;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {

    public static final String LOGTAG = "NotificationService";

    public static final String NOTIFI_FLAG = "NotifiacationFlag";
    public static final String AUTHOR_FLAG = "Notif_Author";

    public NotificationService() {
    }

    NotificationManager nm = new NotificationManager();
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        NotificationManager nr = new NotificationManager();
        registerReceiver(nr, filter);

        nm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        nm.setAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
