package net.mbmedia.drunkmerlin.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver
{
    NotificationManager nm = new NotificationManager();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            nm.setAlarm(context);
        }
    }
}