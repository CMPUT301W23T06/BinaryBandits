package com.example.binarybandits.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.binarybandits.MainActivity;
import com.example.binarybandits.DBConnector;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.ui.auth.LogInActivity;

/**
 * Controller for authenticating the user's input on Login/Signup page.
 * Outstanding issues: N/A
 */
public class AuthController {

    static final String PREF_LOGIN_USERNAME = "login_username";
    static final String PREF_USER_LOGIN_STATUS = "login_status";
    private static PlayerDB db;

    /**
     * Get the SharedPreferences saved to a user's device
     * @param ctx context of the function call
     * @return Return the SharedPreferences saved to the user's device
     */
    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    /**
     * Set the username of the Player using the application
     * @param ctx context of the function call
     * @param username username to store in SharedPreferences
     */
    public static void setUsername(Context ctx, String username) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGIN_USERNAME, username);
        editor.apply();
    }

    /**
     * Get the username of the Player using the application
     * @param ctx context of function call
     * @return Return the username of the Player using the application
     */
    public static String getUsername(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LOGIN_USERNAME, "");
    }

    /**
     * Set the user's log in status
     * @param ctx context of function call
     * @param status true if user is online, false otherwise
     */
    public static void setUserLoggedInStatus(Context ctx, boolean status) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGIN_STATUS, status);
        editor.apply();
    }

    /**
     * Get the user's log in status
     * @param ctx context of function call
     * @return Return the user's log in status
     */
    public static boolean getUserLoggedInStatus(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGIN_STATUS, false);
    }

    /**
     * Attempt to login the user based on inputted username. If a Player with the requested
     * username is not found in the database, show a message and prompt the user to enter
     * a different username. Otherwise, go to home page.
     * @param ctx context of function call
     * @param username username of Player to find in database
     */
    public static void login(Context ctx, String username) {
        // assumes username is not null or empty
        setUserLoggedInStatus(ctx, true);
        setUsername(ctx, username);
        PlayerDB db = new PlayerDB(new DBConnector());
        db.getPlayer(username, new PlayerCallback() {
            @Override
            public void onPlayerCallback(Player player) {
                if (player != null) {
                    Intent myIntent = new Intent(ctx, MainActivity.class);
                    ctx.startActivity(myIntent);
                }
                else {
                    //Need to move Toast to a View class
                    Toast message = Toast.makeText(ctx, "Player not found!", Toast.LENGTH_LONG);
                    message.show();
                }
            }
        });
    }

    /**
     * Register the user in the database. If a Player in the database has a user's requested username,
     * show a message and do not add Player to database.
     * @param ctx context of the function call
     * @param username player's username
     * @param name player's full name
     * @param phone player's phone number
     */
    public static void register(Context ctx, String username, String name, String phone) {
        setUserLoggedInStatus(ctx, true);
        setUsername(ctx, username);
        Player playerToAdd = new Player(username, phone);
        db = new PlayerDB(new DBConnector());
        db.getPlayer(username, new PlayerCallback() {
            @Override
            public void onPlayerCallback(Player player) {
                if (player == null) {
                    db.addPlayer(playerToAdd);
                    Intent myIntent = new Intent(ctx, MainActivity.class);
                    ctx.startActivity(myIntent);
                }
                else {
                    //Need to move Toast to a View class
                    Toast message = Toast.makeText(ctx, "Username is taken!", Toast.LENGTH_LONG);
                    message.show();
                }
            }
        });
    }

}
