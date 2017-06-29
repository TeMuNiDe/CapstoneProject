package com.temunide.capstoneproject;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.temunide.capstoneproject.adapters.StoryListAdapter;
import com.temunide.capstoneproject.appwidget.LatestPostsWidget;
import com.temunide.capstoneproject.utils.PreferenceUtils;
import com.temunide.capstoneproject.utils.Story;
import com.temunide.capstoneproject.utils.Story.SimpleStory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.temunide.capstoneproject.StoryFragment.ARG_STORY;
import static com.temunide.capstoneproject.StoryListActivity.StoryListFragment.ARG_DATA_SET;
import static com.temunide.capstoneproject.utils.Story.ROOT;

public class StoryListActivity extends AppCompatActivity{
    @Nullable @BindView(R.id.story_container)
    FrameLayout storyContainer;
    @BindView(R.id.tab_pager)
    ViewPager tabPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    static {

    }
    private static boolean isTablet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        ButterKnife.bind(this);
        isTablet = storyContainer!=null;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(tabPager);
    }


    @OnClick(R.id.new_post)
        void newPost(View v){
            startActivity(new Intent(this,PostActivity.class));
        }
  public static  class StoryListFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, ChildEventListener,StoryListAdapter.OnStoryClickedListener{
      @BindView(R.id.story_list)
      RecyclerView storyList;
      FirebaseDatabase firebaseDatabase;
      StoryListAdapter storyListAdapter;
      public static final String ARG_DATA_SET = "data_set";
      public StoryListFragment(){
      }
      @Nullable
      @Override
      public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          View rootView = inflater.inflate(R.layout.fragment_story_list,container,false);
          ButterKnife.bind(this,rootView);
          storyListAdapter = new StoryListAdapter(this,new ArrayList<Story>());
          storyList.setAdapter(storyListAdapter);
          storyListAdapter.setIsBookMarksOnly(getArguments().getBoolean(ARG_DATA_SET));
          storyList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
          firebaseDatabase = FirebaseDatabase.getInstance();
          firebaseDatabase.getReference(ROOT).addChildEventListener(this);
          PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
          return rootView;
      }

      @Override
      public void onStoryClicked(Story story,TextView titleView) {
          Bundle args = new Bundle();
          args.putParcelable(ARG_STORY,story);
          if(!isTablet) {
              Intent storyActivity = new Intent(getContext(),StoryActivity.class);
              storyActivity.putExtras(args);

              Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),titleView,titleView.getTransitionName()).toBundle();

              startActivity(storyActivity,options);
          }else {
              StoryFragment storyFragment = new StoryFragment();
              storyFragment.setArguments(args);
              getActivity().getSupportFragmentManager().beginTransaction()
                      .addSharedElement(titleView,titleView.getTransitionName())
                      .replace(R.id.story_container,storyFragment).commit();
          }
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


      @Override
      public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
          if(key.equals(PreferenceUtils.BOOKMARKED_STORIES)&&getArguments().getBoolean(ARG_DATA_SET)){
              storyListAdapter.clearAll();
              firebaseDatabase.getReference(ROOT).removeEventListener(this);
              firebaseDatabase.getReference(ROOT).addChildEventListener(this);
              Log.d("changed","true");
          }
      }

      @Override
      public void onDestroyView() {
          PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);

          super.onDestroyView();
      }
  }
 private class TabAdapter extends FragmentPagerAdapter{

      TabAdapter(FragmentManager fm) {
          super(fm);
      }

      @Override
      public Fragment getItem(int position) {
          StoryListFragment storyListFragment = new StoryListFragment();
          Bundle args = new Bundle();
          args.putBoolean(ARG_DATA_SET,position==1);
          storyListFragment.setArguments(args);
          return storyListFragment;
      }

      @Override
      public int getCount() {
          return 2;
      }

      @Override
      public CharSequence getPageTitle(int position) {
          switch (position){
              case 0:return "All Stories";
              case 1:return "BookMarks";
          }
          return super.getPageTitle(position);
      }

  }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new AlertDialog.Builder(this).setMessage(R.string.log_out_prompt).setPositiveButton(R.string.prompt_yes
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        AppWidgetManager manager = AppWidgetManager.getInstance(StoryListActivity.this);
                        manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(new ComponentName(getApplicationContext(),LatestPostsWidget.class)), R.id.story_list);

                        startActivity(new Intent(StoryListActivity.this,SignInActivity.class));
                        finish();
                    }
                }
        ).show();

        return true;
    }
}
