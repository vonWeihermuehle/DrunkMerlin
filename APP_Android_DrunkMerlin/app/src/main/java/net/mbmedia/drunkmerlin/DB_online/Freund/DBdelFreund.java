package net.mbmedia.drunkmerlin.DB_online.Freund;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.Konfiguration;

import java.util.HashMap;

public class DBdelFreund extends AsyncTask {

    private String hash;
    private String nutzerID;
    private String freundID;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private Context context;
    HashMap<String, String> hashMap = new HashMap<>();

    private DBFunktionen dbFunktionen = new DBFunktionen();

    public DBdelFreund(Context context, String hash, String nutzerID, String freundID, LinearLayout linearLayout, ProgressBar progressBar){
        this.context = context;
        this.hash = hash;
        this.nutzerID = nutzerID;
        this.freundID = freundID;
        this.linearLayout = linearLayout;
        this.progressBar = progressBar;

        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzerID);
        hashMap.put("freundid", freundID);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Freund/del", hashMap);

        return json.equals("[{\"status\":\"erfolg\"}]");

    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        DBshowFreund dBshowFreund = new DBshowFreund(context, hash, nutzerID, linearLayout, progressBar);
        dBshowFreund.execute();
    }
}
