package net.mbmedia.drunkmerlin;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class LokalSpeichern {

    private static final String USERID_FLAG = "USERID";
    private static final String GESCHLECHT_FLAG = "GESCHLECHT";
    private static final String GEWICHT_FLAG ="GEWICHT";
    private static final String MODUS_FLAG = "offlineModus";
    private static final String AddDRINK_FLAG = "addDrink";
    private static final String LASTPROMILLE_FLAG = "letztePromille";
    private static final String LASTZEIT_FLAG = "letzteZeit";
    private static final String DRINKLISTELEER = "drinklisteLeer";
    private static final String STANDORT_FLAG = "standort";
    private static final String LETZTE_BAR_EINGABE = "letztebareingabe";
    private static final String TEILEN_AUFFORDERUNG = "teilenaufforderung";
    private static final String NIEMALS_TEILEN = "appniemalsteilen";
    private static final String ENGLISCH = "englisch";

    private static final String PREF = "DrunkMerlinPreferences";

    private static Context context;


    public LokalSpeichern(Context context){
        LokalSpeichern.context = context;
    }

    public static void setUserID(String id){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(USERID_FLAG, id);
        editor.commit();

    }

    public String getUserID(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        return pref.getString(USERID_FLAG,"null");

    }

    public static void deleteAll(){
        context.getSharedPreferences(PREF, 0).edit().clear().commit();
    }

    public String getGeschlecht(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        return pref.getString(GESCHLECHT_FLAG,"null");
    }

    public void setGeschlecht(String s){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(GESCHLECHT_FLAG, s);
        editor.commit();
    }

    public String getGewicht(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        return pref.getString(GEWICHT_FLAG,"null");
    }

    public void setGewicht(String s){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(GEWICHT_FLAG, s);
        editor.commit();
    }


    public Boolean isOfflineModus(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        return pref.getBoolean(MODUS_FLAG,false);
    }

    public void setOfflineModus(Boolean b){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(MODUS_FLAG, b);
        editor.commit();
    }


    public Boolean isHinzugefuegt(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        return pref.getBoolean(AddDRINK_FLAG,false);
    }

    public void setHinzugefuegt(Boolean b){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(AddDRINK_FLAG, b);
        editor.commit();
    }

    public Set<String> letzerPromilleZeitWert(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Set<String> default_wert = new HashSet<String>();
        default_wert.add("null");
        default_wert.add("null");

        return pref.getStringSet(LASTPROMILLE_FLAG, default_wert);
    }

    public void setLetzterPromilleWert(Set<String> promille_und_zeit){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putStringSet(LASTPROMILLE_FLAG, promille_und_zeit);
        editor.commit();
    }


    public Boolean sollDrinkListegeloeschtwerden(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        return pref.getBoolean(DRINKLISTELEER,false);
    }

    public void DrinkListeSollgeloeschtwerden(Boolean b){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(DRINKLISTELEER, b);
        editor.commit();
    }

    public String getLetzterStandort(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        return pref.getString(STANDORT_FLAG,"null");
    }

    public void setLetzterStandort(String s){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(STANDORT_FLAG, s);
        editor.commit();
    }

    public String getLetzteBarEingabe(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        return pref.getString(LETZTE_BAR_EINGABE,"null");
    }

    public void setLetzteBarEingabe(String s){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(LETZTE_BAR_EINGABE, s);
        editor.commit();
    }

    public Integer getTeilenAufforderung(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        return pref.getInt(TEILEN_AUFFORDERUNG,0);
    }

    public void setTeilenAufforderung(Integer zaehler){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(TEILEN_AUFFORDERUNG, zaehler);
        editor.commit();
    }

    public Boolean isAppNiemalsTeilen(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        return pref.getBoolean(NIEMALS_TEILEN,false);
    }

    public void setAppNiemalsTeilen(Boolean bool){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(NIEMALS_TEILEN, bool);
        editor.commit();
    }

    public Boolean isEnglischeSprache(){
        SharedPreferences pref = context.getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        return pref.getBoolean(ENGLISCH,false);
    }

    public void setEnglischeSprache(Boolean bool){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(ENGLISCH, bool);
        editor.commit();
    }




}
