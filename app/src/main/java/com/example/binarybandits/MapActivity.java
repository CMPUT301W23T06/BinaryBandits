package com.example.binarybandits;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Camera;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * View class that uses the Google Maps API to display geolocation of all QR codes. This
 * class also has a search button that takes users to MapSearchFragment.
 * Outstanding Issues: N/A
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private SearchView searchView;
    private final float defaultZoom = 15.0f;

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

        QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());

        ArrayList<Double> coordinatesTest = new ArrayList<>();
        coordinatesTest.add(53.5269807);
        coordinatesTest.add(-113.5233741);
        Log.d("Maps", String.valueOf(coordinatesTest.get(0)));

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

        getQRCodeLocations(qrCodeDB, googleMap);

        //Referenced: https://www.geeksforgeeks.org/how-to-add-searchview-in-google-maps-in-android/
        //Author: https://auth.geeksforgeeks.org/user/chaitanyamunje/articles
        //License: CCBY-SA https://www.geeksforgeeks.org/copyright-information/
        searchView = findViewById(R.id.map_search_view);

        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String input = searchView.getQuery().toString();

                List<Address> addressList = null;
                if(input != null || input.equals("")) {
                    Geocoder geocoder = new Geocoder(MapActivity.this);

                    try {
                        addressList = geocoder.getFromLocationName(input, 1);
                    } catch (IOException e) {
                        Log.d("MapActivity", e.toString());
                    }

                    if (!addressList.isEmpty()) {
                        //Get the first result from the list of results (addresses)
                        Address address = addressList.get(0);

                        LatLng latLngOfAddress = new LatLng(address.getLatitude(), address.getLongitude());
                        ArrayList<Double> coordinatesOfAddress = new ArrayList<>();
                        coordinatesOfAddress.add(address.getLatitude());
                        coordinatesOfAddress.add(address.getLongitude());

                        int km = 5; //For testing purposes
                        getNearbyQRCodes(qrCodeDB, googleMap, coordinatesOfAddress, km);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngOfAddress, defaultZoom));
                    }
                }
                return false;
            }

                @Override
                public boolean onQueryTextChange(String newText) {
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
                                LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, defaultZoom));
                                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
        }
    }


    /**
     * Get locations of all QR codes that have a geolocation. Place markers at locations of all QR codes.
     * @param qrCodeDB database of QRCodes
     * @param googleMap Google Maps SDK object
     */
    public void getQRCodeLocations(QRCodeDB qrCodeDB, GoogleMap googleMap) {
        //TODO: Currently all QR codes with a location are retrieved (we should only retrieve nearby QR codes)
        //Referenced: https://stackoverflow.com/questions/49839437/how-to-show-markers-only-inside-of-radius-circle-on-maps
        qrCodeDB.getQRCodesByQuery(qrCodeDB.getQRCodesWithCoordinates(), new QRCodeListCallback() {
            @Override
            public void onQRCodeListCallback(ArrayList<QRCode> qrCodeList) {
                googleMap.clear();
                placeMarkers(qrCodeList, googleMap);
            }
        });
    }

    /**
     * Get locations of all QR codes near a given location. Place markers at locations of all QR codes.
     * @param qrCodeDB database of QRCodes
     * @param googleMap Google Maps SDK object
     * @param location latitude and longitude of location to find codes near
     * @param km radius of search near location
     */
    public void getNearbyQRCodes(QRCodeDB qrCodeDB, GoogleMap googleMap, ArrayList<Double> location, int km) {
        final float toDegreesScalar = 0.009f;
        float distance = toDegreesScalar*km;
        qrCodeDB.getNearbyQRCodes(distance, location, new QRCodeListCallback() {
            @Override
            public void onQRCodeListCallback(ArrayList<QRCode> qrCodeList) {
                googleMap.clear();
                googleMap.addCircle(new CircleOptions()
                        .center(new LatLng(location.get(0), location.get(1)))
                        .radius(km*1000)
                        .strokeColor(Color.RED)
                        .strokeWidth(2.0f)
                        .fillColor(0x220000FF));
                placeMarkers(qrCodeList, googleMap);
            }
        });
    }

    /**
     * Place markers on the map at locations of all QR codes in a list
     * @param qrCodeList list of QRCodes
     * @param googleMap Google Maps SDK object
     */
    public void placeMarkers(ArrayList<QRCode> qrCodeList, GoogleMap googleMap) {
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



}