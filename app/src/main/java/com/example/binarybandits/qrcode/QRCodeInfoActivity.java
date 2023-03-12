package com.example.binarybandits.qrcode;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.squareup.picasso.Picasso;

public class QRCodeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qrpage);


        QRCodeDB db_qr = new QRCodeDB(new DBConnector());
        PlayerDB db_player = new PlayerDB(new DBConnector());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Bundle extras = getIntent().getExtras();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            //String name = extras.getString("name");


            String name = extras.getString("name");
            String player_user = extras.getString("username");
            db_qr.getQRCode(name, new QRCodeCallback() {
                @Override
                public void onQRCodeCallback(QRCode qrCode) {
                    String hash = qrCode.getHash();
                    String score = Integer.toString(qrCode.getPoints());

                    ImageView qr_image = findViewById(R.id.QRImageView);
                    TextView qr_name = findViewById(R.id.qr_code_name);
                    TextView qr_score = findViewById(R.id.qr_code_score);
                    ImageButton delete_button = findViewById(R.id.delete_button);
                    String url = "https://api.dicebear.com/5.x/shapes/png?seed=" + hash;

                    Picasso.get().load(url).into(qr_image);
                    qr_name.setText(name);
                    qr_score.setText(score);

                    delete_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeInfoActivity.this);
                            builder.setMessage("Are you sure you want to delete this QR code from your profile?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            db_player.getPlayer(player_user, new PlayerCallback() {
                                                @Override
                                                public void onPlayerCallback(Player player) {
                                                    player.removeQRCodeScanned(qrCode);
                                                    player.decrementTotalQRCodes();
                                                    int newScore = player.getTotalScore() - qrCode.getPoints();
                                                    player.setTotalScore(newScore);
                                                    db_player.updatePlayer(player);
                                                }
                                            });

                                            QRCodeInfoActivity.this.finish();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    });
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
