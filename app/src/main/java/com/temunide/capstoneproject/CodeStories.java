package com.temunide.capstoneproject;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class CodeStories extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().setPersistenceCacheSizeBytes(2000000);
    }
}
