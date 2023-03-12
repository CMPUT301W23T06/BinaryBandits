package com.example.binarybandits;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

/**
 *
 */
public class DBConnector {
    private final String TAG = "DBConnector";
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private CollectionReference collectionReference;
    /**
     *
     */
    public DBConnector() {
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();;
    }

    /**
     *
     * @return
     */
    public FirebaseFirestore getDB() {
        return this.db;
    }

    public FirebaseStorage getStorage() {
        return this.storage;
    }

    public CollectionReference getCollectionReference(String collection) {
        return this.db.collection(collection);
    }
}
