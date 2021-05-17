package net.mbmedia.drunkmerlin.DB_online.User;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import net.mbmedia.drunkmerlin.Activity.LoginActivity;
import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.JsonParser;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.Toaster;

import java.util.HashMap;

public class DBLogin extends AsyncTask {

    private String hash;
    private String username;
    private String pw;
    private String id;

    private Context context;

    private ProgressBar progress;


    private DBFunktionen fun = new DBFunktionen();

    private JsonParser parser = new JsonParser();

    private LokalSpeichern LokalSpeichern;

    private Toaster toaster;

    //http://localhost:8000/login?hash=test&username=maxpw=2ffe4e77325d9a7152f7086ea7aa5114
    HashMap<String, String> hashMap = new HashMap<>();

    private static final String LOGTAG = "DBLOGIN";


    public DBLogin(Context context, String hash, String username, String pw, ProgressBar progress){
        this.hash = hash;
        this.username = username;
        this.pw = pw;
        this.context = context;
        this.progress = progress;

        hashMap.put("hash", hash);
        hashMap.put("username", username);
        hashMap.put("pw", pw);

        toaster = new Toaster(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress.setVisibility(View.VISIBLE);
    }



    @Override
    protected Boolean doInBackground(Object... objects) {
        String json = fun.getJSON(context, Konfiguration.URL + "/User/login", hashMap);

        Log.i(LOGTAG, json);

        id = parser.getID(json);

        //Log.i(LOGTAG, id);

        return true;

    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);


        progress.setVisibility(View.INVISIBLE);

        if(id.equals(Konfiguration.FEHLER)){
            toaster.sage("Benutzername oder Passwort falsch");
            return;
        }else{
            LokalSpeichern = new LokalSpeichern(context);
            System.out.println("id: " + id);
            net.mbmedia.drunkmerlin.LokalSpeichern.setUserID(id);

            return;
        }


    }


}
