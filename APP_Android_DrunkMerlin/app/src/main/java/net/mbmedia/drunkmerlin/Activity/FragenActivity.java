package net.mbmedia.drunkmerlin.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;

import net.mbmedia.drunkmerlin.Challenges.ChallengeContextMenu;
import net.mbmedia.drunkmerlin.Challenges.OfflineDBHelper;
import net.mbmedia.drunkmerlin.Challenges.StandardFragenSet;
import net.mbmedia.drunkmerlin.DB_online.Challenge.DBaddChallenge;
import net.mbmedia.drunkmerlin.DB_online.Challenge.DBdelChallenge;
import net.mbmedia.drunkmerlin.DB_online.Notification.DBgetNotification;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.TO.Frage;
import net.mbmedia.drunkmerlin.TO.Freund;

import java.util.ArrayList;


public class FragenActivity extends ActivityFunktionen {

    private LokalSpeichern lokalSpeichern;
    private Context context = this;
    private net.mbmedia.drunkmerlin.Challenges.OfflineDBHelper dbHelper = new OfflineDBHelper(context);

    private String Freund_name;
    private String Freund_id;
    private TextView btn_back;
    private LinearLayout linearLayout;

    private final int AnzahlFragen = 10;

    private final String LOG_TAG = "FragenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragen);

        lokalSpeichern = new LokalSpeichern(this);
        initGUI();

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString(ChallengeContextMenu.NAME_FLAG)!= null)
        {
            Freund_name = bundle.getString(ChallengeContextMenu.NAME_FLAG);
            Freund_id = bundle.getString(ChallengeContextMenu.FREUND_ID_FLAG);
        }

        checkForFullDB();

        initStory(dbHelper.getZufallsFragen(AnzahlFragen));

    }

    private void checkForFullDB(){
        if(dbHelper.getFragenCount() < StandardFragenSet.minAnzahl){
            dbHelper.setStandardFragen();
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    private void initGUI(){
        linearLayout = findViewById(R.id.layout);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendeNachricht(String frage){
        Log.i(LOG_TAG, "frage: " + frage + " name: " + Freund_name + " id: " + Freund_id);
        DBaddChallenge db = new DBaddChallenge(context, getHash(), lokalSpeichern.getUserID(), Freund_id, frage);
        db.execute();
    }


    /*
    Log.i(LOG_TAG, "frage: " + frage + " name: " + Freund_name + " id: " + Freund_id);
        DBaddChallenge db = new DBaddChallenge(context, getHash(), lokalSpeichern.getUserID(), frage);
        db.execute();

        DBgetNotification dBgetNotification = new DBgetNotification(context, getHash(), lokalSpeichern.getUserID());
        dBgetNotification.execute();

        //DBdelChallenge db = new DBdelChallenge(context, getHash(), lokalSpeichern.getUserID(), 1);
        //db.execute();
     */

    public void initStory(final ArrayList<Frage> fragen){
        int l = fragen.size();

        linearLayout.removeAllViews();

        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams trennerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);

        LinearLayout.LayoutParams ImageViewParams = new LinearLayout.LayoutParams(20, 20);
        ImageViewParams.gravity= Gravity.CENTER;

        LinearLayout.LayoutParams TextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextViewParams.setMargins(15, 0, 0, 0);



        for(int k=0; k<l; k++){
            final int t = k;

            LinearLayout ll = new LinearLayout(context);
            ll.setLayoutParams(LLParams);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setPadding(16, 16, 16, 16);


            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(ImageViewParams);
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.forward_arrow));
            imageView.setColorFilter(Color.argb(255, 255, 255, 255));

            final String frage = fragen.get(t).get();

            TextView textView = new TextView(context);
            textView.setText("... " + frage);
            textView.setLayoutParams(TextViewParams);
            textView.setTextColor(Color.argb(255, 255, 255, 255));
            textView.setTextSize(16);


            LinearLayout trenner = new LinearLayout(context);
            trenner.setLayoutParams(trennerParams);
            trenner.setBackgroundColor(Color.argb(255, 255, 255, 255));


            ll.addView(imageView);
            ll.addView(textView);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getResources().getString(R.string.txt_nachricht_senden1) + " " + Freund_name + " " +  context.getResources().getString(R.string.txt_nachricht_senden2))
                            .setTitle(context.getResources().getString(R.string.title_send))
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                public void onClick(DialogInterface dialog, int id) {
                                    sendeNachricht(frage);
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


            linearLayout.addView(ll);
            linearLayout.addView(trenner);

        }


    }







}
