package net.mbmedia.drunkmerlin.DB_online.User;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.JsonParser;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.Toaster;

import java.util.HashMap;

public class DBRegister extends AsyncTask {

    private String username;
    private String pw_hash;

    private Context context;

    private ProgressBar progressBar;

    HashMap<String, String> hashMap = new HashMap<>();


    private net.mbmedia.drunkmerlin.DB_online.DBFunktionen DBFunktionen = new DBFunktionen();
    private JsonParser jsonParser = new JsonParser();

    private LokalSpeichern lokalSpeichern;

    private Toaster toaster;



    public DBRegister(Context context, String username, String pw_hash, String hash, ProgressBar progress){
        this.username = username;
        this.pw_hash = pw_hash;
        this.context = context;
        this.progressBar = progress;


        hashMap.put("hash", hash);
        hashMap.put("username", username);
        hashMap.put("pw", pw_hash);

        lokalSpeichern = new LokalSpeichern(context);

        toaster = new Toaster(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected Object doInBackground(Object[] objects) {

        String json_string = DBFunktionen.getJSON(context, Konfiguration.URL + "/User/register", hashMap);
        Log.i("test", json_string);
        String id = jsonParser.getID(json_string);

        System.out.println(id);

        if((id.equals(Konfiguration.FEHLER)) || (id.equals(Konfiguration.BREITS_VERGEBEN))){
            toaster.sage(Konfiguration.BREITS_VERGEBEN);
            return null;
        }else{
            System.out.println("id: " + id);
            LokalSpeichern.setUserID(id);

        }



        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        progressBar.setVisibility(View.INVISIBLE);
    }


}
