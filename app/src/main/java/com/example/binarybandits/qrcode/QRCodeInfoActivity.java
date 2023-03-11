package com.example.binarybandits.qrcode;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.models.QRCode;
import com.squareup.picasso.Picasso;

public class QRCodeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qrpage);


        QRCodeDB db = new QRCodeDB(new DBConnector());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            String name = extras.getString("name");
            db.getQRCode(name, new QRCodeCallback() {
                @Override
                public void onQRCodeCallback(QRCode qrCode) {
                    String hash = qrCode.getHash();
                    String score = Integer.toString(qrCode.getPoints());

                    ImageView qr_image = findViewById(R.id.QRImageView);
                    TextView qr_name = findViewById(R.id.qr_code_name);
                    TextView qr_score = findViewById(R.id.qr_code_score);


                    String url = "https://api.dicebear.com/5.x/shapes/png?seed=" + hash;
                    Picasso.get().load(url).into(qr_image);

                    qr_name.setText(name);
                    qr_score.setText(score);


                }
            });
        } else {
            System.out.println("error");
        }







        Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            QRCodeInfoActivity.this.finish();
            }
        });



    }
}
