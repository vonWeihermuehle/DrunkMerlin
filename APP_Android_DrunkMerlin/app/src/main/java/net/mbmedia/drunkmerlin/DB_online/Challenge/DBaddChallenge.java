package net.mbmedia.drunkmerlin.DB_online.Challenge;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.LinearLayout;
import androidx.annotation.RequiresApi;

import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.Konfiguration;

import java.util.Base64;
import java.util.HashMap;


public class DBaddChallenge extends AsyncTask {

    private Context context;
    private String hash;
    private String nutzerID;
    private String text;
    private LinearLayout linearLayout;

    private DBFunktionen dbFunktionen;

    HashMap<String, String> hashMap = new HashMap<>();


    @RequiresApi(api = Build.VERSION_CODES.O)
    public DBaddChallenge(Context context, String hash, String nutzerID, String freundID, String text
            //, ProgressBar progressBar
                          ){
        this.context = context;
        this.hash = hash;
        this.nutzerID = nutzerID;
        this.text = text;
        //this.progressBar = progressBar;
        this.linearLayout = linearLayout;

        String base64text = Base64.getEncoder().encodeToString(text.getBytes());

        //für alten get request
        //url = Konfiguration.URL + "/frageadd?hash=" + hash + "&nutzerid=" + nutzerID + "&freundid=" + freundID + "&text=" + base64text;
        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzerID);
        hashMap.put("freundid", freundID);
        hashMap.put("text", base64text);

        dbFunktionen = new DBFunktionen();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //progressBar.setVisibility(View.VISIBLE);
    }



    @Override
    protected Object doInBackground(Object[] objects) {

        //alter get request
        //String json = dbFunktionen.getJSON(context, url);

        String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Challenge/add", hashMap);

        return json.equals("[{\"status\":\"erfolg\"}]");

    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        //context.finish();
    }
}

