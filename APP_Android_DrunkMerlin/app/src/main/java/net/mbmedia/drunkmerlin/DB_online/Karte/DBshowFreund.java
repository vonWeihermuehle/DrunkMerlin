package net.mbmedia.drunkmerlin.DB_online.Karte;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.mbmedia.drunkmerlin.DB_online.DBFunktionen;
import net.mbmedia.drunkmerlin.JsonParser;
import net.mbmedia.drunkmerlin.Konfiguration;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.TO.LocationTO;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class DBshowFreund extends AsyncTask {

    private final String LOGTAG = "dbLocation";

    //https://localhost:8000/location?hash=3afddfbb893ecd2fa6a394645b4d82a4&nutzerid=E0DBD6BF-2B4E-4592-A5B0-D43037088B5D
    //https://mb-media.net:8000/location?hash=3afddfbb893ecd2fa6a394645b4d82a4&nutzerid=E0DBD6BF-2B4E-4592-A5B0-D43037088B5D

    private DBFunktionen dbFunktionen = new DBFunktionen();
    private Context context;
    HashMap<String, String> hashMap = new HashMap<>();
    private String nutzer_id;
    private GoogleMap googleMap;
    private String hash;

    private ArrayList<LocationTO> locations = null;

    public DBshowFreund(Context context, String nutzer_id, String hash, GoogleMap googleMap){
        this.context = context;
        this.googleMap = googleMap;
        this.nutzer_id = nutzer_id;
        this.hash = hash;

        hashMap.put("hash", hash);
        hashMap.put("nutzerid", nutzer_id);

    }


    @Override
    protected Object doInBackground(Object[] objects) {
        String json = dbFunktionen.getJSON(context, Konfiguration.URL + "/Freund/karte", hashMap);

        Log.i(LOGTAG, json);

        JsonParser parser = new JsonParser();
        try {
            locations = parser.getLocations(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        String letzterName = "";

        if(locations != null){
            for(int i=0; i<locations.size(); i++){

                if(!locations.get(i).getUsername().equals(letzterName)){
                    letzterName = locations.get(i).getUsername();


                    Log.i(LOGTAG, locations.get(i).getUsername());
                    String s = locations.get(i).getLatlng();
                    if(s.contains("(") && s.contains(")")){
                        googleMap.addMarker(buildMarker(convertStringtoLatLng(s), locations.get(i).getUsername(), true));
                    }


                }

            }
        }

    }




    public LatLng convertStringtoLatLng(String s){
        //input: lat/lng: (48.9397329,11.777866)

        if(s.length()<7){
            return null;
        }

        String neu = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
        //output: 48.9397329,11.777866

        String[] result = neu.split(",");

        return new LatLng(Double.parseDouble(result[0]), Double.parseDouble(result[1]));
    }

    public MarkerOptions buildMarker(LatLng latLng, String titel, Boolean withCustomIcon){
        MarkerOptions marker = new MarkerOptions().position(latLng).title(titel);
        if(withCustomIcon){
            marker.icon(BitmapDescriptorFactory.fromBitmap(getIcon()));
        }
        return marker;
    }

    public Bitmap getIcon(){
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(R.drawable.cheers);
        Bitmap b = bitmapdraw.getBitmap();
        return Bitmap.createScaledBitmap(b, width, height, false);
    }
}
