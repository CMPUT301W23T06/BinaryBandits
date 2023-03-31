package com.example.binarybandits.qrcode;

import com.example.binarybandits.models.QRCode;

import java.util.ArrayList;

/**
 * An interface containing a callback function used to retrieve a list of QR codes from the database.
 * Outstanding issues: N/A
 */
public interface QRCodeListCallback {
    void onQRCodeListCallback(ArrayList<QRCode> qrCodeList);
}
