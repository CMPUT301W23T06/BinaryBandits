package com.example.binarybandits;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A connection to the FirebaseFirestore database. This class is responsible for holding the database
 * object and storing location images in the database.
 * Design Pattern: Singleton (Planned)
 */
public class DBConnector {
    private final String TAG = "DBConnector";
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private CollectionReference collectionReference;
    /**
     * Constructor for the database connector
     */
    public DBConnector() {
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();;
    }

    /**
     * Get the instance of the FirebaseFirestore database
     * @return Return the instance of the FirebaseFirestore database
     */
    public FirebaseFirestore getDB() {
        return this.db;
    }

    /**
     * Get the storage object used to store location images
     * @return Return the storage object for location images
     */
    public FirebaseStorage getStorage() {
        return this.storage;
    }

    /**
     * Get the collection reference for a given collection in the database
     * @param collection Collection name in the database
     * @return Return the collection reference for the inputted collection
     */
    public CollectionReference getCollectionReference(String collection) {
        return this.db.collection(collection);
    }
}
