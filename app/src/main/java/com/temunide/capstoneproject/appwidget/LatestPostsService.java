package com.temunide.capstoneproject.appwidget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.temunide.capstoneproject.R;
import com.temunide.capstoneproject.utils.Story;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.temunide.capstoneproject.utils.Story.ROOT;

public class LatestPostsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new LatestPostsFactory(getApplicationContext());
    }

class LatestPostsFactory implements RemoteViewsFactory,ValueEventListener{

final Context context;
ArrayList<Story> stories;

     LatestPostsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }


    @Override
    public void onDataSetChanged() {
        stories = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(ROOT).addListenerForSingleValueEvent(this);
        synchronized (this) {
            try {
                Log.d("thread", "Waiting...");
                this.wait();
                Log.d("thread", "Continue...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sortStories(stories);
    }
    private void sortStories(ArrayList<Story> stories) {
        Collections.sort(stories, new Comparator<Story>() {
            @Override
            public int compare(Story o1, Story o2) {
                return Long.compare(o2.getTimeStamp(), o1.getTimeStamp());
            }
        });
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        Log.d("thread","item_count:"+stories.size());
        return stories.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Story story = stories.get(position);
        Log.d("thread","getView:"+position+" of "+stories.size());
        RemoteViews rootViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_layout);
        rootViews.setTextViewText(R.id.story_title,story.getTitle());
        rootViews.setTextViewText(R.id.story_author,story.getAuthor());
        rootViews.setOnClickFillInIntent(R.id.widget_list_layout,new Intent());
        return rootViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.d("thread","itemTypecount");
        return 1;
    }

    @Override
    public long getItemId(int position) {
        Log.d("thread","getItemID");
        return position;
    }

    @Override
    public boolean hasStableIds() {
        Log.d("thread","has_stableID");
        return true;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
            stories.add(new Story.Builder().build(dataSnapshot1.getValue(Story.SimpleStory.class)));
            Log.d("thread","Added");
        }
        Log.d("thread","notified");
        synchronized (this) {
            this.notify();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("thread","error");
        synchronized (this) {
            this.notify();
        }
    }


}


}
