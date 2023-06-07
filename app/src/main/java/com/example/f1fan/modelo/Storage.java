package com.example.f1fan.modelo;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Storage {
    private final static FirebaseStorage storage = FirebaseStorage.getInstance("gs://f1fan-b7d7b.appspot.com");
    private final static StorageReference storageRef = storage.getReference();

    public StorageReference getReference() {
        return storageRef;
    }
}
