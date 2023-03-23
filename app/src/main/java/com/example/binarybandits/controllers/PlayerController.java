package com.example.binarybandits.controllers;

import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.qrcode.QRCodeDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Controller for Player that makes changes to Player model class.
 * Outstanding issues: N/A
 */
public class PlayerController {

    private PlayerDB playerDB;
    private QRCodeDB qrCodeDB;
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
    public QRCode getHighestQRCode() {
        QRCode highestQRCode;
        ArrayList<QRCode> qrCodesScanned = player.getQrCodesScanned();
        qrCodesScanned = sortQRCodes(qrCodesScanned);
        if(qrCodesScanned.size() > 0) {
            highestQRCode = qrCodesScanned.get(qrCodesScanned.size() - 1);
            player.setHighestScore(highestQRCode.getPoints());
        }
        else {
            highestQRCode = null;
            player.setHighestScore(0);
        }
        return highestQRCode;
    }

    /**
     * Gets the lowest QR code scanned by a Player
     * @return Returns the lowest scoring QR code scanned by a Player
     */
    public QRCode getLowestQRCode() {
        QRCode lowestQRCode;
        ArrayList<QRCode> qrCodesScanned = player.getQrCodesScanned();
        qrCodesScanned = sortQRCodes(qrCodesScanned);
        if(qrCodesScanned.size() > 0) {
            lowestQRCode = qrCodesScanned.get(0);
        }
        else {
            lowestQRCode = null;
        }
        return lowestQRCode;
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
