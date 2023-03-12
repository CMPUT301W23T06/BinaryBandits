package com.example.binarybandits.controllers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.qrcode.QRCodeDB;
import com.google.zxing.Result;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Controller for adding a QRCode to a Player's profile
 * Outstanding issues: N/A
 */
public class ScannerController {
    public void addQRCode(QRCode qrCode, Player player) {
        QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());
        PlayerDB playerDB = new PlayerDB(new DBConnector());
        playerDB.getPlayer(player.getUsername(), new PlayerCallback() {
            @Override
            public void onPlayerCallback(Player updatedPlayer) {
                Log.d("ScannerController", updatedPlayer.getQrCodesScanned().toString());
                if(!updatedPlayer.getQrCodesScanned().contains(qrCode)) {
                    qrCodeDB.addQRCode(qrCode); //Add QRCode to database if it is not there already
                    updatedPlayer.addQRCodeScanned(qrCode); //Add QRCode to player's profile (locally)
                    updatedPlayer.incrementTotalQRCodes();

                    //Calculate player's new score with scanned QR code
                    int newScore = updatedPlayer.getTotalScore() + qrCode.getPoints();
                    updatedPlayer.setTotalScore(newScore);
                    //Update player information in database
                    playerDB.updatePlayer(updatedPlayer);
                }
            }
        });
    }
}
