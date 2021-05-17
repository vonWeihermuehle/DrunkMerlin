package net.mbmedia.drunkmerlin.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mbmedia.drunkmerlin.BuildConfig;
import net.mbmedia.drunkmerlin.DB_online.Drink.DBPromilleZeit;
import net.mbmedia.drunkmerlin.Dialog.DialogNotification;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.Location;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.Notification.NotificationService;
import net.mbmedia.drunkmerlin.R;

import java.util.Iterator;
import java.util.Set;

public class HauptActivity extends ActivityFunktionen {

    private final String LOGTAG = "HauptActivity";

    private LokalSpeichern lokalSpeichern;

    private TextView txt_promille;
    private TextView txt_zeit;
    private LinearLayout btn_bar;
    private LinearLayout btn_uebersicht;
    private LinearLayout btn_challenge;

    private ImageView btn_setting;
    private ImageView btn_danke;


    private Thread aktualisierThread;

    private Boolean sollThreadlaufen = true;

    private Context context = this;

    public static final String LISTELOESCHEN_FLAG = "drinkListeloeschen";

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(LOGTAG, "onDestroy");

        sollThreadlaufen = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkIfNotificationisLoaded();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LOGTAG, "onCreate");

        setContentView(R.layout.activity_main);
        lokalSpeichern = new LokalSpeichern(this);
        initGUI();


        btn_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent barintent = new Intent(HauptActivity.this, BarActivity.class);
                startActivity(barintent);
                finish();
            }
        });

        btn_uebersicht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Uebersicht = new Intent(HauptActivity.this, UebersichtActivity.class);
                startActivity(Uebersicht);
                finish();
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Einstellung = new Intent(HauptActivity.this, EinstellungenActivity.class);
                startActivity(Einstellung);
                finish();
            }
        });

        btn_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Challenge = new Intent(HauptActivity.this, ChallengeActivity.class);
                startActivity(Challenge);
            }
        });




        aktualisierePromilleZeit();

        if(lokalSpeichern.sollDrinkListegeloeschtwerden()){
            //dann sollte man die Drinks löschen
            Log.i(LOGTAG, "liste zurücksetzen");
            frageObDrinkListeZurueckgesetztwerdensoll();
        }

        speichereLetzteStandort();

        showTeilenAufforderung();

        checkNotificationService();
        checkIfNotificationisLoaded();





    }

    private void checkIfNotificationisLoaded(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            DialogNotification drinkUebersicht = new DialogNotification((Activity) context, bundle.getString(NotificationService.NOTIFI_FLAG), bundle.getString(NotificationService.AUTHOR_FLAG));
            drinkUebersicht.getWindow().setBackgroundDrawableResource(android.R.color.black);
            drinkUebersicht.show();
        }
    }

    private void checkNotificationService(){
        if(isMyServiceRunning(NotificationService.class)){
            //do nothing
        }else {
            startService(new Intent(getApplicationContext(), NotificationService.class));
        }
    }

    private void showTeilenAufforderung(){
        if(lokalSpeichern.isAppNiemalsTeilen()){
            return;
        }

        int zaehler = lokalSpeichern.getTeilenAufforderung();

        if(zaehler == Konfiguration.TEILEN_AUFFORDERUNG_MIN){
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getResources().getString(R.string.txt_teilen))
                    .setTitle(context.getResources().getString(R.string.title_teilen))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            app_und_promille_teilen();
                            lokalSpeichern.setTeilenAufforderung(0);
                        }
                    })
                    .setNegativeButton(context.getResources().getString(R.string.btn_abbrechen), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            lokalSpeichern.setTeilenAufforderung(0);
                            //zähler wieder auf 0 setzen
                        }
                    })
                    .setNeutralButton(context.getResources().getString(R.string.txt_nie), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            lokalSpeichern.setAppNiemalsTeilen(true);
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();
        }else{
            zaehler += 1;
            lokalSpeichern.setTeilenAufforderung(zaehler);
        }
    }


    private void app_und_promille_teilen(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getResources().getText(R.string.app_name));

        String shareMessage="\n" + context.getResources().getString(R.string.txt_hab_anscheinend) + " " + txt_promille.getText().toString() + " " + context.getResources().getString(R.string.txt_alkohol_im_blut) + "..." + "\n" + context.getResources().getString(R.string.txt_schau_die_app_an) + "\n\n";


        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);


        try {
            startActivity(shareIntent);

        } catch (android.content.ActivityNotFoundException ex) {
            //print.print_short("Whatsapp ist nicht installiert...");
        }
    }


    private void speichereLetzteStandort(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Location location = new Location(context);
                location.setLocation();
            }
        });

        thread.start();
    }


    private void frageObDrinkListeZurueckgesetztwerdensoll(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.txt_der_komplette_alkohol_ist_weg))
                .setTitle(context.getResources().getString(R.string.txt_zuruecksetzen))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(HauptActivity.this, UebersichtActivity.class);
                        intent.putExtra(LISTELOESCHEN_FLAG, true);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.txt_noch_nicht), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void aktualisierePromilleZeit(){
        aktualisierThread = new Thread(){
            public void run(){


                while(sollThreadlaufen) {

                    Log.i(LOGTAG, "Thread arbeitet");

                    DBPromilleZeit dbPromilleZeit = new DBPromilleZeit(context, txt_promille, txt_zeit, getHash());
                    dbPromilleZeit.execute();

                    try {
                        sleep(Konfiguration.AKTUALISIERUNG);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        aktualisierThread.start();
    }

    private void initGUI(){
        txt_promille = findViewById(R.id.txt_promille);
        txt_zeit = findViewById(R.id.txt_zeit);
        btn_bar = findViewById(R.id.ll1);
        btn_uebersicht = findViewById(R.id.btn_uebersicht);
        btn_setting = findViewById(R.id.btn_setting);
        btn_challenge = findViewById(R.id.btn_challenge);
        btn_danke = findViewById(R.id.btn_thanks);

        btn_danke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent danke = new Intent(HauptActivity.this, DankeActivity.class);
                startActivity(danke);
            }
        });


        Set<String> hash_Set = lokalSpeichern.letzerPromilleZeitWert();

        Iterator iterator = hash_Set.iterator();
        while(iterator.hasNext()){
            String element = (String) iterator.next();

            if(element.contains("min")){
                txt_zeit.setText(element);
                System.out.println("hash_set " + element);
            }else if(element.contains("‰")){
                txt_promille.setText(element);
                System.out.println("hash_set " + element);
            }
        }

        if(lokalSpeichern.isOfflineModus()){
            btn_challenge.setVisibility(View.INVISIBLE);
        }else{
            btn_challenge.setVisibility(View.VISIBLE);
        }


    }
}
