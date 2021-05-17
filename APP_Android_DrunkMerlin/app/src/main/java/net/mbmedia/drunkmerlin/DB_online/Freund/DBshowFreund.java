package net.mbmedia.drunkmerlin.DB_online.Freund;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.mbmedia.drunkmerlin.Challenges.ChallengeContextMenu;
import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.JsonParser;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.TO.Freund;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class DBshowFreund extends AsyncTask {

    public ArrayList<Freund> freunde;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private String hash;
    private String nutzerID;
    private Context context;
    HashMap<String, String> hashMap = new HashMap<>();
    private DBFunktionen dbFunktionen;
    private JsonParser parser = new JsonParser();

    public DBshowFreund(Context context, String hash, String nutzerID, LinearLayout linearLayout, ProgressBar progressBar){
        this.context = context;
        this.hash = hash;
        this.nutzerID = nutzerID;
        this.linearLayout = linearLayout;
        this.progressBar = progressBar;

        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzerID);

        dbFunktionen = new DBFunktionen();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar.setVisibility(View.VISIBLE);
    }



    @Override
    protected Object doInBackground(Object[] objects) {

        String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Freund/show", hashMap);

        try {
            freunde = parser.getFreunde(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(json);


        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(freunde != null){
            initStory(freunde);
        }

        progressBar.setVisibility(View.INVISIBLE);
    }

    private void delFreund(String freundID){
        DBdelFreund dBdelFreund = new DBdelFreund(context, hash, nutzerID, freundID, linearLayout, progressBar);
        dBdelFreund.execute();
    }


    public void initStory(final ArrayList<Freund> freunde){
        int l = freunde.size();

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

            TextView textView = new TextView(context);
            textView.setText(t+1 + ". " + freunde.get(k).getName() + " (" + freunde.get(k).getPromille() + "‰)");
            textView.setLayoutParams(TextViewParams);
            textView.setTextColor(Color.argb(255, 255, 255, 255));
            textView.setTextSize(16);


            LinearLayout trenner = new LinearLayout(context);
            trenner.setLayoutParams(trennerParams);
            trenner.setBackgroundColor(Color.argb(255, 255, 255, 255));


            ll.addView(imageView);
            ll.addView(textView);



            if(!freunde.get(t).getName().equals("Ich")){


                ll.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(context.getResources().getString(R.string.txt_soll) + " \"" + freunde.get(t).getName() + "\" " + context.getResources().getString(R.string.txt_von_freundesliste_entfernen))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // CONFIRM

                                        delFreund(freunde.get(t).getID());


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

                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu menu = new PopupMenu(context, v);
                        menu.getMenu().add(ChallengeContextMenu.Ichhabnochnie);
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getTitle().toString()){
                                    case ChallengeContextMenu.Ichhabnochnie:
                                        ChallengeContextMenu.startFrageActivity(context, freunde.get(t));
                                        return true;
                                    default:
                                        return true;
                                }
                            }
                        });

                        menu.show();
                    }
                });


            }

            linearLayout.addView(ll);
            linearLayout.addView(trenner);

        }


    }



    public void initStory2(final ArrayList<Freund> freunde){
        int l = freunde.size();


        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.removeAllViews();


        for(int k=0; k<l;k++){
            final int t = k;

            RelativeLayout ll = new RelativeLayout(context);
            ll.setLayoutParams(LLParams);

            TextView tv_was = new TextView(context);
            tv_was.setText(t+1 + ". " + freunde.get(k).getName() + " (" + freunde.get(k).getPromille() + "‰)");
            tv_was.setTextColor(context.getResources().getColor(R.color.white));
            tv_was.setTextSize(20);


            ll.addView(tv_was);

            if(!freunde.get(t).getName().equals("Ich")){
                ll.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(context.getResources().getString(R.string.txt_soll) + " \"" + freunde.get(t).getName() + "\" " + context.getResources().getString(R.string.txt_von_freundesliste_entfernen))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // CONFIRM

                                        delFreund(freunde.get(t).getID());


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
            }


            linearLayout.addView(ll);


        }



    }
}
