package net.mbmedia.drunkmerlin.DB_online.Drink;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import net.mbmedia.drunkmerlin.DB_offline.OfflineDBHelper;
import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.JsonParser;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.TO.Drink;

import java.util.Base64;
import java.util.HashMap;

public class DBaddDrink extends AsyncTask {

    private String hash;
    private String nutzerid;
    private String name;
    private String menge;
    private String prozent;
    private String zeitpunkt;
    private String base64LatLng;

    HashMap<String, String> hashMap = new HashMap<>();

    private DBFunktionen dbFunktionen;

    private JsonParser jsonParser = new JsonParser();

    private LokalSpeichern lokalSpeichern;

    public OfflineDBHelper offlineDB;

    private ProgressBar progressBar;

    private Context context;

    //http://localhost:8000/drink?hash=3c2599ef96507afec934d08a4c077e43&befehl=add&nutzerid=1&name=name&menge=0.2&prozent=30&zeitpunkt=zeitpunkt

    public DBaddDrink(Context context, String hash, String nutzerid, String name, String menge, String prozent, String zeitpunkt, String base64LatLng, ProgressBar progressBar){
        this.hash = hash;
        this.nutzerid = nutzerid;
        this.name = name;
        this.menge = menge;
        this.prozent = prozent;
        this.zeitpunkt = zeitpunkt;
        this.context = context;
        this.progressBar = progressBar;
        this.base64LatLng = base64LatLng;

        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzerid);
        hashMap.put("name", name);
        hashMap.put("menge", menge);
        hashMap.put("prozent", prozent);
        hashMap.put("zeitpunkt", zeitpunkt);
        hashMap.put("latlng", base64LatLng);

        dbFunktionen = new DBFunktionen();
        lokalSpeichern = new LokalSpeichern(context);
        offlineDB = new OfflineDBHelper(context);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Boolean doInBackground(Object[] objects) {

        if(lokalSpeichern.isOfflineModus()){

            //zeitpunkt ist base64 codiert

            String zeitpunkt_decoded = new String(Base64.getDecoder().decode(zeitpunkt.getBytes()));
            offlineDB.addDrink(new Drink(name, menge, prozent, zeitpunkt_decoded));
            return true;

        }else{

            String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Drink/add", hashMap);
            Log.i("test", json);

            return json.equals("[{\"status\":\"erfolg\"}]");
        }

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        lokalSpeichern.setHinzugefuegt(true);

        progressBar.setVisibility(View.INVISIBLE);
    }
}
