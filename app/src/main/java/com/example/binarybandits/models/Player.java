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
    public Player(String username, String phone, int totalScore, int totalQRCodes, Bitmap playerAvatar, ArrayList<QRCode> qrCodesScanned) {
        this.username = username;
        this.phone = phone;
        this.totalScore = totalScore;
        this.totalQRCodes = totalQRCodes;
        this.playerAvatar = playerAvatar;
        this.qrCodesScanned = qrCodesScanned;
    }

    String username;
    String phone;
    int totalScore;
    int totalQRCodes;
    Bitmap playerAvatar;
    ArrayList<QRCode> qrCodesScanned;


    /**
     * Get the player's username
     * @return Return the player's username
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public Bitmap getPlayerAvatar() {
        return playerAvatar;
    }

    public void setPlayerAvatar(Bitmap playerAvatar) {
        this.playerAvatar = playerAvatar;
    }

    public int getTotalQRCodes() {
        return totalQRCodes;
    }

    public void setTotalQRCodes(int totalQRCodes) {
        if (totalQRCodes >= 0) {
            this.totalQRCodes = totalQRCodes;
        }
        else {
            this.totalQRCodes = 0;
        }
    }

    public ArrayList<QRCode> getQrCodesScanned() {
        return qrCodesScanned;
    }

    public void setQrCodesScanned(ArrayList<QRCode> qrCodesScanned) {
        this.qrCodesScanned = qrCodesScanned;
    }

    public void incrementTotalQRCodes() {
        this.totalQRCodes += 1;
    }

    public void decrementTotalQRCodes() {
        if (totalQRCodes >= 1) {
            this.totalQRCodes -= 1;
        }
    }

    public void addQRCodeScanned(QRCode qrCode) {
        if (findQRCodeScanned(qrCode)) {
            throw new IllegalArgumentException();
        }
        qrCodesScanned.add(qrCode);
    }

    public boolean findQRCodeScanned(QRCode qrCode) {
        for (int i = 0; i < qrCodesScanned.size(); i++) {
            if (qrCodesScanned.get(i).getName().equals(qrCode.getName())) {
                return true;
            }
        }
        return false;
    }

    public void removeQRCodeScanned(QRCode qrCode) {
        //Referenced: https://stackoverflow.com/questions/8520808/how-to-remove-specific-object-from-arraylist-in-java
        //Author: https://stackoverflow.com/users/1899700/tmh
        //License: CC BY-SA 4.0
        if(!qrCodesScanned.removeIf(qrCode1 -> qrCode.getName().equals(qrCode1.getName()))) {
            throw new IllegalArgumentException();
        }
    }
}
