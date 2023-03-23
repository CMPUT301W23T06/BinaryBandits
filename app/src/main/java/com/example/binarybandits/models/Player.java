package com.example.binarybandits.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class for a Player object.
 * Outstanding issues: N/A
 */
public class Player {

    /**
     * Empty constructor for creating objects through DB
     */
    public Player() {}

    /**
     * Constructor for creating a Player account without a phone number
     * @param username player's unique username
     */
    public Player(String username) {
        this.username = username;
        this.phone = null;
        this.totalScore = 0;
        this.totalQRCodes = 0;
        this.playerAvatar = null; //temporary
        this.qrCodesScanned = new ArrayList<QRCode>();
        this.highestScore = 0;
    }

    /**
     * Constructor for creating a Player account with a phone number
     * @param username player's username
     * @param phone player's phone number
     */
    public Player(String username, String phone) {
        this.username = username;
        this.phone = phone;
        this.totalScore = 0;
        this.totalQRCodes = 0;
        this.playerAvatar = null; //temporary
        this.qrCodesScanned = new ArrayList<QRCode>();
        this.highestScore = 0;
    }

    /**
     * Constructor used to create Players from documents in the database
     * @param username player's username
     * @param phone player's phone number
     * @param totalScore total score of a player's scanned QR codes
     * @param totalQRCodes number of QR codes a player has scanned
     * @param playerAvatar player's avatar picture
     * @param qrCodesScanned list of QR code objects scanned by the player
     */
    public Player(String username, String phone, int totalScore, int totalQRCodes, Bitmap playerAvatar, ArrayList<QRCode> qrCodesScanned, int highestScore) {
        this.username = username;
        this.phone = phone;
        this.totalScore = totalScore;
        this.totalQRCodes = totalQRCodes;
        this.playerAvatar = playerAvatar;
        this.qrCodesScanned = qrCodesScanned;
        this.highestScore = highestScore;
    }

    String username;
    String phone;
    int totalScore;
    int totalQRCodes;
    Bitmap playerAvatar;
    ArrayList<QRCode> qrCodesScanned;
    int highestScore;


    /**
     * Get the player's username
     * @return Return the player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's username
     * @param username player's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the player's phone number
     * @return Return the player's phone number. Returns null if the player does not have a phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the player's phone number
     * @param phone player's phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the player's total score of all QR codes owned
     * @return Return the total score of all QR codes owned
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Set the player's total score of all QR codes owned
     * @param totalScore total score of all QR codes owned
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Get the player's avatar as a Bitmap
     * @return Return the player's avatar
     */
    public Bitmap getPlayerAvatar() {
        return playerAvatar;
    }

    /**
     * Sets the player's avatar as a Bitmap
     * @param playerAvatar Bitmap containing an avatar
     */
    public void setPlayerAvatar(Bitmap playerAvatar) {
        this.playerAvatar = playerAvatar;
    }

    /**
     * Gets the total QR codes scanned
     * @return Return the total QR codes scanned
     */
    public int getTotalQRCodes() {
        return totalQRCodes;
    }

    /**
     * Sets the total QR codes scanned. If totalQRCodes is negative, set totalQRCodes to 0.
     * @param totalQRCodes new total QR codes scanned
     */
    public void setTotalQRCodes(int totalQRCodes) {
        if (totalQRCodes >= 0) {
            this.totalQRCodes = totalQRCodes;
        }
        else {
            this.totalQRCodes = 0;
        }
    }

    /**
     * Get the points value of a Player's highest scoring QR code
     * @return Return the points value of the Player's highest scoring QR code
     */
    public int getHighestScore() {
        return highestScore;
    }

    /**
     * Set the points value of a Player's highest scoring QR code
     * @param highestScore Points value of the Player's highest scoring QR code
     */
    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    /**
     * Gets list of scanned QR codes
     * @return Return the list of scanned QR codes
     */
    public ArrayList<QRCode> getQrCodesScanned() {
        return qrCodesScanned;
    }

    /**
     * Sets list of scanned QR codes to a new ArrayList
     * @param qrCodesScanned new ArrayList of QR codes scanned
     */
    public void setQrCodesScanned(ArrayList<QRCode> qrCodesScanned) {
        this.qrCodesScanned = qrCodesScanned;
    }

    /**
     * Increases total QR codes by 1
     */
    public void incrementTotalQRCodes() {
        this.totalQRCodes += 1;
    }

    /**
     * Decreases total QR codes by 1
     */
    public void decrementTotalQRCodes() {
        if (totalQRCodes >= 1) {
            this.totalQRCodes -= 1;
        }
        else {
            this.totalQRCodes = 0;
        }
    }

    /**
     * Adds a QRCode to list of scanned QR codes if the QRCode is not in the list
     * @param qrCode QRCode to add to list of scanned QR codes
     */
    public void addQRCodeScanned(QRCode qrCode) {
        if (findQRCodeScanned(qrCode)) {
            throw new IllegalArgumentException();
        }
        qrCodesScanned.add(qrCode);
    }

    /**
     * Looks for a QRCode in list of scanned QR codes
     * @param qrCode QRCode to look for in list of scanned QR codes
     * @return Return true if the qrCode is found in list of scanned QR codes, false otherwise
     */
    public boolean findQRCodeScanned(QRCode qrCode) {
        for (int i = 0; i < qrCodesScanned.size(); i++) {
            if (qrCodesScanned.get(i).getName().equals(qrCode.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a QRCode from the list of scanned QR codes if the QR code is in the list
     * @param qrCode QRCode to remove in list of scanned QR codes
     */
    public void removeQRCodeScanned(QRCode qrCode) {
        //Referenced: https://stackoverflow.com/questions/8520808/how-to-remove-specific-object-from-arraylist-in-java
        //Author: https://stackoverflow.com/users/1899700/tmh
        //License: CC BY-SA 4.0
        if(!qrCodesScanned.removeIf(qrCode1 -> qrCode.getName().equals(qrCode1.getName()))) {
            throw new IllegalArgumentException();
        }
    }
}
