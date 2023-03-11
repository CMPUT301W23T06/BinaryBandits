package com.example.binarybandits.qrcode;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.squareup.picasso.Picasso;

public class QRCodeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qrpage);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();
        String hash;

        if (extras != null) {
            hash = extras.getString("hash");
            System.out.println("yessssssssss");
        } else {
            hash = AuthController.getUsername(QRCodeInfoActivity.this);
        }

        String url = "https://api.dicebear.com/5.x/shapes/png?seed=" + hash;
        ImageView imageView = findViewById(R.id.QRImageView);
        Picasso.get().load(url).into(imageView);


        Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            QRCodeInfoActivity.this.finish();
            }
        });



    }
}
