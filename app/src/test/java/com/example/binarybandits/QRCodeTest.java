package com.example.binarybandits;

import com.example.binarybandits.models.QRCode;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Unit tests for the QRCode model class
 */
public class QRCodeTest {

    /**
     * Creates a mock QR code without coordinates for testing
     * @return Returns a QR code without coordinates
     */
    public QRCode mockQRCode() {
        String hash = "cdf07521489cd151da290b3315207a61935357af5fe5614df7668b30a1d6f672";
        return new QRCode(hash, "SuperAmazingFerret", 47);
    }

    /**
     * Creates a mock QR code with coordinates for testing
     * @return Returns a QR code with coordinates
     */
    public QRCode mockQRCodeWithCoordinates() {
        String hash = "cdf07521489cd151da290b3315207a61935357af5fe5614df7668b30a1d6f672";
        ArrayList<Double> coordinates = new ArrayList<Double>();
        coordinates.add(53.5282595);
        coordinates.add(-113.5301112);
        return new QRCode(hash, "SuperAmazingFerret", 47, coordinates);
    }

    @Test
    public void testQRCodeName() {

    }
}
