package net.mbmedia.drunkmerlin;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.Base64Utils;

import net.mbmedia.drunkmerlin.TO.Drink;
import net.mbmedia.drunkmerlin.TO.Freund;
import net.mbmedia.drunkmerlin.TO.LocationTO;
import net.mbmedia.drunkmerlin.TO.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class JsonParser {

    public String getID(String json) {

        System.out.println("json: " + json);

        if((json == null) || json.equals("null")){
            return Konfiguration.FEHLER;
        }

        try {
            JSONArray temp_array = new JSONArray(json);
            JSONObject temp_object = temp_array.getJSONObject(0);

            String id = temp_object.getString("id");
            return id;
        } catch (JSONException e) {
            e.printStackTrace();
            return Konfiguration.FEHLER;
        }

    }


    public ArrayList<Drink> getDrinks(String json_input_string) throws JSONException {
        JSONArray temp_array = new JSONArray(json_input_string);

        ArrayList<Drink> drinks = new ArrayList<Drink>();

        if(temp_array.length() > 0) {

            for (int i = 0; i < temp_array.length(); i++) {
                JSONObject temp_object = temp_array.getJSONObject(i);
                Drink temp_drink = new Drink();
                temp_drink.setId(temp_object.getString("id"));
                temp_drink.setProzent(temp_object.getString("prozent"));
                temp_drink.setName(temp_object.getString("name"));
                temp_drink.setMenge(temp_object.getString("menge"));
                temp_drink.setZeit(temp_object.getString("zeitpunkt"));

                drinks.add(temp_drink);
            }
        }

        return drinks;
    }

    public ArrayList<Freund> getFreunde(String json_input_string) throws JSONException {
        JSONArray temp_array = new JSONArray(json_input_string);

        ArrayList<Freund> freunde = new ArrayList<Freund>();

        if(temp_array.length() > 0) {

            for (int i = 0; i < temp_array.length(); i++) {
                JSONObject temp_object = temp_array.getJSONObject(i);
                Freund tmp = new Freund();
                tmp.setId(temp_object.getString("freund_id"));
                tmp.setName(temp_object.getString("username"));
                tmp.setPromille(temp_object.getString("promille"));

                freunde.add(tmp);
            }
        }

        return freunde;
    }

    public ArrayList<LocationTO> getLocations(String json_input_string) throws JSONException {
        JSONArray temp_array = new JSONArray(json_input_string);

        Log.i("test", json_input_string);

        ArrayList<LocationTO> locations = new ArrayList<LocationTO>();

        if(temp_array.length() > 0) {

            for (int i = 0; i < temp_array.length(); i++) {
                JSONObject temp_object = temp_array.getJSONObject(i);
                LocationTO tmp = new LocationTO();
                tmp.setUsername(temp_object.getString("username"));
                tmp.setLatlng(decodeBase64(temp_object.getString("latlng")));
                tmp.setZeitpunkt(temp_object.getString("zeitpunkt"));

                if(!tmp.getLatlng().equals("null")){
                    locations.add(tmp);
                }
            }
        }

        return locations;
    }

    private String decodeBase64(String encoded){

        String decoded = new String(Base64Utils.decode(encoded));
        return decoded;
    }

    public String getStatus(String json) {

        if((json == null) || json.equals("null")){
            return Konfiguration.FEHLER;
        }

        try {
            JSONArray temp_array = new JSONArray(json);
            JSONObject temp_object = temp_array.getJSONObject(0);

            String id = temp_object.getString("status");
            return id;
        } catch (JSONException e) {
            e.printStackTrace();
            return Konfiguration.FEHLER;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Notification> getNotifications(String json_input_string) throws JSONException {
        JSONArray temp_array = new JSONArray(json_input_string);

        ArrayList<Notification> notifications = new ArrayList<Notification>();

        if(temp_array.length() > 0) {

            for (int i = 0; i < temp_array.length(); i++) {
                JSONObject temp_object = temp_array.getJSONObject(i);
                Notification tmp = new Notification();
                tmp.setId(temp_object.getInt("id"));
                //tmp.setArt(temp_object.getInt("art"));
                tmp.setBase64text(temp_object.getString("text"));
                tmp.setFreundName(temp_object.getString("author"));

                notifications.add(tmp);
            }
        }

        return notifications;
    }
}
