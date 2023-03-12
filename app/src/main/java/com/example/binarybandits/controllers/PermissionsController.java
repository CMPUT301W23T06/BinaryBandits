package com.example.binarybandits.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsController {

    public static boolean cameraPermissionGranted = false;
    public static boolean locationPermissionGranted = false;

    public static void askAllPermissions(Context ctx) {
        askCameraPermission(ctx);
        askLocationPermission(ctx);
    }

    public static void askCameraPermission(Context ctx) {
        if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) ctx, new String[] {android.Manifest.permission.CAMERA}, 100);
        } else {
            cameraPermissionGranted = true;
        }
    }

    public static void askLocationPermission(Context ctx) {
        if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) ctx, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        } else {
            locationPermissionGranted = true;
        }
        if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) ctx, new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            locationPermissionGranted = true;
        }
    }

}
