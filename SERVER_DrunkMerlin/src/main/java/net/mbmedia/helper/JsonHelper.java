package net.mbmedia.helper;

import com.google.gson.Gson;
import net.mbmedia.db.entities.ChallengeMitUsername;
import net.mbmedia.db.entities.Drink;
import net.mbmedia.db.entities.FreundeMitKarte;
import net.mbmedia.db.entities.FreundeMitPromille;

import java.util.ArrayList;

public class JsonHelper {

    public static String generateJsonFromFreunde(String userID){
        return "[{\"id\":\"" + userID + "\"}]";
    }

    public static String generateJsonFromFreunde(ArrayList<FreundeMitPromille> freunde){
        return new Gson().toJson(freunde);
    }

    public static String generateJsonFromChallenges(ArrayList<ChallengeMitUsername> c){
        return new Gson().toJson(c);
    }

    public static String generateJsonFromDrinks(ArrayList<Drink> d){
        return new Gson().toJson(d);
    }

    public static String generateJsonFromFreundeKarte(ArrayList<FreundeMitKarte> k){
        return new Gson().toJson(k);
    }
}
