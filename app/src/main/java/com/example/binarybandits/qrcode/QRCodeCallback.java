package com.example.binarybandits.qrcode;

import com.example.binarybandits.models.QRCode;

// Referenced:

/**
 * An interface containing a callback function used to retrieve QR code from the database.
 * Outstanding issues: N/A
 */
public interface QRCodeCallback {
    void onQRCodeCallback(QRCode qrCode);
}
