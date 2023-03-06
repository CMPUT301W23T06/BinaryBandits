package com.example.binarybandits.qrcode;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.binarybandits.R;

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
            hash = extras.getString("name");
            // and get whatever type user account id is
        } else {
            hash = "wildlife";
        }

//        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
//                .execute("https://picsum.photos/seed/" + hash + "/300");

        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                .execute("https://api.api-ninjas.com/v1/randomimage?category=" + hash);



    }
}
