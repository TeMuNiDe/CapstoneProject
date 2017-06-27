package com.temunide.capstoneproject.utils;

import android.app.Service;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by varma on 27-06-2017.
 */

public class FirebaseInstanceService extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh(){
        super.onTokenRefresh();
        FirebaseMessaging.getInstance().subscribeToTopic("default");
    }
}
