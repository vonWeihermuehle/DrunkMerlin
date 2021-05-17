package net.mbmedia.drunkmerlin.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.Localization;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.R;

import java.util.Locale;

import static java.lang.Thread.sleep;

public class Splash extends Activity {

    private LokalSpeichern lokalSpeichern;
    private Localization localization = new Localization(this);


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        lokalSpeichern = new LokalSpeichern(this);

        checkLanguage();


        if(checkPermission()){
            starteAppdelayed();
        }else{
            getPermission();
            startePermissionListenor();
        }



    }

    private void checkLanguage(){
        if(lokalSpeichern.isEnglischeSprache()){
            localization.setAppLocale("en");
        }
    }


    private void startePermissionListenor(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                int z = 0;

                while(!checkPermission()){
                    System.out.println("permissionthread");
                    z++;
                    if(z > 100){return;}

                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                starteApp();

            }
        });
        thread.start();

    }


    private void starteApp(){
        Intent mainIntent = new Intent(Splash.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public Boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            return false;
        }else{
            return true;
        }
    }

    private void starteAppdelayed(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(Splash.this, LoginActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, Konfiguration.SPLASH_DAUER);
    }

    private void getPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},123);
    }


}
