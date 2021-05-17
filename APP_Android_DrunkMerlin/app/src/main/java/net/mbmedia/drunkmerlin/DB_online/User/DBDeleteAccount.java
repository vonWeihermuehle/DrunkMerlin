package net.mbmedia.drunkmerlin.DB_online.User;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.LokalSpeichern;

import java.util.HashMap;

public class DBDeleteAccount extends AsyncTask {
    private Activity activity;
    private ProgressBar progress;
    private LokalSpeichern lokalSpeichern;

    private DBFunktionen fun = new DBFunktionen();
    HashMap<String, String> hashMap = new HashMap<>();

    private static final String LOGTAG = "DBDELETEAccount";

    public DBDeleteAccount(Activity a, String hash, ProgressBar progress, LokalSpeichern ls){
        this.progress = progress;
        this.lokalSpeichern = ls;
        this.activity = a;

        hashMap.put("hash", hash);
        hashMap.put("nutzerid", lokalSpeichern.getUserID());

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress.setVisibility(View.VISIBLE);
    }



    @Override
    protected Boolean doInBackground(Object... objects) {
        String json = fun.getJSON(activity.getApplicationContext(), Konfiguration.URL + "/User/deleteAccount.php", hashMap);

        Log.i(LOGTAG, json);

        return true;

    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        progress.setVisibility(View.INVISIBLE);

        lokalSpeichern.deleteAll();
        activity.finishAffinity();

    }
}
