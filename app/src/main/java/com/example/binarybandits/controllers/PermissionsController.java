package com.example.binarybandits.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsController {

    public static void askAllPermissions(Context ctx) {
        askCameraPermission(ctx);
    }

    public static void askCameraPermission(Context ctx) {
        if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity) ctx, new String[] {android.Manifest.permission.CAMERA}, 100);
        }
    }

}
