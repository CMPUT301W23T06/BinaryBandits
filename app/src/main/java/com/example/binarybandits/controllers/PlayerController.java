package com.example.binarybandits.controllers;

import android.util.Log;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.ScoreCallback;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.qrcode.QRCodeDB;
import com.example.binarybandits.qrcode.QRCodeListCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Controller for Player that makes changes to Player model class.
 * Outstanding issues: N/A
 */
public class PlayerController {

    private QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());
    private Player player;

    /**
     * Constructor for PlayerController
     * @param player player to make changes to
     */
    public PlayerController(Player player) {
        this.player = player;
    }

    /**
     * Gets the highest scoring QR code scanned by a Player
     * @return Returns the highest scoring QR code scanned by a Player
     */
    public void getHighestQRCode(ScoreCallback callback) {
        int highestQRCode;
        ArrayList<String> qrCodeNames = player.getQrCodesScanned();
        if (qrCodeNames.size() == 0) {
            highestQRCode = 0;
            player.setHighestScore(0);
            callback.scoreCallback(highestQRCode);
        }
        else {
            qrCodeDB.getPlayerQRCodes(player.getUsername(), new QRCodeListCallback() {
                @Override
                public void onQRCodeListCallback(ArrayList<QRCode> qrCodeList) {
                    Log.d("Controller", qrCodeList.toString());
                    qrCodeList = sortQRCodes(qrCodeList);
                    int highestQRCode = qrCodeList.get(qrCodeList.size() - 1).getPoints();
                    player.setHighestScore(highestQRCode);
                    callback.scoreCallback(highestQRCode);
                }
            });
        }
    }

    /**
     * Gets the lowest QR code scanned by a Player
     * @return Returns the lowest scoring QR code scanned by a Player
     */
    public void getLowestQRCode(ScoreCallback callback) {
        int lowestQRCode;
        ArrayList<String> qrCodeNames = player.getQrCodesScanned();
        if (qrCodeNames.size() == 0) {
            lowestQRCode = 0;
            callback.scoreCallback(lowestQRCode);
        }
        else {
            qrCodeDB.getPlayerQRCodes(player.getUsername(), new QRCodeListCallback() {
                @Override
                public void onQRCodeListCallback(ArrayList<QRCode> qrCodeList) {
                    qrCodeList = sortQRCodes(qrCodeList);
                    int lowestQRCode = qrCodeList.get(0).getPoints();
                    callback.scoreCallback(lowestQRCode);
                }
            });
        }
    }

    /**
     * Sorts a list of QRCodes in ascending order
     * @param qrCodesScanned list of QRCodes to sort
     * @return Return the sorted list of QRCodes
     */
    public ArrayList<QRCode> sortQRCodes(ArrayList<QRCode> qrCodesScanned) {
        Collections.sort(qrCodesScanned, new Comparator<QRCode>() {
            @Override
            public int compare(QRCode qrCode1, QRCode qrCode2) {
                if(qrCode1.getPoints() > qrCode2.getPoints()) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
        return qrCodesScanned;
    }

}
