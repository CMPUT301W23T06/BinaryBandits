package com.example.binarybandits.models;
import android.graphics.Bitmap;
import android.util.Pair;

import com.example.binarybandits.Geolocation;

import java.util.ArrayList;

public class QRCode {

    /**
     * Constructor for QRCodes without Geolocation
     * @param hash
     * @param name
     * @param points
     */
    public QRCode(String hash, String name, int points) {
        this.hash = hash;
        this.name = name;
        this.points = points;
        this.numPlayersScannedBy = 1;
    }

    /**
     * Constructor for QRCodes with a Geolocation
     * @param hash
     * @param name
     * @param points
     * @param coordinates
     */
    public QRCode(String hash, String name, int points, Geolocation coordinates) {
        this.hash = hash;
        this.name = name;
        this.points = points;
        this.coordinates = coordinates;
        this.numPlayersScannedBy = 1;
    }


    public QRCode(String hash, String name, int points, String scannerUID, Geolocation coordinates,
                  Bitmap locationImage, ArrayList<String> comments, int numPlayersScannedBy) {
        this.hash = hash;
        this.name = name;
        this.points = points;
        this.scannerUID = scannerUID;
        this.coordinates = coordinates;
        this.locationImage = locationImage;
        this.comments = comments;
        this.numPlayersScannedBy = numPlayersScannedBy;
    }

    String hash;
    String name;
    int points;
    String scannerUID;
    Geolocation coordinates; //Need to change to Geolocation class
    Bitmap locationImage;
    ArrayList<String> comments;
    int numPlayersScannedBy;

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

    public Geolocation getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Geolocation coordinates) {
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

    public int getNumPlayersScannedBy() {
        return numPlayersScannedBy;
    }

    public void incrementNumPlayersScannedBy() {
        this.numPlayersScannedBy = numPlayersScannedBy + 1;
    }

    public void decrementNumPlayersScannedBy() {
        this.numPlayersScannedBy = numPlayersScannedBy - 1;
    }


}
