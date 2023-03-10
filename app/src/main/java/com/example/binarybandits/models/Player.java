package com.example.binarybandits.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class Player {

    /**
     * Empty constructor for creating objects through DB
     */
    public Player() {}
    public Player(String username) {
        this.username = username;
        this.phone = null;
        this.totalScore = 0;
        this.totalQRCodes = 0;
        this.playerAvatar = null; //temporary
        this.qrCodesScanned = new ArrayList<QRCode>();
    }

    public Player(String username, String phone) {
        this.username = username;
        this.phone = phone;
        this.totalScore = 0;
        this.totalQRCodes = 0;
        this.playerAvatar = null; //temporary
        this.qrCodesScanned = new ArrayList<QRCode>();
    }
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
        this.totalQRCodes = totalQRCodes;
    }

    public ArrayList<QRCode> getQrCodesScanned() {
        return qrCodesScanned;
    }

    public void setQrCodesScanned(ArrayList<QRCode> qrCodesScanned) {
        this.qrCodesScanned = qrCodesScanned;
    }

    public void addQRCodeScanned(QRCode qrCode) {
        qrCodesScanned.add(qrCode);
    }

    public void removeQRCodeScanned(QRCode qrCode) {
        qrCodesScanned.remove(qrCode);
    }

    public QRCode getHighestQRCode() {

        return null; //temporary
    }

    public QRCode getLowestQRCode() {

        return null; //temporary
    }
}
