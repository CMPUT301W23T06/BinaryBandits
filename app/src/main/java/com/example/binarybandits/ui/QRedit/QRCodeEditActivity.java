package com.example.binarybandits.ui.QRedit;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.qrcode.DownloadImageTask;

public class QRCodeEditActivity extends AppCompatActivity {

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

        ImageView imageView = findViewById(R.id.imageView);
        DownloadImageTask.loadQRImageIntoView(imageView, hash);

    }

}
