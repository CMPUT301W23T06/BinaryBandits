package com.example.binarybandits;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Camera;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.controllers.PermissionsController;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.qrcode.QRCodeDB;
import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.example.binarybandits.qrcode.QRCodeListCallback;
import com.example.binarybandits.ui.QRedit.QRCodeEditActivity;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

/**
 * View class that uses the Google Maps API to display geolocation of all QR codes. This
 * class also has a search button that takes users to MapSearchFragment.
 * Outstanding Issues: N/A
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private ArrayList<Double> currentLocation;

    /**
     * Accesses bundles from instance and handles events accordingly. Initializes the map fragment
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout file as the content view.
        setContentView(R.layout.fragment_maps);

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Get a handle to the GoogleMap object and display markers for every QR code with a geolocation
     * @param googleMap Google Maps SDK object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        getCurrentLocation(googleMap);
        /*googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));*/

        QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());

        ArrayList<Double> coordinatesTest = new ArrayList<>();
        coordinatesTest.add(53.5269807);
        coordinatesTest.add(-113.5233741);
        Log.d("Maps", String.valueOf(coordinatesTest.get(0)));

        //Referenced: https://stackoverflow.com/questions/49839437/how-to-show-markers-only-inside-of-radius-circle-on-maps
        qrCodeDB.getQRCodesByQuery(qrCodeDB.getQRCodesWithCoordinates(), new QRCodeListCallback() {
            @Override
            public void onQRCodeListCallback(ArrayList<QRCode> qrCodeList) {
                for(int i = 0; i < qrCodeList.size(); i++) {
                    QRCode qrCode = qrCodeList.get(i);
                    if(qrCode != null) {
                        String name = qrCode.getName();
                        double latitude = qrCode.getCoordinates().get(0);
                        double longitude = qrCode.getCoordinates().get(1);
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(name));
                        assert marker != null;
                        marker.setTag(qrCode);
                    }
                }
            }
        });

        //When the user clicks on a marker, show the visual representation, and points of QR code
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Log.d("Maps", "Marker Clicked!");
                LatLng markerLatLng = marker.getPosition();
                QRCode qrCode = (QRCode)marker.getTag();
                Log.d("Maps", String.valueOf(qrCode.getPoints()));

                //TODO: When a marker is clicked, players are taken to QRCodeInfoActivity. Need to change to a popup as shown on the storyboard
                Intent myIntent = new Intent(MapActivity.this, QRCodeInfoActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", String.valueOf(qrCode.getName()));
                extras.putString("username", AuthController.getUsername(MapActivity.this));
                extras.putBoolean("current_user", true);
                myIntent.putExtras(extras);
                // go to QRCodeInfoActivity to display the QR code
                MapActivity.this.startActivity(myIntent);
                return false;
            }
        });
    }

    /**
     * Get the user's current location and show current location on the MapActivity
     * @param googleMap Google Maps SDK object
     */
    public void getCurrentLocation(GoogleMap googleMap) {
        //Referenced: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
        if (PermissionsController.locationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                PermissionsController.askLocationPermission(this);
            }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            googleMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                final float defaultZoom = 15.0f;
                                LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, defaultZoom));
                                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
        }
    }



}
