package com.temunide.capstoneproject.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.temunide.capstoneproject.R;
import com.temunide.capstoneproject.appwidget.LatestPostsWidget;

import java.util.Map;

import static com.temunide.capstoneproject.utils.Story.ROOT;

public class FirebaseMessageHandler extends FirebaseMessagingService implements ValueEventListener {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();

        FirebaseDatabase.getInstance().getReference(ROOT).child(data.get("id")).addListenerForSingleValueEvent(this);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(new ComponentName(getApplicationContext(), LatestPostsWidget.class)), R.id.story_list);

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        NotificationUtils.notify(this, new Story.Builder().build(dataSnapshot.getValue(Story.SimpleStory.class)), 0);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
