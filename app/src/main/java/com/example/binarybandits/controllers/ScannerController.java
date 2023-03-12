package com.example.binarybandits.controllers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
 *
 */
public class ScannerController {
    public void addQRCode(QRCode qrCode, Player player) {
        QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());
        PlayerDB playerDB = new PlayerDB(new DBConnector());
        if(!player.getQrCodesScanned().contains(qrCode)) {
            qrCodeDB.addQRCode(qrCode); //Add QRCode to database if it is not there already
            player.addQRCodeScanned(qrCode); //Add QRCode to player's profile (locally)
            player.incrementTotalQRCodes();

            //Calculate player's new score with scanned QR code
            int newScore = player.getTotalScore() + qrCode.getPoints();
            player.setTotalScore(newScore);
            //Update player information in database
            playerDB.updatePlayer(player);
        }
    }
}
