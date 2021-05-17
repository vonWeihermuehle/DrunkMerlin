package net.mbmedia.drunkmerlin.Activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.mbmedia.drunkmerlin.DB_online.Drink.DBdelAllDrink;
import net.mbmedia.drunkmerlin.DB_online.Drink.DBshowDrink;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.R;

public class UebersichtActivity extends ActivityFunktionen {

    private LokalSpeichern lokalSpeichern = new LokalSpeichern(this);

    private LinearLayout linearLayout;

    private ProgressBar progressBar;

    private TextView btn_delAll;

    private Context context = this;

    private Boolean sollListeGeloeschtWerden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_uebersicht);

        Intent intent = getIntent();
        sollListeGeloeschtWerden = intent.getBooleanExtra(HauptActivity.LISTELOESCHEN_FLAG, false);


        linearLayout = findViewById(R.id.layout);
        progressBar = findViewById(R.id.progress);
        btn_delAll = findViewById(R.id.btn_delAll);

        btn_delAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getResources().getString(R.string.txt_alles_loeschen))
                        .setTitle(context.getResources().getString(R.string.title_warnung))
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delAll();
                            }
                        })
                        .setPositiveButton(context.getResources().getString(R.string.btn_abbrechen), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
            }
        });

        zeigeDrinks();

        if(sollListeGeloeschtWerden){
            delAll();
        }


    }

    private void delAll(){
        DBdelAllDrink dBdelAllDrink = new DBdelAllDrink(this, getHash(), lokalSpeichern.getUserID(), linearLayout, progressBar);
        dBdelAllDrink.execute();
    }

    private void zeigeDrinks(){
        DBshowDrink dBshowDrink = new DBshowDrink(this, getHash(), lokalSpeichern.getUserID(), linearLayout, progressBar);
        dBshowDrink.execute();
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HauptActivity.class);
        startActivity(intent);
        finish();
    }
}
