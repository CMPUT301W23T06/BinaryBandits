package com.example.binarybandits.models;
import android.graphics.Bitmap;
import android.util.Pair;

import com.example.binarybandits.Geolocation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Model class for a QR code object.
 * Outstanding issues: N/A
 */
public class QRCode {

    /**
     * Constructor for QRCodes without Geolocation
     * @param hash QRCode hash
     * @param name unique name of QRCode
     * @param points points QRCode is worth
     */
    public QRCode(String hash, String name, int points) {
        this.hash = hash;
        this.name = name;
        this.points = points;
        this.numPlayersScannedBy = 1;
    }

    /**
     * Constructor for QRCodes with a Geolocation
     * @param hash QRCode hash
     * @param name unique name of QRCode
     * @param points points QRCode is worth
     * @param coordinates pair containing latitude and longitude where QRCode was scanned
     */
    public QRCode(String hash, String name, int points, ArrayList<Double> coordinates) {
        this.hash = hash;
        this.name = name;
        this.points = points;
        this.coordinates = coordinates;
        this.numPlayersScannedBy = 1;
    }

    /**
     * Constructor used to create QRCodes from documents in database
     * @param hash QRCode hash
     * @param name unique name of QRCode
     * @param points points QRCode is worth
     * @param scannerUID name of user who first scanned the QR code
     * @param coordinates pair containing latitude and longitude where QRCode was scanned
     * @param locationImage image of location where QR code was scanned
     * @param comments list of comments associated with QR code
     * @param numPlayersScannedBy number of player who have scanned the QR code
     */
    public QRCode(String hash, String name, int points, String scannerUID, ArrayList<Double> coordinates,
                  String locationImage, ArrayList<String> comments, int numPlayersScannedBy) {
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
    ArrayList<Double> coordinates; //Need to change to Geolocation class
    String locationImage;
    ArrayList<String> comments;
    int numPlayersScannedBy;

    /**
     * Gets the hash of a QR code
     * @return Return the hash of a QR code
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the hash of a QR code
     * @param hash hash of QRCode
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Get the name of a QRCode
     * @return Return the unique name of QRCode
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of a QRCode
     * @param name unique name of QRCode
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the points a QRCode is worth
     * @return points a QRCode is worth
     */
    public int getPoints() {
        return points;
    }

    /**
     * Set the points a QRCode is worth
     * @param points points a QRCode is worth
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Get the username of the player who first scanned a QRCode
     * @return Return the username of the player who first scanned a QRCode
     */
    public String getScannerUID() {
        return scannerUID;
    }

    /**
     * Set the username of the player who first scanned a QRCode
     * @param scannerUID username of player who first scanned QRCode
     */
    public void setScannerUID(String scannerUID) {
        this.scannerUID = scannerUID;
    }

    /**
     * Get the coordinates of a QRCode
     * @return Return the coordinate pair of the QRCode
     */
    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    /**
     * Set the coordinates of a QRCode
     * @param coordinates coordinate pair of QRCode
     */
    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Get the image of the location where QR code was taken
     * @return Return the image of location where QRCode was taken
     */
    public String getLocationImage() {
        return locationImage;
    }

    /**
     * Set the image of the location where QR code was taken
     * @param locationImage image of location where QRCode was taken
     */
    public void setLocationImage(String locationImage) {
        this.locationImage = locationImage;
    }

    /**
     * Get the list of comments associated with a QR code
     * @return Return the list of comments of a QR code
     */
    public ArrayList<String> getComments() {
        return comments;
    }

    /**
     * Sets the list of comments of a QRCode
     * @param comments list of comments of QRCode
     */
    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    /**
     * Gets the number of players who have scanned a QR code
     * @return Return number of players who have scanned a QR code
     */
    public int getNumPlayersScannedBy() {
        return numPlayersScannedBy;
    }

    /**
     * Increases number of players who have scanned a QR code by 1
     */
    public void incrementNumPlayersScannedBy() {
        this.numPlayersScannedBy = numPlayersScannedBy + 1;
    }

    /**
     * Decreases number of players who have scanned a QR code by 1
     */
    public void decrementNumPlayersScannedBy() {
        this.numPlayersScannedBy = numPlayersScannedBy - 1;
    }
}
