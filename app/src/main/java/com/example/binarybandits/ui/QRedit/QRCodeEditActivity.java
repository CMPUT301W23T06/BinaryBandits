package com.example.binarybandits.ui.QRedit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;

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
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

/**
 * An activity that allows users to add a location image and accept/decline geolocation permissions
 */
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

    private KonfettiView konfettiView = null;
    private Shape.DrawableShape drawableShape = null;

    private static final int CAMERA_REQUEST = 1888;

    /*final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);
    drawableShape = new Shape.DrawableShape(drawable, true);

    konfettiView = findViewById(R.id.konfettiView);
    EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
    Party party = new PartyFactory(emitterConfig)
            .angle(270)
            .spread(90)
            .setSpeedBetween(1f, 5f)
            .timeToLive(2000L)
            .shapes(new Shape.Rectangle(0.2f), drawableShape)
            .sizes(new Size(12, 5f, 0.2f))
            .position(0.0, 0.0, 1.0, 0.0)
            .build();*/



    /**
     * Creates the QRCodeEdit view
     * @param savedInstanceState the saved instance state that is restored if the app crashes
     */
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
        String username = AuthController.getUsername(QRCodeEditActivity.this);
        String pointsString = points + " point(s) collected!";
        pointsTextView.setText(pointsString);
        DownloadImageTask.loadQRImageIntoView(imageView, hash);

        // Confetti animation for when the user find a QR code
        // Sourced from https://github.com/DanielMartinus/Konfetti
        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart); //adds a heart shape to the confetti
        drawableShape = new Shape.DrawableShape(drawable, true);

        // Styles the confetti
        konfettiView = findViewById(R.id.konfettiView);
        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        Party party = new PartyFactory(emitterConfig) // creates a party with the given emitter config
                .angle(270)
                .spread(90)
                .setSpeedBetween(1f, 5f)
                .timeToLive(2000L)
                .shapes(new Shape.Rectangle(0.2f), drawableShape)
                .sizes(new Size(12, 5f, 0.2f))
                .position(0.0, 0.0, 1.0, 0.0)
                .build();
                konfettiView.start(party);
                parade(); // invokes the confetti animation in parade style


        /**
         * When the user clicks the save button, the QR code is saved to the database
         * and the user is redirected to the scanner activity
         * @param v - the view
         */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                String uid = AuthController.getUsername(QRCodeEditActivity.this);

                ScannerController scannerController = new ScannerController();

                PlayerDB db = new PlayerDB(new DBConnector());
                if (photo != null) {
                    QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());
                    qrCodeDB.addLocationImageToServer(photo, name+username);
                }
                db.getPlayer(uid, new PlayerCallback() {
                    @Override
                    public void onPlayerCallback(Player player) {
                        //Add current player's username to list of players that have scanned a QR code
                        String username = player.getUsername();
                        //ArrayList<String> playersScannedBy = new ArrayList<>();
                        //playersScannedBy.add(username);
                        //Create a new QR code and add it to the database. Location image can be set by clicking add image button
                        QRCode qrCode = new QRCode(hash, name, points, uid, coordinates, "", new ArrayList<>(), 1, new ArrayList<>());
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
     * Builds the confetti in a parade style!
     * Source: https://github.com/DanielMartinus/Konfetti
     */
    public void parade() {
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(30);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.RIGHT - 45)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(0.0, 0.5))
                        .build(),
                new PartyFactory(emitterConfig)
                        .angle(Angle.LEFT + 45)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(1.0, 0.5))
                        .build()
        );
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

    /***
     * Function to handle the result of the camera intent
     * @param requestCode - the request code
     * @param resultCode - the result code
     * @param data - the data
     */
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
