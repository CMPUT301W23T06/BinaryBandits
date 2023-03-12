package com.example.binarybandits.ui.QRedit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.MainActivity;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.controllers.PermissionsController;
import com.example.binarybandits.controllers.QRController;
import com.example.binarybandits.controllers.ScannerController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.qrcode.DownloadImageTask;
import com.example.binarybandits.qrcode.QRCodeDB;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class QRCodeEditActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView pointsTextView;
    private TextView imageConfirmationTextView;
    private CheckBox locationCheckBox;
    private Button saveBtn;
    private Button addImageBtn;
    private ArrayList<Double> coordinates;

    private FusedLocationProviderClient fusedLocationClient;

    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_edit);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();
        String hash;

        if (extras != null) {
            hash = extras.getString("hash");
        } else {
            hash = AuthController.getUsername(QRCodeEditActivity.this);
        }

        QRController qrController = new QRController();

        nameTextView = findViewById(R.id.qrName);
        pointsTextView = findViewById(R.id.pointsText);
        imageConfirmationTextView = findViewById(R.id.imageConfirmationText);
        locationCheckBox = findViewById(R.id.locationCheckBox);
        saveBtn = findViewById(R.id.saveButton);
        addImageBtn = findViewById(R.id.imageButton);
        ImageView imageView = findViewById(R.id.imageView);

        getCurrentLocation();

        String name = qrController.generateUniqueName(hash);
        int points = qrController.calculatePoints(hash);
        nameTextView.setText(name);
        String pointsString = points + " point(s) collected!";
        pointsTextView.setText(pointsString);
        DownloadImageTask.loadQRImageIntoView(imageView, hash);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                // TODO: add QR Code to database here
                String uid = AuthController.getUsername(QRCodeEditActivity.this);

                QRCode qrCode = new QRCode(hash, name, points, uid, coordinates, null, new ArrayList<>(), 0);
                ScannerController scannerController = new ScannerController();

                PlayerDB db = new PlayerDB(new DBConnector());
                db.getPlayer(uid, new PlayerCallback() {
                    @Override
                    public void onPlayerCallback(Player player) {
                        scannerController.addQRCode(qrCode, player);
                    }
                });

                Intent myIntent = new Intent(QRCodeEditActivity.this, MainActivity.class);
                QRCodeEditActivity.this.startActivity(myIntent);
            }
        });

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                changeTextOnImageClick();
            }
        });
    }

    public Location getCurrentLocation() {
        if (PermissionsController.locationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                PermissionsController.askLocationPermission(QRCodeEditActivity.this);
                return null;
            }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            coordinates = new ArrayList<>();
                            coordinates.add(latitude);
                            coordinates.add(longitude);
                        }
                    }
                });
        }
        return null;
    }

    public void changeTextOnImageClick() {
        imageConfirmationTextView.setText("Image successfully added!");
        addImageBtn.setText("Replace");
    }

}