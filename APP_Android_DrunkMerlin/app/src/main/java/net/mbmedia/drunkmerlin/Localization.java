package net.mbmedia.drunkmerlin;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class Localization {

    private Context context;

    public Localization(Context context){
        this.context = context;
    }

    public void setAppLocale(String localeCode){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    public String getAppLocale(){
        String l = "";
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                l = context.getResources().getConfiguration().getLocales().toString();
            }
        } else {
            l = context.getResources().getConfiguration().locale.toString();
        }

        return l;
    }
}
