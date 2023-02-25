package com.example.binarybandits;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import java.util.Objects;

public class ScanQRActivity  extends AppCompatActivity  {

    private CodeScannerView scannerView;
    private CodeScanner codeScanner;
    private QRController qrController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqr);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        qrController = new QRController();
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
                        System.out.println("Contents: " + contents);
                        System.out.println("Hash: " + hash);
                        System.out.println("Unique Name: " + name);
                    }
                });
            }
        });
        scannerView.setOnClickListener(view -> codeScanner.startPreview());
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
