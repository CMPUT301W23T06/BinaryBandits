package com.example.binarybandits;

/**
 * Interface containing a callback method used to retrieve a player's
 * highest/lowest QR code score from the database
 */
public interface ScoreCallback {
    void scoreCallback(int score);
}
