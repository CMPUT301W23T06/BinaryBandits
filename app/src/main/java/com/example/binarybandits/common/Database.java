package com.example.binarybandits.common;

import android.content.SharedPreferences;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 *
 */
public class Database {
    private final String TAG = "Database";
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private SharedPreferences settings;

    /**
     *
     */
    public Database() {
        this.db = new DBConnector().getDB();
    }


}
