package com.temunide.capstoneproject;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.temunide.capstoneproject.adapters.StoryListAdapter;
import com.temunide.capstoneproject.utils.Story.SimpleStory;
import com.temunide.capstoneproject.utils.Story;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.temunide.capstoneproject.StoryFragment.ARG_STORY;

public class StoryListActivity extends AppCompatActivity implements StoryListAdapter.OnStoryClickedListener,ChildEventListener {
    @BindView(R.id.story_list)
    RecyclerView storyList;
    @Nullable @BindView(R.id.story_container)
    FrameLayout storyContainer;
    FirebaseDatabase firebaseDatabase;
    StoryListAdapter storyListAdapter;
    static {
       //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    static boolean isTablet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        ButterKnife.bind(this);
        isTablet = storyContainer!=null;
        storyListAdapter = new StoryListAdapter(this,new ArrayList<Story>());
        storyList.setAdapter(storyListAdapter);
        storyList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        firebaseDatabase =   FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("stories").addChildEventListener(this);
        firebaseDatabase.getReference("stories").keepSynced(true);
    }
    @Override
    public void onStoryClicked(Story story) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_STORY,story);
        if(!isTablet) {
            Intent storyActivity = new Intent(this,StoryActivity.class);
            storyActivity.putExtras(args);
            startActivity(storyActivity);
        }else {
            StoryFragment storyFragment = new StoryFragment();
            storyFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.story_container,storyFragment).commit();
        }
    }

    @OnClick(R.id.new_post)
        void newPost(View v){
            startActivity(new Intent(this,PostActivity.class));
        }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        storyListAdapter.addStory(new Story.Builder().build(dataSnapshot.getValue(SimpleStory.class)));
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        storyListAdapter.removeStory(new Story.Builder().build(dataSnapshot.getValue(SimpleStory.class)));
        storyListAdapter.addStory(new Story.Builder().build(dataSnapshot.getValue(SimpleStory.class)));
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.d("child","removed");
        storyListAdapter.removeStory(new Story.Builder().build(dataSnapshot.getValue(SimpleStory.class)));
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
