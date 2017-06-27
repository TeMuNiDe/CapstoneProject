package com.temunide.capstoneproject;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.temunide.capstoneproject.utils.Story.SimpleStory;
import com.temunide.capstoneproject.utils.Story;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends AppCompatActivity {
@BindView(R.id.story_title)EditText storyTitle;
@BindView(R.id.story_content)EditText storyContent;
@BindView(R.id.story_topics)EditText storyTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.story_post)
    void validateAndPostStory(View v){
        Log.d("clicked","true");
        Story story = new Story.Builder().addTitle(storyTitle.getText().toString()).addStory(storyContent.getText().toString()).addTopics(storyTopics.getText().toString()).build();
        if(Story.validateStory(story)==Story.STORY_VALID){
            publishStory(story);

        }else {
            handleErrorStory(Story.validateStory(story));
        }
    }
    void handleErrorStory(int errorCode){
        Log.d("story_status","error :"+errorCode);
        Snackbar.make(findViewById(R.id.activity_post),"Error "+errorCode,Snackbar.LENGTH_LONG).show();


    }
   void publishStory(Story story){
       SimpleStory simpleStory = story.getSimpleStory();
       Log.d("story_status","published");
       FirebaseDatabase database = FirebaseDatabase.getInstance();
       database.getReference("stories").child(story.getId()).setValue(simpleStory, new DatabaseReference.CompletionListener() {
           @Override
           public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
               if(databaseError!=null){
                   Log.d("story_status",databaseError.getMessage());

               }else{
                   Log.d("story_status","No ERROR");
               }
           }
       });
   }

}
