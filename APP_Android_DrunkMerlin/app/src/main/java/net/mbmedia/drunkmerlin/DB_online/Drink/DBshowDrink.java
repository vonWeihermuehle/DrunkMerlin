package net.mbmedia.drunkmerlin.DB_online.Drink;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.mbmedia.drunkmerlin.Activity.UebersichtActivity;
import net.mbmedia.drunkmerlin.DB_offline.OfflineDBHelper;
import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.JsonParser;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.TO.Drink;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class DBshowDrink extends AsyncTask {

    private String hash;
    private String nutzerID;
    HashMap<String, String> hashMap = new HashMap<>();

    private Context context;

    private JsonParser parser = new JsonParser();

    private DBFunktionen dbFunktionen = new DBFunktionen();

    private OfflineDBHelper dbHelper;

    private LokalSpeichern lokalSpeichern;

    public ArrayList<Drink> drinks;

    private LinearLayout layout_story;

    final Integer left = 70;

    private ProgressBar progressBar;


    public DBshowDrink(Context context, String hash, String nutzerID, LinearLayout layout, ProgressBar progressBar){
        this.hash = hash;
        this.nutzerID = nutzerID;
        this.context = context;
        this.layout_story = layout;

        this.progressBar = progressBar;

        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzerID);

        lokalSpeichern = new LokalSpeichern(context);
        dbHelper = new OfflineDBHelper(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        if(lokalSpeichern.isOfflineModus()){

            drinks = dbHelper.getAllDrinks();

        }else{
            try {
                String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Drink/show", hashMap);
                Log.i("test", json);
                drinks = parser.getDrinks(json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(!(drinks == null)){
            initStory(drinks);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }


    private void deleteDrink(String drinkID){

        final String id = drinkID;
        progressBar.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    DBdelDrink dBdelDrink = new DBdelDrink(context, hash, id, nutzerID);
                    Boolean erfolgreichgeloescht = (Boolean) dBdelDrink.execute().get();

                    if(erfolgreichgeloescht){
                        progressBar.setVisibility(View.INVISIBLE);

                        ((UebersichtActivity)context).runOnUiThread(new Runnable() {
                            public void run() {
                                DBshowDrink dBshowDrink = new DBshowDrink(context, hash, nutzerID, layout_story, progressBar);
                                dBshowDrink.execute();
                            }
                        });

                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public void initStory(final ArrayList<Drink> drinks){
        int l = drinks.size();

        layout_story.removeAllViews();

        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams trennerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);

        LinearLayout.LayoutParams ImageViewParams = new LinearLayout.LayoutParams(20, 20);
        ImageViewParams.gravity= Gravity.CENTER;

        LinearLayout.LayoutParams TextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextViewParams.setMargins(15, 0, 0, 0);

        LinearLayout.LayoutParams TextViewParams_Info = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextViewParams_Info.setMargins(40, 0, 0, 10);


        for(int k=0; k<l;k++){
            final int t = k;

            LinearLayout ll = new LinearLayout(context);
            ll.setLayoutParams(LLParams);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setPadding(16, 16, 16, 16);


            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(ImageViewParams);
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.forward_arrow));
            imageView.setColorFilter(Color.argb(255, 255, 255, 255));

            TextView textView_Was = new TextView(context);
            textView_Was.setText(drinks.get(k).getName());
            textView_Was.setLayoutParams(TextViewParams);
            textView_Was.setTextColor(Color.argb(255, 255, 255, 255));
            textView_Was.setTextSize(16);

            final TextView textView_infos = new TextView(context);
            textView_infos.setText(context.getResources().getString(R.string.hint_menge)+": " + FormatMenge(drinks.get(k).getMenge()) + "\n" + context.getResources().getString(R.string.hint_percentage)+": " + drinks.get(k).getProzent() + " % \n" + context.getResources().getString(R.string.hint_time)+": " + FormatZeit(drinks.get(k).getZeit()));
            textView_infos.setLayoutParams(TextViewParams_Info);
            textView_infos.setTextColor(Color.argb(255, 255, 255, 255));
            textView_infos.setTextSize(16);
            textView_infos.setVisibility(View.GONE);

            final TextView textView_infos_final = textView_infos;


            LinearLayout trenner = new LinearLayout(context);
            trenner.setLayoutParams(trennerParams);
            trenner.setBackgroundColor(Color.argb(255, 255, 255, 255));

            ll.addView(imageView);
            ll.addView(textView_Was);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(textView_infos_final.getVisibility() == View.GONE){
                        textView_infos_final.setVisibility(View.VISIBLE);
                    }else{
                        textView_infos_final.setVisibility(View.GONE);
                    }


                }
            });

            textView_infos_final.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView_infos_final.setVisibility(View.GONE);
                }
            });


            ll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getResources().getString(R.string.txt_soll) + " \"" + drinks.get(t).getName() + "\" " + context.getResources().getString(R.string.txt_entfernen) + "?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // CONFIRM

                                    deleteDrink(drinks.get(t).getId());


                                }
                            })
                            .setNegativeButton(context.getResources().getString(R.string.btn_abbrechen), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // CANCEL
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create().show();



                    return false;
                }
            });

            layout_story.addView(ll);
            layout_story.addView(textView_infos_final);
            layout_story.addView(trenner);


        }



    }

    private String FormatMenge(String input){
        //input
        //0.2

        Double wert = Double.valueOf(input);
        wert = wert * 1000;

        String output = String.valueOf(wert);

        return output.substring(0, output.indexOf(".")) + " ml";
    }


    public static String FormatZeit(String s){
        String logtag = "FormatZeit";
        //input 2018-12-12 10:10:10

        Date date = new Date(s);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        String ausgabe = formatter.format(date);
        return ausgabe + " Uhr";
        /*

        String uhr = s.substring(10, 16) + " Uhr";
        String datum_vorher = s.substring(0,10);

        String[] datum_split = datum_vorher.split("-");

        String datum_nachher = datum_split[2] + "." + datum_split[1] + "." +datum_split[0];


        return datum_nachher + " " + uhr;

         */
    }
}
