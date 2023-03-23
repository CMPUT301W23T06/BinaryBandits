package com.example.binarybandits;

import com.example.binarybandits.models.Comment;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.google.type.DateTime;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    /**
     * Tests the getters and setters for name
     */
    @Test
    public void testQRCodeName() {
        QRCode mockQRCode = mockQRCode();
        assertEquals(mockQRCode.getName(), "SuperAmazingFerret");

        mockQRCode.setName("AwesomeSquirrel");
        assertEquals(mockQRCode.getName(), "AwesomeSquirrel");
    }

    /**
     * Tests the getters and setters for a QRCode's hash
     */
    @Test
    public void testQRCodeHash() {
        QRCode mockQRCode = mockQRCode();
        assertEquals(mockQRCode.getHash(), "cdf07521489cd151da290b3315207a61935357af5fe5614df7668b30a1d6f672");

        mockQRCode.setHash("bf0d42b5f0e81e7268ca4af3aa1794e14bb434ffbb739e0a89af0a6272a4682d");
        assertEquals(mockQRCode.getHash(), "bf0d42b5f0e81e7268ca4af3aa1794e14bb434ffbb739e0a89af0a6272a4682d");
    }

    /**
     * Tests the getters and setters for the points a QR code is worth
     */
    @Test
    public void testQRCodePoints() {
        QRCode mockQRCode = mockQRCode();
        assertEquals(mockQRCode.getPoints(), 47);

        mockQRCode.setPoints(3);
        assertEquals(mockQRCode.getPoints(), 3);
    }

    /**
     * Tests the getters and setters for the coordinates of a QR code
     */
    @Test
    public void testCoordinates() {
        QRCode mockQRCode = mockQRCodeWithCoordinates();
        ArrayList<Double> coordinates = new ArrayList<Double>();
        coordinates.add(53.5282595);
        coordinates.add(-113.5301112);
        assertEquals(mockQRCode.getCoordinates(), coordinates);
   }

   @Test
   public void testLocationImage() {
        QRCode mockQRCode = mockQRCode();
        //Check that a QR code does not have a location image on creation. Players need to add a location image
        assertNull(mockQRCode.getLocationImage());

        //mockQRCode.setLocationImage("");
   }

    /**
     * Tests getting number of players a QR code is scanned by and incrementing/decrementing
     * number of players scanned by.
     */
    @Test
    public void testNumPlayersScannedBy() {
        QRCode mockQRCode = mockQRCode();
        //Checks that numPlayersScannedBy() is initially 1
        assertEquals(mockQRCode.getNumPlayersScannedBy(), 1);

        //Checks that numPlayersScannedBy() increases by 1 when incrementNumPlayersScannedBy() is called
        mockQRCode.incrementNumPlayersScannedBy();
        assertEquals(mockQRCode.getNumPlayersScannedBy(), 2);

        //Checks that numPlayersScannedBy() decreases by 1 when decrementNumPlayersScannedBy() is called
        mockQRCode.decrementNumPlayersScannedBy();
        assertEquals(mockQRCode.getNumPlayersScannedBy(), 1);

        //Checks that numPlayersScannedBy() is never negative
        mockQRCode.decrementNumPlayersScannedBy();
        mockQRCode.decrementNumPlayersScannedBy();
        assertEquals(mockQRCode.getNumPlayersScannedBy(), 0);
    }


    /**
     * Tests getters and setters for a QR code's comments
     * (COMMENTED OUT WHILE COMMENTS ARE BEING IMPLEMENTED)
     */
    /*@Test
    public void testComments() {
        QRCode mockQRCode = mockQRCode();
        //A QRCode should initially have no comments
        assertNull(mockQRCode.getComments());

        ArrayList<Comment> comments = new ArrayList<Comment>();
        //TODO: Need to add comments to ArrayList in test

        assertEquals(mockQRCode.getComments(), comments);
    }*/

    /**
     * Tests getter for image URL. Note that there is no setter for image URL since
     * the URL is determined by hash which has a setter.
     */
    @Test
    public void testImageURL() {
        QRCode mockQRCode = mockQRCode();
        String imageURL = "https://api.dicebear.com/5.x/shapes/png?seed=" + mockQRCode.getHash();
        assertEquals(mockQRCode.getImageURL(), imageURL);
    }

    /**
     * Tests getter and setter for username of first person to scan a QR code
     */
    @Test
    public void testScannerUID() {
        QRCode mockQRCode = mockQRCode();
        mockQRCode.setScannerUID("toast");
        assertEquals(mockQRCode.getScannerUID(), "toast");
    }
}
