package net.mbmedia.drunkmerlin.DB_online.Drink;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import net.mbmedia.drunkmerlin.DB_offline.OfflineDBHelper;
import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.DB_online.User.PromilleUpdate;
import net.mbmedia.drunkmerlin.JsonParser;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.TO.Drink;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DBPromilleZeit extends AsyncTask {

    private final String LOGTAG = "DBPROMILLEZEIT";

    private TextView txt_promille;
    private TextView txt_zeit;

    private Context context;

    HashMap<String, String> hashMap = new HashMap<>();
    private String hash;

    private String promille;
    private String zeit;

    public ArrayList<Drink> drinks;

    private JsonParser parser = new JsonParser();

    private DBFunktionen dbFunktionen = new DBFunktionen();
    private LokalSpeichern lokalSpeichern;
    private OfflineDBHelper dbHelper;


    private String Anfangs_Zeit;

    private Boolean drinkInitialisiert = false;



    public DBPromilleZeit(Context context, TextView promille, TextView zeit, String hash){
        this.context = context;
        this.txt_promille = promille;
        this.txt_zeit = zeit;
        this.hash = hash;

        lokalSpeichern = new LokalSpeichern(context);
        dbHelper = new OfflineDBHelper(context);

        hashMap.put("hash", hash);
        hashMap.put("nutzerid", lokalSpeichern.getUserID());


    }




    @Override
    protected Object doInBackground(Object[] objects) {


        if(lokalSpeichern.isOfflineModus()){

            Log.i(LOGTAG, "ist offline");

            drinks = dbHelper.getAllDrinks();
            drinkInitialisiert = true;

        }else{
            String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Drink/show", hashMap);
            Log.i(LOGTAG, json);
            try {
                Log.i("JSON", json);
                drinks = parser.getDrinks(json);
                drinkInitialisiert = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);


        if(drinkInitialisiert){
            Log.i(LOGTAG, "drink ist initialisiert");


            for(int i=0; i<drinks.size(); i++){
                Log.i(LOGTAG, "drink: " + drinks.get(i).getName() + " " + drinks.get(i).getMenge() + " " + drinks.get(i).getProzent() + " " + drinks.get(i).getZeit());
            }

            String[] zeit_array = berechne_Zeit_aus_Promille(berechne_Promille_ohne_Zeit(drinks));

            String zeit_wert = "in " + zeit_array[0] + "h "+ context.getResources().getString(R.string.txt_und) + " " + zeit_array[1] + "min " + context.getResources().getString(R.string.txt_wieder_nuechtern);
            txt_zeit.setText(zeit_wert);


            String promille = String.valueOf(PromilleausZeit(zeit_array));

            txt_promille.setText(promille + "‰");

            Set<String> hash_Set = new HashSet<String>();
            hash_Set.add(promille + "‰");
            hash_Set.add(zeit_wert);

            lokalSpeichern.setLetzterPromilleWert(hash_Set);

            updatePromille(promille);



            if((drinks.size() > 0) && (promille.equals("0.0"))){
                lokalSpeichern.DrinkListeSollgeloeschtwerden(true);
            }else{
                lokalSpeichern.DrinkListeSollgeloeschtwerden(false);
            }

        }


    }



    private void updatePromille(String p){
        PromilleUpdate promilleUpdate = new PromilleUpdate(context, hash, lokalSpeichern.getUserID(), p);
        promilleUpdate.execute();
    }


    public Double berechne_Promille_ohne_Zeit(ArrayList<Drink> drink){
        String logtag = "berechne_Promille";


        Double promille_gesamt = 0.0;

        int l = drink.size();

        for(int i=0; i<l; i++){
            Drink tmp_drink = new Drink();
            tmp_drink = drink.get(i);
            tmp_drink.setPromille(getPromillefromProzentUndMenge(tmp_drink.getProzent(), tmp_drink.getMenge(), lokalSpeichern.getGeschlecht(), lokalSpeichern.getGewicht()));
            drink.set(i, tmp_drink);
            promille_gesamt = promille_gesamt + Double.valueOf(tmp_drink.getPromille());

            if(i==l-1){
                Log.i(LOGTAG, "tmpdrin: " + tmp_drink.getZeit());
                Anfangs_Zeit = tmp_drink.getZeit();
            }

        }
        Log.i(logtag, "promille_gesamt: " + promille_gesamt);

        return promille_gesamt;

    }

    public String getPromillefromProzentUndMenge(String prozent, String menge, String geschlecht, String gewicht){
        String logtag = "PromillefromProzentMeng";
        //String geschlecht = "W";
        //String gewicht = "70"; in kg
        //String menge = "0,5"; in L
        //String prozent = "4,8";
        //Beispieleingaben

        Log.i(logtag, "geschlecht: " + geschlecht);
        Log.i(logtag, "gewicht: " + gewicht);

        double gewicht_double;

        if((geschlecht == null) || geschlecht.equals("null")){
            gewicht_double = 70.0;
        }else{
            gewicht_double = Double.parseDouble(gewicht);
        }



        double z_promille = 0;

        //alkohol gramm berechnen
        double gramm = alkohol_gramm(menge, prozent);
        gramm = gramm * 0.8; //es gelangen nur ca 80% in den blutkreislauf

        if((geschlecht.equals("m")) || (geschlecht.equals("M"))){
            z_promille = gramm / (gewicht_double * 0.68);
        }else if((geschlecht.equals("w")) || (geschlecht.equals("W"))){
            z_promille = gramm / (gewicht_double * 0.55);
        }

        return String.valueOf(z_promille).replace(",",".").trim();
    }

    public double alkohol_gramm(String menge, String prozent){
        double gramm = 0;
        double menge_double = Double.parseDouble(menge);
        double prozent_double = Double.parseDouble(prozent);

        gramm = menge_double * prozent_double * 8;
        return gramm;
    }

    public String[] berechne_Zeit_aus_Promille(double Promille_gesamt){
        String logtag = "berechne_Zeit";

        String[] zeit = new String[2];

        double minuten_ges = Promille_gesamt * 400;

        Log.i(logtag, "Promille_gesamt: " + Promille_gesamt);

        try {
            Log.i(logtag, "minuten_ges: " + minuten_ges + "  zeitvergangen_minuten: " + ZeitDifferenz(Anfangs_Zeit));
            minuten_ges = minuten_ges - ZeitDifferenz(Anfangs_Zeit);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i(logtag, "minuten_ges: " + minuten_ges);

        if(minuten_ges < 0 ){
            String[] zeit2 = new String[2];
            zeit2[0] = "0";
            zeit2[1] = "0";
            return zeit2;
        }

        double stunden = (minuten_ges - (minuten_ges % 60)) / 60 ;
        double minuten = minuten_ges % 60;

        String stunden_string = String.valueOf(stunden).substring(0,String.valueOf(stunden).indexOf("."));
        String minuten_string = String.valueOf(minuten).substring(0,String.valueOf(minuten).indexOf("."));

        zeit[0] = stunden_string; //stunden
        zeit[1] = minuten_string; //minuten

        Log.i(logtag, "stunden: " + zeit[0]);
        Log.i(logtag, "minuten: " + zeit[1]);

        return zeit;

    }

    public double ZeitDifferenz(String anfang) throws ParseException {
        String logtag = "Diff_Zeit";
        //anfang: 2018-12-19 18:40:64

        Log.i(logtag, "anfang: " + anfang);

        if(anfang == null){
            Log.i(logtag, "anfang == null");
            return 0.0;
        }


        Timestamp timestamp_anfang = StringtoTimestamp(anfang);

        Date date = new Date();
        Timestamp timestamp_jetzt = new Timestamp(date.getTime());
        Log.i(logtag, "timestamp_jetz: " + timestamp_jetzt);
        Log.i(logtag, "timestamp_anfang: " + timestamp_anfang);



        long milliseconds = timestamp_jetzt.getTime() - timestamp_anfang.getTime();
        int seconds = (int) milliseconds / 1000;

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = (seconds % 3600) % 60;

        Log.i(logtag, "diff: " + "hours: " + hours + " min: " + minutes + " sec: " + seconds);

        double minutes_ges = (60.0 * hours) + minutes;

        Log.i(logtag, "minutes_ges: " + minutes_ges);

        return minutes_ges;
    }

    public Timestamp StringtoTimestamp(String a){
        final String logtag = "StringtoTimestamp";
        Log.i(logtag, a);
        //input: Jan 25, 2021 8:51:00 PM
        Date date = new Date(a);
        Timestamp t = new java.sql.Timestamp(date.getTime());
        return t;
        /*
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(a);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            Log.i(logtag, "a: " + a);
            Log.i(logtag, "Timestamp: " + timestamp);
            return timestamp;
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }

        return null;
         */
    }

    public double PromilleausZeit(String[] zeit){
        String logtag = "PromilleausZeit";

        Log.i(logtag, "zeit[0]: " + zeit[0] + "  zeit[1]: " + zeit[1]);

        double promillealsminuten = (Double.parseDouble(zeit[0]) * 60) + Double.parseDouble(zeit[1]);
        Log.i(logtag, "promillealsminuten: " + promillealsminuten);

        double Promille = promillealsminuten / 400;
        Log.i(logtag, "Promille: " + Promille);

        return Promille;
    }








}
