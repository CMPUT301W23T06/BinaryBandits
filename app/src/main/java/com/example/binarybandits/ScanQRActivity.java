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
     *
     * @param savedInstanceState
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


        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);
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
                .build();
        konfettiView.setOnClickListener(view ->
                konfettiView.start(party)
        );


        codeScanner.setDecodeCallback(new DecodeCallback() {
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
                        ScanQRActivity.this.startActivity(myIntent);
                        explode();
                    }
                });
            }
        });
        scannerView.setOnClickListener(view -> codeScanner.startPreview());
    }

    public void explode() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .spread(360)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0.3))
                        .build()
        );
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    /**
     *
     */
    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}
