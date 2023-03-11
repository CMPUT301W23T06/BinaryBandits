package com.example.binarybandits.qrcode;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;

public class QRCodeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrinfo_temp);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();
        String hash;

        if (extras != null) {
            hash = extras.getString("hash");
        } else {
            hash = AuthController.getUsername(QRCodeInfoActivity.this);
        }

        ImageView imageView = findViewById(R.id.imageView);
        DownloadImageTask.loadQRImageIntoView(imageView, hash);

    }
}
