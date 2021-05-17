package net.mbmedia.drunkmerlin.DB_online.Drink;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import net.mbmedia.drunkmerlin.DB_offline.OfflineDBHelper;
import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.TO.Drink;

import java.util.HashMap;

public class DBdelDrink extends AsyncTask {

    private String drinkID;
    private String nutzerID;
    private String hash;
    HashMap<String, String> hashMap = new HashMap<>();

    private ProgressBar progressBar;

    private DBFunktionen dbFunktionen = new DBFunktionen();

    private OfflineDBHelper dbHelper;
    private LokalSpeichern lokalSpeichern;

    private Context context;

    private static final String LOGTAG = "DBdelDrink";


    public DBdelDrink(Context context, String hash, String drinkID, String nutzerID){
        this.hash = hash;
        this.drinkID = drinkID;
        this.nutzerID = nutzerID;
        this.context = context;

        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzerID);
        hashMap.put("drinkid", drinkID);

        dbHelper = new OfflineDBHelper(context);
        lokalSpeichern = new LokalSpeichern(context);

    }

    @Override
    protected Boolean doInBackground(Object[] objects) {

        if(lokalSpeichern.isOfflineModus()){

            Drink tmpDrink = new Drink();
            tmpDrink.setId(drinkID);

            dbHelper.delDrink(tmpDrink);

            return true;

        }else{
            String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Drink/del",hashMap);

            Log.i(LOGTAG, json);

            return json.equals("[{\"status\":\"erfolg\"}]");
        }



    }

}
