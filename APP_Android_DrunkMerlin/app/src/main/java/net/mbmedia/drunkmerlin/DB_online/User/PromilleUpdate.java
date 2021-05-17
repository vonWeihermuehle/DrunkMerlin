package net.mbmedia.drunkmerlin.DB_online.User;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.Konfiguration;

import java.util.HashMap;

public class PromilleUpdate extends AsyncTask {

    private Context context;
    private String hash;
    private String nutzerID;
    private String promille;

    HashMap<String, String> hashMap = new HashMap<>();

    private DBFunktionen dbFunktionen = new DBFunktionen();

    public PromilleUpdate(Context context, String hash, String nutzerID, String promille){
        this.context = context;
        this.hash = hash;
        this.nutzerID = nutzerID;
        this.promille = promille;


        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzerID);
        hashMap.put("promille", promille);
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/User/promille", hashMap);


        return json.equals("[{\"status\":\"erfolg\"}]");

    }

}
