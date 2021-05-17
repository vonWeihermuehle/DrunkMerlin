package net.mbmedia.drunkmerlin.DB_online.Challenge;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.DB_online.Freund.DBshowFreund;
import net.mbmedia.drunkmerlin.Konfiguration;

import java.util.HashMap;


public class DBdelChallenge extends AsyncTask {

    private String hash;
    private String nutzerID;
    private int ID;
    private Context context;
    HashMap<String, String> hashMap = new HashMap<>();

    private DBFunktionen dbFunktionen = new DBFunktionen();

    public DBdelChallenge(Context context, String hash, String nutzerID, int id){
        this.context = context;
        this.hash = hash;
        this.nutzerID = nutzerID;

        //this.url = Konfiguration.URL + "/fragedel?hash=" + hash + "&nutzerid=" + nutzerID + "&id=" + id;
        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzerID);
        hashMap.put("id", String.valueOf(id));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Challenge/del", hashMap);

        return json.equals("[{\"status\":\"erfolg\"}]");

    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}

