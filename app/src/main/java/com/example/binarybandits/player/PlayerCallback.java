package com.example.binarybandits.player;

import com.example.binarybandits.models.Player;

import java.util.ArrayList;

/**
 * An interface containing a callback function used to retrieve a Player from the database.
 */
public interface PlayerCallback {
    void onPlayerCallback(Player player);
}
