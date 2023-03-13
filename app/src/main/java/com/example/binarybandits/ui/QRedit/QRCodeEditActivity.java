package com.example.binarybandits.ui.QRedit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.Nullable;
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
    private TextView viewImageTextView;
    private CheckBox locationCheckBox;
    private Button saveBtn;
    private Button addImageBtn;
    private ArrayList<Double> coordinates;
    private Bitmap photo;

    private FusedLocationProviderClient fusedLocationClient;

    private View popupView;
    private PopupWindow popupWindow;

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
        viewImageTextView = findViewById(R.id.viewImageText);
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

                QRCode qrCode = new QRCode(hash, name, points, uid, coordinates, "", new ArrayList<>(), 0);
                ScannerController scannerController = new ScannerController();

                PlayerDB db = new PlayerDB(new DBConnector());
                if (photo != null) {
                    QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());
                    qrCodeDB.addLocationImageToServer(photo, name);
                }
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
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    /**
     * Function to get last known location of users
     * @return A Location object containing the coordinates of the last known user location
     */
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

    /**
     * Function to change text for "Click image" button, and display confirmation text
     */
    public void changeTextOnImageClick() {
        imageConfirmationTextView.setText("Image successfully added!");
        addImageBtn.setText("Replace");
        viewImageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(resultCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            changeTextOnImageClick();
        }
    }

     /**
     * Function to show a pop-up window containing the image clicked by user
     * @param view A reference of the view that was just clicked
     */
    public void showImagePopup(View view) {
        new LocationImageFragment(photo).show(getSupportFragmentManager(), "show");
    }
}
