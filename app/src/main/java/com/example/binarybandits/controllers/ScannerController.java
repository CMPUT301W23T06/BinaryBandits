package com.example.binarybandits.controllers;

import android.util.Log;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.qrcode.QRCodeDB;

/**
 * Controller for adding a QRCode to a Player's profile
 * Outstanding issues: N/A
 */
public class ScannerController {

    /**
     * Add a QR Code to a player's list of QR codes. Update player in database to include added QR
     * code in the player's list
     * @param qrCode QR code to add to player's list
     * @param player Player to add QR code to
     * @param shareLocation True if location shared, false otherwise
     */
    public void addQRCode(QRCode qrCode, Player player, boolean shareLocation) {
        if(!shareLocation) {
            qrCode.setCoordinates(null);
        }
        QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());
        PlayerDB playerDB = new PlayerDB(new DBConnector());
        playerDB.getPlayer(player.getUsername(), new PlayerCallback() {
            @Override
            public void onPlayerCallback(Player updatedPlayer) {
                Log.d("ScannerController", updatedPlayer.getQrCodesScanned().toString());
                if(!updatedPlayer.findQRCodeScanned(qrCode.getName())) {
                    // Add/Update QR code on the database
                    qrCodeDB.addQRCode(qrCode, updatedPlayer.getUsername()); //Add QRCode to database if it is not there already
                    updatedPlayer.addQRCodeScanned(qrCode.getName()); //Add QRCode to player's profile (locally)
                    updatedPlayer.incrementTotalQRCodes();
                    int points = qrCode.getPoints();

                    //Update player's highest score if needed
                    if(points > updatedPlayer.getHighestScore()) {
                        updatedPlayer.setHighestScore(points);
                    }
                    //Calculate player's new score with scanned QR code
                    int newScore = updatedPlayer.getTotalScore() + points;
                    updatedPlayer.setTotalScore(newScore);
                    //Update player information in database
                    playerDB.updatePlayer(updatedPlayer);
                }
            }
        });
    }
}
