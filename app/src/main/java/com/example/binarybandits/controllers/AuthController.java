package com.example.binarybandits.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.binarybandits.MainActivity;
import com.example.binarybandits.ui.auth.LogInActivity;

public class AuthController {

    static final String PREF_LOGIN_USERNAME = "login_username";
    static final String PREF_USER_LOGIN_STATUS = "login_status";

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUsername(Context ctx, String username) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGIN_USERNAME, username);
        editor.commit();
    }

    public static String getUsername(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LOGIN_USERNAME, "");
    }

    public static void setUserLoggedInStatus(Context ctx, boolean status) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGIN_STATUS, status);
        editor.commit();
    }

    public static boolean getUserLoggedInStatus(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGIN_STATUS, false);
    }

    public static void clearLoggedInEmailAddress(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGIN_USERNAME);
        editor.remove(PREF_USER_LOGIN_STATUS);
        editor.commit();
    }

    public static void login(Context ctx, String username) {
        // assumes username is not null or empty
        setUserLoggedInStatus(ctx, true);
        setUsername(ctx, username);
        Intent myIntent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(myIntent);
    }

    public static void logout(Context ctx) {
        setUserLoggedInStatus(ctx, false);
        Intent myIntent = new Intent(ctx, LogInActivity.class);
        ctx.startActivity(myIntent);
    }

    public static void register(Context ctx, String username, String name, String phone) {
        setUserLoggedInStatus(ctx, true);
        setUsername(ctx, username);
        Intent myIntent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(myIntent);
    }

}
