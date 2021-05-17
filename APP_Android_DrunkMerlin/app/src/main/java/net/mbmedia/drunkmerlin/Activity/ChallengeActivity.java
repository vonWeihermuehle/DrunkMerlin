package net.mbmedia.drunkmerlin.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.mbmedia.drunkmerlin.BuildConfig;
import net.mbmedia.drunkmerlin.DB_online.Freund.DBaddFreund;
import net.mbmedia.drunkmerlin.DB_online.Freund.DBshowFreund;
import net.mbmedia.drunkmerlin.Dialog.QRCode;
import net.mbmedia.drunkmerlin.Location;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.Toaster;

import java.util.HashSet;
import java.util.Iterator;

import static java.lang.Thread.sleep;

public class ChallengeActivity extends ActivityFunktionen {

    private BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private ImageView btn_share;

    private LokalSpeichern lokalSpeichern;

    private Context context = this;

    private IntentIntegrator qrScan;

    private Toaster toaster = new Toaster(context);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        lokalSpeichern = new LokalSpeichern(this);
        initGUI();


        aktualisiereListe();

        speichereLetzteStandort();


    }

    @Override
    protected void onResume() {
        super.onResume();
        speichereLetzteStandort();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Abgebrochen", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                String freund_id = result.getContents().trim();

                addFreund(freund_id);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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


    private void addFreund(String freundID){
        DBaddFreund DBaddFreund = new DBaddFreund(this, getHash(), lokalSpeichern.getUserID(), freundID, linearLayout, progressBar);
        DBaddFreund.execute();

    }

    private void aktualisiereListe(){

        DBshowFreund dBshowFreund = new DBshowFreund(this, getHash(), lokalSpeichern.getUserID(), linearLayout, progressBar);
        dBshowFreund.execute();

    }

    private void QRCodeScanner() {
        qrScan.setOrientationLocked(false);
        qrScan.setPrompt("");
        qrScan.setBeepEnabled(false);
        qrScan.initiateScan();
    }

    private void showQRCode(){
        QRCode qrCode = new QRCode(this, lokalSpeichern.getUserID());
        qrCode.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                aktualisiereListe();
            }
        });
        qrCode.show();

    }

    private void initGUI(){
        bottomNavigationView = findViewById(R.id.nav_view);
        linearLayout = findViewById(R.id.layout);
        progressBar = findViewById(R.id.progress);
        btn_share = findViewById(R.id.btn_share);

        qrScan = new IntentIntegrator(this);


        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app_und_promille_teilen();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.qr_code:
                        showQRCode();
                        break;
                    case R.id.scan:
                        QRCodeScanner();
                        break;
                    case R.id.map:
                        starteMap();
                        break;
                }
                return true;
            }
        });
    }

    private String hole_letzte_Promille(){

        HashSet<String> hash_Set = (HashSet<String>) lokalSpeichern.letzerPromilleZeitWert();

        Iterator iterator = hash_Set.iterator();
        while(iterator.hasNext()){
            String element = (String) iterator.next();

            if(element.contains("‰")){
                return element;
            }
        }

        return "0.0‰";
    }

    private void app_und_promille_teilen(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getResources().getText(R.string.app_name));


        String shareMessage="\n" + context.getResources().getString(R.string.txt_hab_anscheinend) + " " + hole_letzte_Promille() + " " + context.getResources().getString(R.string.txt_alkohol_im_blut) + "..." + "\n" + context.getResources().getString(R.string.txt_schau_die_app_an) + "\n\n";


        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);


        try {
            startActivity(shareIntent);

        } catch (android.content.ActivityNotFoundException ex) {
            //print.print_short("Whatsapp ist nicht installiert...");
        }
    }

    private void starteMap(){
        Intent intent = new Intent(ChallengeActivity.this, KarteActivity.class);
        startActivity(intent);
    }

}
