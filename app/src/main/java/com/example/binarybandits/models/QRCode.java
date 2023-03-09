package com.example.binarybandits.models;
import android.graphics.Bitmap;
import android.util.Pair;

import java.util.ArrayList;

public class QRCode {

    public QRCode(String hash, String name, int points, String scannerUID, Pair<Double, Double> coordinates, Bitmap locationImage, ArrayList<String> comments) {
        this.hash = hash;
        this.name = name;
        this.points = points;
        this.scannerUID = scannerUID;
        this.coordinates = coordinates;
        this.locationImage = locationImage;
        this.comments = comments;
    }

    String hash;
    String name;
    int points;
    String scannerUID;
    Pair<Double, Double> coordinates; //Need to change to Geolocation class
    Bitmap locationImage;
    ArrayList<String> comments;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getScannerUID() {
        return scannerUID;
    }

    public void setScannerUID(String scannerUID) {
        this.scannerUID = scannerUID;
    }

    public Pair<Double, Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Pair<Double, Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Bitmap getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(Bitmap locationImage) {
        this.locationImage = locationImage;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }
}
