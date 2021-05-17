package net.mbmedia.drunkmerlin.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ActivityFunktionen extends AppCompatActivity {

    public ActivityFunktionen(){


    }


    public static String getHash(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String datum =  formatter.format(date);

        String zu_hashen = datum + "DrunkMerlinGeheim";
        //System.out.println(zu_hashen);
        return MD5(zu_hashen);
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String getDatumZeit(){
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm dd.MM.yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}

