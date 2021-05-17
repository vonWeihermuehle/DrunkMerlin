package net.mbmedia.drunkmerlin.DB_online.Drink;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import net.mbmedia.drunkmerlin.DB_offline.OfflineDBHelper;
import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.LokalSpeichern;

import java.util.HashMap;


public class DBdelAllDrink extends AsyncTask {

    private String nutzerID;
    private String hash;
    HashMap<String, String> hashMap = new HashMap<>();

    private ProgressBar progressBar;

    private LinearLayout linearLayout;

    private Context context;

    private DBFunktionen dbFunktionen = new DBFunktionen();

    private OfflineDBHelper dbHelper;
    private LokalSpeichern lokalSpeichern;


    public DBdelAllDrink(Context context, String hash, String nutzerID, LinearLayout linearLayout, ProgressBar progressBar){
        this.hash = hash;
        this.nutzerID = nutzerID;
        this.linearLayout = linearLayout;
        this.progressBar = progressBar;
        this.context = context;

        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzerID);
        hashMap.put("drinkid", "all");

        dbHelper = new OfflineDBHelper(context);
        lokalSpeichern = new LokalSpeichern(context);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        lokalSpeichern.DrinkListeSollgeloeschtwerden(false);
    }

    @Override
    protected Boolean doInBackground(Object[] objects) {

        if(lokalSpeichern.isOfflineModus()){


            dbHelper.delAlleDrinks();

            return true;

        }else{
            String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Drink/del", hashMap);

            return json.equals("[{\"status\":\"erfolg\"}]");
        }



    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        linearLayout.removeAllViews();

        DBshowDrink dBshowDrink = new DBshowDrink(context, hash, nutzerID, linearLayout, progressBar);
        dBshowDrink.execute();
    }
}
