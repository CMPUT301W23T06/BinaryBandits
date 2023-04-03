package com.example.binarybandits;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

/**
 * Class that contains functions used to get the Player's current locations
 */
public class Geolocation {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Context context;

    /**
     * Constructor for Geolocation class
     * @param context the context of program
     * @param listener listener that retrieves the Player's current location if location permissions are enabled
     */
    public Geolocation(Context context, LocationListener listener) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = listener;
    }

    /**
     * Get the Player's last known location
     */
    public void start() {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    /**
     * Stop getting location updates
     */
    public void stop() {
        locationManager.removeUpdates(locationListener);
    }

}
