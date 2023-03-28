package com.example.binarybandits;

import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Player model class
 */
public class PlayerTest {

    /**
     * Creates a mock player without a phone number for testing
     * @return Return a player without a phone number for testing
     */
    public Player mockPlayerNoPhone() {
        String username = "QRCodeEnjoyer";
        return new Player(username);
    }

    /**
     * Creates a mock player with a phone number for testing
     * @return Return a player with a phone number for testing
     */
    public Player mockPlayerWithPhone() {
        String username = "BestScannerNA";
        return new Player(username, "5879831023");
    }

    /**
     * Tests username getters and setters
     */
    @Test
    public void testUsername() {
        Player mockPlayer = mockPlayerWithPhone();

        //Check that getUsername gets the correct username
        assertEquals("BestScannerNA", mockPlayer.getUsername());

        //Check that setUsername changes the username
        mockPlayer.setUsername("BestScannerEU");
        assertEquals("BestScannerEU", mockPlayer.getUsername());

        //Check that setUsername works with all uppercase input
        mockPlayer.setUsername("JAMES");
        assertEquals("JAMES", mockPlayer.getUsername());

        //Check that setUsername works with numbers
        mockPlayer.setUsername("ConnorMcDavid97");
        assertEquals("ConnorMcDavid97", mockPlayer.getUsername());
    }

    /**
     * Tests phone number getters and setters
     */
    @Test
    public void testPhone() {
        //Check that getPhone get the correct phone number
        Player mockPlayerWithPhone = mockPlayerWithPhone();
        assertEquals("5879831023", mockPlayerWithPhone.getPhone());

        //Check that setPhone changes the phone number
        mockPlayerWithPhone.setPhone("7804172312");
        assertEquals("7804172312", mockPlayerWithPhone.getPhone());

        //Check that getPhone() returns null for a Player with no phone number
        Player mockPlayerNoPhone = mockPlayerNoPhone();
        assertNull(mockPlayerNoPhone.getPhone());
    }

    /**
     * Tests total score getters and setters
     */
    @Test
    public void testTotalScore() {
        Player mockPlayer = mockPlayerWithPhone();

        //Check that total score is initially 0
        assertEquals(0, mockPlayer.getTotalScore());

        //Check that total score changes when a QRCode is added
        int qrCodeScore = 13; //Simulated QRCode score
        int totalScore = mockPlayer.getTotalScore() + qrCodeScore;
        mockPlayer.setTotalScore(totalScore);
        assertEquals(13, mockPlayer.getTotalScore());

        qrCodeScore = 17; //Another simulated QRCode score
        totalScore = mockPlayer.getTotalScore() + qrCodeScore;
        mockPlayer.setTotalScore(totalScore);
        assertEquals(30, mockPlayer.getTotalScore());

        //Check that total score changes when a QRCode is removed
        qrCodeScore = 13;
        totalScore = mockPlayer.getTotalScore() - qrCodeScore;
        mockPlayer.setTotalScore(totalScore);
        assertEquals(17, mockPlayer.getTotalScore());
    }

    /**
     * Tests total QR codes getters and setters. Also tests functions used
     * to increment/decrement total QR codes
     */
    @Test
    public void testTotalQRCodes() {
        Player mockPlayer = mockPlayerWithPhone();

        //Check that total QR codes scanned is initially 0
        assertEquals(0, mockPlayer.getTotalQRCodes());

        //Simulate adding a QRCode to total
        mockPlayer.incrementTotalQRCodes();
        assertEquals(1, mockPlayer.getTotalQRCodes());

        //Simulate removing a QRCode from total
        mockPlayer.decrementTotalQRCodes();
        assertEquals(0, mockPlayer.getTotalQRCodes());

        //Check that totalQRCodes cannot become negative
        mockPlayer.decrementTotalQRCodes();
        assertEquals(0, mockPlayer.getTotalQRCodes());

        //Check setTotalQRCodes sets totalQRCodes correctly
        mockPlayer.setTotalQRCodes(5);
        assertEquals(5, mockPlayer.getTotalQRCodes());

        //Check that totalQRCodes cannot be set to a negative numbers
        mockPlayer.setTotalQRCodes(-1);
        assertEquals(0, mockPlayer.getTotalQRCodes());
    }

    /**
     * Tests adding a QR code to a player's list of QR codes scanned
     */
    @Test
    public void testAddQRCodeScanned() {
        Player mockPlayer = mockPlayerWithPhone();
        String hash = "bf0d42b5f0e81e7268ca4af3aa1794e14bb434ffbb739e0a89af0a6272a4682d";
        QRCode mockQRCode = new QRCode(hash, "SuperAmazingFerret", 47);

        //Check that qrCodesScanned is initially empty
        assertTrue(mockPlayer.getQrCodesScanned().isEmpty());

        //Check that a qrCode can be added to the list
        mockPlayer.addQRCodeScanned(mockQRCode);
        assertEquals(1, mockPlayer.getQrCodesScanned().size());

        //Check that a qrCode can be removed from list
        mockPlayer.removeQRCodeScanned(mockQRCode);
        assertEquals(0, mockPlayer.getQrCodesScanned().size());
    }

    /**
     * Tests adding a QR code that is already in a player's list
     */
    @Test
    public void testAddQRCodeScannedException() {
        Player mockPlayer = mockPlayerWithPhone();
        String hash = "bf0d42b5f0e81e7268ca4af3aa1794e14bb434ffbb739e0a89af0a6272a4682d";
        QRCode mockQRCode = new QRCode(hash, "SuperAmazingFerret", 47);

        //Check that qrCodesScanned is initially empty
        assertTrue(mockPlayer.getQrCodesScanned().isEmpty());

        //Check that a qrCode can be added to the list
        mockPlayer.addQRCodeScanned(mockQRCode);
        assertEquals(1, mockPlayer.getQrCodesScanned().size());

        //Check that a QRCode cannot be added to list if it is already in list
        assertThrows(IllegalArgumentException.class, () -> {
            mockPlayer.addQRCodeScanned(mockQRCode);
        });
    }

    /**
     * Tests removing a QR code from a player's list of scanned QR codes
     */
    @Test
    public void testRemoveQRCodeScanned() {
        Player mockPlayer = mockPlayerWithPhone();
        String hash = "cdf07521489cd151da290b3315207a61935357af5fe5614df7668b30a1d6f672";

        QRCode mockQRCode = new QRCode(hash, "SuperAmazingFerret", 47);

        //Check that qrCodesScanned is initially empty
        assertTrue(mockPlayer.getQrCodesScanned().isEmpty());

        //Check that a qrCode can be added to the list
        mockPlayer.addQRCodeScanned(mockQRCode);
        assertEquals(1, mockPlayer.getQrCodesScanned().size());

        //Check that a qrCode can be removed from list
        mockPlayer.removeQRCodeScanned(mockQRCode);
        assertEquals(0, mockPlayer.getQrCodesScanned().size());
    }

    /**
     * Tests removing a QR code that is not in a player's list of scanned QR codes
     */
    @Test
    public void testRemoveQRCodeScannedException() {
        Player mockPlayer = mockPlayerWithPhone();

        //Instantiate 2 QRCodes, one is removed successfully, the other is not in the list
        String hash = "cdf07521489cd151da290b3315207a61935357af5fe5614df7668b30a1d6f672";
        QRCode mockQRCode = new QRCode(hash, "SuperAmazingFerret", 47);
        hash = "bf0d42b5f0e81e7268ca4af3aa1794e14bb434ffbb739e0a89af0a6272a4682d";
        QRCode qrCode = new QRCode(hash, "MegaTirelessBlackBear", 9);

        //Check that qrCodesScanned is initially empty
        assertTrue(mockPlayer.getQrCodesScanned().isEmpty());

        //Check that a qrCode can be added to the list
        mockPlayer.addQRCodeScanned(mockQRCode);
        assertEquals(1, mockPlayer.getQrCodesScanned().size());

        //Check that a qrCode can be removed from list
        mockPlayer.removeQRCodeScanned(mockQRCode);
        assertEquals(0, mockPlayer.getQrCodesScanned().size());

        //Check that a qrCode not in the list cannot be removed
        assertThrows(IllegalArgumentException.class, () -> {
            mockPlayer.removeQRCodeScanned(qrCode);
        });
    }

}
