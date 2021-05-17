package net.mbmedia.drunkmerlin.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import net.mbmedia.drunkmerlin.Bouncer;
import net.mbmedia.drunkmerlin.DB_online.Drink.DBaddDrink;
import net.mbmedia.drunkmerlin.Dialog.Mengepicker;
import net.mbmedia.drunkmerlin.Dialog.Prozentpicker;
import net.mbmedia.drunkmerlin.Dialog.Zeitpicker;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.Toaster;

import java.util.Base64;

import static java.lang.Thread.sleep;

public class BarActivity extends ActivityFunktionen {

    private ImageView btn_wein;
    private ImageView btn_cocktail;
    private ImageView btn_bier;
    private ImageView btn_schnaps;

    private EditText name;

    private TextView menge;
    private TextView prozent;
    private TextView zeit;
    private TextView btn_add;

    private ProgressBar progressBar;

    private Context context = this;

    private Thread thread;

    private LokalSpeichern lokalSpeichern;

    private Boolean warten_weil_button_gedrueckt = false;

    private Boolean Thread_vorzeitig_beenden = false;


    @Override
    protected void onDestroy() {
        super.onDestroy();

        lokalSpeichern.setHinzugefuegt(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bar);

        initGUI();

        lokalSpeichern = new LokalSpeichern(this);


        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;

                while(true){
                    if(i>200){
                        //nach 200 durchläufen - knapp 3 minuten - soll der Thread beendet sein
                        return;
                    }else{
                        i++;
                    }

                    if(Thread_vorzeitig_beenden){
                        return;
                    }

                    if(warten_weil_button_gedrueckt){
                        if(lokalSpeichern.isHinzugefuegt()){
                            lokalSpeichern.setHinzugefuegt(false);
                            Intent hauptintent = new Intent(BarActivity.this, HauptActivity.class);
                            startActivity(hauptintent);
                            finish();
                            return;
                        }
                    }



                    System.out.println("Bar Thread läuft");

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        thread.start();

        setLetzteEingabe();


    }

    @Override
    public void onBackPressed() {

        if(warten_weil_button_gedrueckt){
            return;
        }else{
            Thread_vorzeitig_beenden = true;
            Intent intent = new Intent(this, HauptActivity.class);
            startActivity(intent);
            finish();
        }


    }




    private void setShot(){
        name.setText("Shot");
        menge.setText("20 ml");
        prozent.setText("40 %");

        animateTrinkenButton();
    }

    private void setWein(){
        name.setText("Wein");
        menge.setText("200 ml");
        prozent.setText("12 %");

        animateTrinkenButton();
    }

    private void setCocktail(){
        name.setText("Cocktail");
        menge.setText("300 ml");
        prozent.setText("8 %");

        animateTrinkenButton();
    }

    private void setBier(){
        name.setText("Bier");
        menge.setText("500 ml");
        prozent.setText("5 %");

        animateTrinkenButton();
    }

    private void animateTrinkenButton(){
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        Bouncer interpolator = new Bouncer(0.1, 20);
        myAnim.setInterpolator(interpolator);

        btn_add.startAnimation(myAnim);
    }


    private void initGUI(){
        btn_schnaps = findViewById(R.id.btn_schnaps);
        btn_wein = findViewById(R.id.btn_wein);
        btn_cocktail = findViewById(R.id.btn_cocktail);
        btn_bier = findViewById(R.id.btn_bier);
        btn_add = findViewById(R.id.btn_add);

        name = findViewById(R.id.txt_name);
        menge = findViewById(R.id.txt_menge);
        prozent = findViewById(R.id.txt_prozent);
        zeit = findViewById(R.id.txt_zeit);

        progressBar = findViewById(R.id.progress);



        btn_schnaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShot();
            }
        });

        btn_wein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWein();
                System.out.println("wein");

            }
        });

        btn_cocktail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCocktail();
            }
        });

        btn_bier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBier();
            }
        });

        zeit.setText(getDatumZeit());

        zeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog_zeit = new Zeitpicker(zeit);
                dialog_zeit.show(getSupportFragmentManager(), "timePicker");
            }
        });

        prozent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog_prozent = new Prozentpicker(prozent);
                dialog_prozent.show(getSupportFragmentManager(), "prozentpicker");
            }
        });

        menge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog_menge = new Mengepicker(menge);
                dialog_menge.show(getSupportFragmentManager(), "mengepicker");
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                Toaster toaster = new Toaster(context);

                if(!checkObVariablenGesetzt()){
                    toaster.sage("Bitte erst alle Variablen setzen");
                    return;
                }

                LokalSpeichern speichern = new LokalSpeichern(context);

                speichern.setLetzteBarEingabe(name.getText().toString().trim() + "," + menge.getText().toString().trim() + "," + prozent.getText().toString().trim());




                String zeitconvertiert = ZeitConverter(zeit.getText().toString().trim());


                String ZeitasBase64 = Base64.getEncoder().encodeToString(zeitconvertiert.getBytes());

                String base64LatLng = Base64.getEncoder().encodeToString(lokalSpeichern.getLetzterStandort().getBytes());

                warten_weil_button_gedrueckt = true;


                DBaddDrink dBaddDrink = new DBaddDrink(context, getHash(), speichern.getUserID(), name.getText().toString().trim(), berechneMenge(menge.getText().toString()), prozent.getText().toString().replace(" %", ""), ZeitasBase64, base64LatLng, progressBar);
                dBaddDrink.execute();

            }
        });



    }

    private void setLetzteEingabe(){
        LokalSpeichern speichern = new LokalSpeichern(context);
        String s = speichern.getLetzteBarEingabe();

        if(s.equals("null")){
            //do nothing
        }else if(s.contains(",")){
            String[] tmp = s.split(",");
            name.setText(tmp[0]);
            menge.setText(tmp[1]);
            prozent.setText(tmp[2]);
        }
    }

    private Boolean checkObVariablenGesetzt(){
        if(name.getText().length() < 1){
            return false;
        }

        if(!menge.getText().toString().contains("ml")){
            return false;
        }

        return prozent.getText().toString().contains("%");

    }

    private String ZeitConverter(String s){
        //aus 16:19 27.12.2019
        //soll 2019-12-27 16:19
        //werden

        String neu = s.substring(12, 16) + "-" + s.substring(9, 11) + "-" + s.substring(6, 8) + " " + s.substring(0, 5);
        System.out.println("Zeitconvertier: " + neu);
        return neu;
    }

    private String berechneMenge(String menge){
        String s = menge.replace(" ml", "");

        Double menge_vorberechnung = Double.valueOf(s);

        return String.valueOf(menge_vorberechnung / 1000);
    }




}
