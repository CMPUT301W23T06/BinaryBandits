package com.example.binarybandits;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.binarybandits.controllers.ScannerController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.binarybandits.controllers.QRController;
import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.google.zxing.Result;

public class ScanQRActivity extends AppCompatActivity {

    private CodeScannerView scannerView;
    private CodeScanner codeScanner;
    private QRController qrController;
    private ScannerController scannerController;

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
                        getCurrentPlayer(qrCode);
                        Intent myIntent = new Intent(ScanQRActivity.this, QRCodeInfoActivity.class);
                        myIntent.putExtra("hash", hash); // Optional parameters
                        ScanQRActivity.this.startActivity(myIntent);
                    }
                });
            }
        });
        scannerView.setOnClickListener(view -> codeScanner.startPreview());
    }

    public void getCurrentPlayer(QRCode qrCode) {
        //I'm still debugging this method. Not ready to use
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(scannerView.getContext());
        String username = preferences.getString("login_username", "");
        PlayerDB db = new PlayerDB(new DBConnector());

        db.getPlayer(username, new PlayerCallback() {
            @Override
            public void onPlayerCallback(Player player) {
                scannerController.addQRCode(qrCode, player);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}
