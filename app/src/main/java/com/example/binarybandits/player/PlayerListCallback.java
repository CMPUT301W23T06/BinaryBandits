package com.example.binarybandits.player;

import com.example.binarybandits.models.Player;

import java.util.ArrayList;

/**
 * An interface containing a callback function used to retrieve a list of Players from the database.
 */
public interface PlayerListCallback {
    void onPlayerListCallback(ArrayList<Player> playerList);
}
