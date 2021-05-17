package net.mbmedia.drunkmerlin.DB_online.Notification;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import net.mbmedia.drunkmerlin.Activity.ActivityFunktionen;
import net.mbmedia.drunkmerlin.Activity.HauptActivity;
import net.mbmedia.drunkmerlin.DB_online.Challenge.DBdelChallenge;
import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.JsonParser;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.Notification.NotificationService;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.TO.Notification;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DBgetNotification extends AsyncTask {

    private String hash;
    private String nutzerID;
    HashMap<String, String> hashMap = new HashMap<>();

    private Context context;

    private JsonParser parser = new JsonParser();
    private DBFunktionen dbFunktionen = new DBFunktionen();

    ArrayList<Notification> notifications;

    private NotificationManager mNM;

    private String CHANNEL_ID = "DrunkMerlinChannel";

    LokalSpeichern lokalSpeichern;



    public DBgetNotification(Context context, String hash, String nutzerID){
        this.hash = hash;
        this.nutzerID = nutzerID;
        this.context = context;
        lokalSpeichern = new LokalSpeichern(context);

        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzerID);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Challenge/show", hashMap);
            Log.i("test", json);
            notifications = parser.getNotifications(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        mNM = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if(notifications == null){
            return;
        }

        for(int i=0; i<notifications.size(); i++){
            showNotification(context.getResources().getString(R.string.txt_ihavenever) + "<br /> \r\n" + notifications.get(i).getText(), notifications.get(i).getFreundName());
            loescheNotification(notifications.get(i).getId());
            Log.i(NotificationService.LOGTAG, "zeige notification");
            return;
        }

    }

    private void loescheNotification(int id){
        if(context != null){
            DBdelChallenge del = new DBdelChallenge(context, ActivityFunktionen.getHash(), lokalSpeichern.getUserID(), id);
            del.execute();
        }

    }

    private void showNotification(String text, String author) {

        createNotificationChannel();

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, HauptActivity.class);
        intent.putExtra(NotificationService.NOTIFI_FLAG, text);
        intent.putExtra(NotificationService.AUTHOR_FLAG, author);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(text.replace("<br />", "\n"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "DrunkMerlinChannel";
            String description = "DrunkMerlinChannel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}
