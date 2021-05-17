package net.mbmedia.drunkmerlin.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;


import net.mbmedia.drunkmerlin.DB_online.User.DBDeleteAccount;
import net.mbmedia.drunkmerlin.Localization;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.Toaster;

public class EinstellungenActivity extends ActivityFunktionen {

    private LokalSpeichern lokalSpeichern;
    private Toaster toaster;
    private Localization localization;

    private Context context = this;

    private EditText txt_gewicht;
    private AutoCompleteTextView txt_sex;
    private Switch offlineModus;
    //private Switch englisch;
    private TextView btn_speichern;
    private TextView btn_logout;
    private TextView btn_delAccount;
    private ProgressBar progressBar;

    public final String Constant_SEX_M = "männlich";
    public final String Constant_SEX_W = "weiblich";
    public final String Constant_SEX_M_en = "male";
    public final String Constant_SEX_W_en = "female";
    public String SEX_M = "";
    public String SEX_W = "";

    @Override
    public void onBackPressed() {

        if(lokalSpeichern.getGewicht().equals("null") || lokalSpeichern.getGeschlecht().equals("null")){
            toaster.sage(getApplicationContext().getResources().getString(R.string.text_warnung_speichern));
        }else{

            Intent intent = new Intent(this, HauptActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_einstellungen);

        localization = new Localization(this);
        lokalSpeichern = new LokalSpeichern(this);
        toaster = new Toaster(this);

        SEX_M = context.getResources().getString(R.string.Sex_M);
        SEX_W = context.getResources().getString(R.string.Sex_W);

        initGUI();

        Intent intent = getIntent();
        if(intent.getStringExtra(LoginActivity.ERSTER_START) != null){
            zeigeWarnung();
        }



        btn_speichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()){
                    speichern();
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lokalSpeichern.deleteAll();
                finishAffinity();
            }
        });

        /*englisch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    localization.setAppLocale("en");
                }else{
                    localization.setAppLocale("de");
                }
            }
        });*/

    }

    private void zeigeWarnung(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getApplicationContext().getResources().getString(R.string.Warnung))
                .setTitle(getApplicationContext().getResources().getString(R.string.title_warnung))
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void speichern(){
        String sex = txt_sex.getText().toString().trim();
        String gewicht = txt_gewicht.getText().toString().trim();
        boolean isChecked = offlineModus.isChecked();

        switch (sex){
            case Constant_SEX_M: lokalSpeichern.setGeschlecht("m");
                break;
            case Constant_SEX_W: lokalSpeichern.setGeschlecht("w");
                break;
            case Constant_SEX_M_en: lokalSpeichern.setGeschlecht("m");
                break;
            case Constant_SEX_W_en: lokalSpeichern.setGeschlecht("w");
                break;
            default: lokalSpeichern.setGeschlecht("null");
                break;
        }

        lokalSpeichern.setGewicht(gewicht);

        lokalSpeichern.setOfflineModus(isChecked);

        //lokalSpeichern.setEnglischeSprache(englisch.isChecked());



        Intent intent = new Intent(EinstellungenActivity.this, HauptActivity.class);
        startActivity(intent);
        finish();
    }

    private Boolean checkInputs(){
        if(txt_gewicht.length() < 1){
            toaster.sage("Bitte richtiges Gewicht in KG angeben");
            return false;
        }


        if(txt_sex.getText().toString().trim().equals(SEX_M) || txt_sex.getText().toString().trim().equals(SEX_W)){
            //do nothing
        }else{
            System.out.println(txt_sex.getText());
            toaster.sage("Bitte Geschlecht als \"" + SEX_W + "\" oder \"" + SEX_M + "\" angeben");
            return false;
        }

        return true;
    }

    private void initGUI(){
        txt_gewicht = findViewById(R.id.txt_gewicht);
        txt_sex = findViewById(R.id.txt_sex);
        offlineModus = findViewById(R.id.switch_offline);
        btn_speichern = findViewById(R.id.btn_speichern);
        btn_logout = findViewById(R.id.btn_logout);
        btn_delAccount = findViewById(R.id.btn_deleteAccount);
        //englisch = findViewById(R.id.switch_englisch);
        progressBar = findViewById(R.id.progress);

        String[] gender = {SEX_W, SEX_M};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gender);
        txt_sex.setAdapter(adapter);
        txt_sex.setThreshold(0);
        txt_sex.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("onTouch");
                txt_sex.showDropDown();
                return false;
            }
        });

        fuelleVoreingestelltes();

        btn_delAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });

    }

    private void deleteAccount(){
        //warnung zeigen

        Resources r = getApplicationContext().getResources();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(r.getString(R.string.txt_account_loeschen))
                .setTitle(r.getString(R.string.title_warnung))
                .setNegativeButton(r.getString(R.string.btn_abbrechen), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setPositiveButton(r.getString(R.string.txt_deleteAccount), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBDeleteAccount db = new DBDeleteAccount(EinstellungenActivity.this, getHash(), progressBar, lokalSpeichern);
                        db.execute();
                    }
                });
        builder.create().show();
    }

    private void fuelleVoreingestelltes(){
        String sex = lokalSpeichern.getGeschlecht();
        String gewicht = lokalSpeichern.getGewicht();
        Boolean isChecked = lokalSpeichern.isOfflineModus();

        switch (sex){
            case "m": txt_sex.setText(SEX_M);
                break;
            case "w": txt_sex.setText(SEX_W);
                break;
            default: //do nothing
                break;
        }

        switch (gewicht){
            case "null": //do nothing
                break;
            default: txt_gewicht.setText(gewicht);
        }


        offlineModus.setChecked(isChecked);

        /*if(localization.getAppLocale().toLowerCase().contains("en")){
            englisch.setChecked(true);
        }*/

    }
}
