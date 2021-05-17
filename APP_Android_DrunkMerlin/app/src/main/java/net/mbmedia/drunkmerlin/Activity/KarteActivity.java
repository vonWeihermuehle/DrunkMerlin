package net.mbmedia.drunkmerlin.Activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.mbmedia.drunkmerlin.DB_online.Karte.DBshowFreund;
import net.mbmedia.drunkmerlin.Location;
import net.mbmedia.drunkmerlin.LokalSpeichern;
import net.mbmedia.drunkmerlin.R;
import net.mbmedia.drunkmerlin.Toaster;

import static java.lang.Thread.sleep;

public class KarteActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String LOG_TAG = "karte";

    private GoogleMap mMap;

    private LokalSpeichern lokalspeichern = new LokalSpeichern(this);

    private Toaster toaster = new Toaster(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karte);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ZeigeEigenenStandort();
        zeigeFreundeStandort();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void zeigeFreundeStandort(){
        DBshowFreund db = new DBshowFreund(this, lokalspeichern.getUserID(), ActivityFunktionen.getHash(), mMap);
        db.execute();
    }


    private void ZeigeEigenenStandort(){
        String s = lokalspeichern.getLetzterStandort();

        System.out.println("ZeigeEigenenStandort: " + s);

        if(s.contains("(") && s.contains(")")){
            if(s.equals("null")){
                return;
            }

            LatLng home = convertStringtoLatLng(s);

            mMap.addMarker(buildMarker(home, "Ich", false));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
            mMap.animateCamera( CameraUpdateFactory.zoomTo( 10 ) );
        }else{
            toaster.sage("Dein Standort ist nicht aktiviert!");
        }

    }


    public LatLng convertStringtoLatLng(String s){
        //input: lat/lng: (48.9397329,11.777866)
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
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.cheers);
        Bitmap b = bitmapdraw.getBitmap();
        return Bitmap.createScaledBitmap(b, width, height, false);
    }



}
