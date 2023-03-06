package com.example.binarybandits.models;

import android.graphics.Bitmap;
import java.util.ArrayList;

public class Player {

    public Player(String username, String phone, int totalScore, Bitmap playerAvatar, ArrayList<QRCode> qrCodesScanned) {
        this.username = username;
        this.phone = phone;
        this.totalScore = totalScore;
        this.playerAvatar = playerAvatar;
        this.qrCodesScanned = qrCodesScanned;
    }

    String username;
    String phone;
    int totalScore;
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

    public ArrayList<QRCode> getQrCodesScanned() {
        return qrCodesScanned;
    }

    public void setQrCodesScanned(ArrayList<QRCode> qrCodesScanned) {
        this.qrCodesScanned = qrCodesScanned;
    }
}
