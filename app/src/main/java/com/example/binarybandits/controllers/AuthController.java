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
 *
 */
public class AuthController {

    static final String PREF_LOGIN_USERNAME = "login_username";
    static final String PREF_USER_LOGIN_STATUS = "login_status";
    private static PlayerDB db;

    /**
     *
     * @param ctx
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    /**
     *
     * @param ctx
     * @param username
     */
    public static void setUsername(Context ctx, String username) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGIN_USERNAME, username);
        editor.commit();
    }

    /**
     *
     * @param ctx
     * @return
     */
    public static String getUsername(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LOGIN_USERNAME, "");
    }

    /**
     *
     * @param ctx
     * @param status
     */
    public static void setUserLoggedInStatus(Context ctx, boolean status) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGIN_STATUS, status);
        editor.commit();
    }

    /**
     *
     * @param ctx
     * @return
     */
    public static boolean getUserLoggedInStatus(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGIN_STATUS, false);
    }

    /**
     *
     * @param ctx
     */
    public static void clearLoggedInEmailAddress(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGIN_USERNAME);
        editor.remove(PREF_USER_LOGIN_STATUS);
        editor.commit();
    }

    /**
     *
     * @param ctx
     * @param username
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
            }
        });
    }

    /**
     *
     * @param ctx
     */
    public static void logout(Context ctx) {
        setUserLoggedInStatus(ctx, false);
        Intent myIntent = new Intent(ctx, LogInActivity.class);
        ctx.startActivity(myIntent);
    }

    /**
     *
     * @param ctx
     * @param username
     * @param name
     * @param phone
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
