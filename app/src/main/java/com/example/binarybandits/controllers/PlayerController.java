package com.example.binarybandits.controllers;

import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.qrcode.QRCodeDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlayerController {

    private PlayerDB playerDB;
    private QRCodeDB qrCodeDB;
    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    public QRCode getHighestQRCode() {
        QRCode highestQRCode;
        ArrayList<QRCode> qrCodesScanned = player.getQrCodesScanned();
        qrCodesScanned = sortQRCodes(qrCodesScanned);
        if(qrCodesScanned.size() > 0) {
            highestQRCode = qrCodesScanned.get(qrCodesScanned.size() - 1);
        }
        else {
            highestQRCode = null;
        }
        return highestQRCode;
    }

    /**
     *
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
