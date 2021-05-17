package net.mbmedia.drunkmerlin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class Location {

    private Context context;
    private LatLng latLng = null;

    private LokalSpeichern lokalSpeichern;

    private Toaster toaster;

    public Location(Context context) {
        this.context = context;

        lokalSpeichern = new LokalSpeichern(context);
        toaster = new Toaster(context);
    }


    public LatLng setLocation() {

        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("test", "Standortfreigabe verboten");
        }else{
            Log.i("test", "freigegeben");
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        } else {
                            if (!lokalSpeichern.isOfflineModus()) {
                                toaster.sage("Bitte aktiviere deinen Standort");
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        return latLng;
    }

    public void onLocationChanged(android.location.Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //Toast.makeText(context, latLng.toString(), Toast.LENGTH_SHORT).show();
        if(latLng!=null){
            lokalSpeichern.setLetzterStandort(latLng.toString());
        }else{
            lokalSpeichern.setLetzterStandort("null");
        }

    }
}
