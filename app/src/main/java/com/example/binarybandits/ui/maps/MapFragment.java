package com.example.binarybandits.ui.maps;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.controllers.PermissionsController;
import com.example.binarybandits.controllers.QRController;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.qrcode.QRCodeCallback;
import com.example.binarybandits.qrcode.QRCodeDB;
import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.example.binarybandits.qrcode.QRCodeListCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.Slider;

import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * View class that uses the Google Maps API to display geolocation of all QR codes. This
 * class also has a search button that takes users to MapSearchFragment.
 * Outstanding Issues:
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private SearchView searchView;
    private final float defaultZoom = 15.0f;
    private View mapView;
    private Slider radiusSlider;
    private ArrayList<Double> coordinatesOfAddress = new ArrayList<>();
    private ArrayList<QRCode> qrCodes = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mapView = inflater.inflate(R.layout.fragment_maps, container, false);


        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return mapView;
    }

    QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());
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
                            getActivity(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        getQRCodes(qrCodeDB, googleMap);

        //When the user clicks on a marker, show the visual representation, and points of QR code
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Log.d("Maps", "Marker Clicked!");
                LatLng markerLatLng = marker.getPosition();
                QRCode qrCode = (QRCode)marker.getTag();
                Log.d("Maps", String.valueOf(qrCode.getPoints()));

                Intent myIntent = new Intent(getActivity(), QRCodeInfoActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", String.valueOf(qrCode.getName()));
                extras.putString("username", AuthController.getUsername(getActivity()));
                extras.putBoolean("current_user", false);
                myIntent.putExtras(extras);
                // go to QRCodeInfoActivity to display the QR code
                startActivity(myIntent);
                return false;
            }
        });

        //Referenced: https://www.geeksforgeeks.org/how-to-add-searchview-in-google-maps-in-android/
        //Author: https://auth.geeksforgeeks.org/user/chaitanyamunje/articles
        //License: CCBY-SA https://www.geeksforgeeks.org/copyright-information/
        searchView = mapView.findViewById(R.id.map_search_view);
        radiusSlider = mapView.findViewById(R.id.map_slider);

        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String input = searchView.getQuery().toString();

                List<Address> addressList = null;
                if(input != null || input.equals("")) {
                    Geocoder geocoder = new Geocoder(getActivity());

                    try {
                        addressList = geocoder.getFromLocationName(input, 1);
                    } catch (IOException e) {
                        Log.d("MapActivity", e.toString());
                    }

                    if (!addressList.isEmpty()) {
                        //Get the first result from the list of results (addresses)
                        Address address = addressList.get(0);

                        LatLng latLngOfAddress = new LatLng(address.getLatitude(), address.getLongitude());
                        coordinatesOfAddress.set(0, address.getLatitude());
                        coordinatesOfAddress.set(1, address.getLongitude());

                        float radius = radiusSlider.getValue();
                        getNearbyQRCodes(qrCodes, googleMap, coordinatesOfAddress, radius);
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

        radiusSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                Log.d("Radius", String.valueOf(value));
                getNearbyQRCodes(qrCodes, googleMap, coordinatesOfAddress, value);
            }
        });
    }

    /**
     * Get the user's current location and show current location on the MapFragment
     * @param googleMap Google Maps SDK object
     */
    public void getCurrentLocation(GoogleMap googleMap) {
        //Referenced: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
        if (PermissionsController.locationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                PermissionsController.askLocationPermission(getActivity());
            }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            googleMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, defaultZoom));
                                googleMap.getUiSettings().setMyLocationButtonEnabled(false);

                                //Initially set coordinates to current location
                                coordinatesOfAddress.add(location.getLatitude());
                                coordinatesOfAddress.add(location.getLongitude());
                                getNearbyQRCodes(qrCodes, googleMap, coordinatesOfAddress, 1);
                            }
                        }
                    });
        } else {
            //Set current location to an arbitrary location
            coordinatesOfAddress.add(0.0);
            coordinatesOfAddress.add(0.0);
        }
    }

    public void getQRCodes(QRCodeDB qrCodeDB, GoogleMap googleMap) {
        qrCodeDB.getQRCodesByQuery(qrCodeDB.getQRCodesWithCoordinates(), new QRCodeListCallback() {
            @Override
            public void onQRCodeListCallback(ArrayList<QRCode> qrCodeList) {
                qrCodes.addAll(qrCodeList);
                getCurrentLocation(googleMap);

                // if sent from QRpage, the QRcode name is passed through. Get QRcode coordinates and send
                // to getQRCodeFromLocation
                Bundle args = getArguments();
                String qrCode = args.getString("QRCode");
                if (!qrCode.equals("none")) {
                    Log.d("name", qrCode);

                    QRCodeDB db_qr = new QRCodeDB(new DBConnector());
                    db_qr.getQRCode(qrCode, new QRCodeCallback() {
                        @Override
                        public void onQRCodeCallback(QRCode qrCode) {
                            getQRCodeFromLocation(qrCode, googleMap);
                        }
                    });
                }
            }
        });
    }

    /**
     * Get locations of all QR codes near a given location. Place markers at locations of all QR codes.
     * @param qrCodeList list of all QR codes with locations in the database
     * @param googleMap Google Maps SDK object
     * @param location latitude and longitude of location to find codes near
     * @param radius radius of search near location
     */
    public void getNearbyQRCodes(ArrayList<QRCode> qrCodeList, GoogleMap googleMap, ArrayList<Double> location, float radius) {
        final float toDegreesScalar = 0.009009009f;
        float distance = toDegreesScalar*radius;
        ArrayList<QRCode> resultsList = new ArrayList<>();
        for(int i = 0; i < qrCodeList.size(); i++) {
            QRCode qrCode = qrCodeList.get(i);
            if (qrCode.getCoordinates().get(0)>=location.get(0) - distance && qrCode.getCoordinates().get(1) <= location.get(0) + distance
                    && qrCode.getCoordinates().get(1) >= location.get(1) - distance && qrCode.getCoordinates().get(1) <= location.get(1) + distance) {
                resultsList.add(qrCode);
            }
        }
        googleMap.clear();
        placeMarkers(resultsList, googleMap);
    }

    public void getQRCodeFromLocation(QRCode qrCode, GoogleMap googleMap) {
        LatLng coordinates = new LatLng(qrCode.getCoordinates().get(0), qrCode.getCoordinates().get(1));
        String name = qrCode.getName();
        double latitude = qrCode.getCoordinates().get(0);
        double longitude = qrCode.getCoordinates().get(1);
        Marker marker = googleMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(latitude, longitude)).title(name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker)));
        assert marker != null;
        marker.setTag(qrCode);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, defaultZoom));
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
                Marker marker = googleMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(latitude, longitude)).title(name)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker)));

                assert marker != null;
                marker.setTag(qrCode);
                //new MarkerOptions()
                        //.icon(BitmapDescriptorFactory.defaultMarker());
                        //.icon(BitmapDescriptorFactory.fromFile());
            }
        }
    }
}
