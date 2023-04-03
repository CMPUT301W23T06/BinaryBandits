package com.example.binarybandits;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.controllers.ScannerController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.ui.QRedit.QRCodeEditActivity;
import com.example.binarybandits.controllers.QRController;
import com.google.zxing.Result;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

/**
 * An activity that uses the device's camera to scan QR codes. Scanned QR codes are added to a player's
 * list of scanned QR codes and added/updated in both the QR code database and the Player database
 */
public class ScanQRActivity extends AppCompatActivity {

    private CodeScannerView scannerView;
    private CodeScanner codeScanner;
    private QRController qrController;
    private ScannerController scannerController;
    private KonfettiView konfettiView = null;
    private Shape.DrawableShape drawableShape = null;

    /**
     * Create the ScanQRActivity
     * @param savedInstanceState the saved instance state that is restored after the app crashes
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqr);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        qrController = new QRController();
        scannerController = new ScannerController();
        scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);


        /**
         * This is the callback function that is called when a QR code is scanned. The QR code is
         * added to the player's list of scanned QR codes and added/updated in both the QR code
         * database and the Player database.
         * param - The callback function's parameter is the result of the QR code scan.
         */
        codeScanner.setDecodeCallback(new DecodeCallback() {
            /***
             * This function switches to the QRCodeEditActivity and passes the QR code's hash to the activity.
             * @param result - the result of the QR code scan.
             */
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String contents = result.getText();
                        String hash = qrController.getHash(contents);
                        String name = qrController.generateUniqueName(hash);
                        int points = qrController.calculatePoints(hash);
                        System.out.println("Contents: " + contents);
                        System.out.println("Hash: " + hash);
                        System.out.println("Unique Name: " + name);
                        System.out.println("Points: " + points);
                        QRCode qrCode = new QRCode(hash, name, points);

                        Intent myIntent = new Intent(ScanQRActivity.this, QRCodeEditActivity.class);
                        myIntent.putExtra("hash", hash);
                        ScanQRActivity.this.startActivity(myIntent); // start the QRCodeEditActivity
                    }
                });
            }
        });
        scannerView.setOnClickListener(view -> codeScanner.startPreview());
    }


    /***
     * This function is called when the activity is resumed. It starts the camera preview.
     */
    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    /***
     * This function is called when the activity is paused. It releases the camera resources.
     */
    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}
