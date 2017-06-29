package com.temunide.capstoneproject;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.temunide.capstoneproject.utils.Story;
import com.temunide.capstoneproject.utils.Story.SimpleStory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.temunide.capstoneproject.utils.Story.IN_VALID_CONTENT;
import static com.temunide.capstoneproject.utils.Story.IN_VALID_TITLE;
import static com.temunide.capstoneproject.utils.Story.IN_VALID_TOPICS;
import static com.temunide.capstoneproject.utils.Story.TOPICS_NULL;

public class PostActivity extends AppCompatActivity {
    @BindView(R.id.story_title)
    EditText storyTitle;
    @BindView(R.id.story_content)
    EditText storyContent;
    @BindView(R.id.story_topics)
    EditText storyTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.story_post)
    void validateAndPostStory(View v) {
        Log.d("clicked", "true");
        Story story = new Story.Builder().addTitle(storyTitle.getText().toString()).addStory(storyContent.getText().toString()).addTopics(storyTopics.getText().toString()).build();
        if (Story.validateStory(story) == Story.STORY_VALID) {
            publishStory(story);

        } else {
            handleErrorStory(Story.validateStory(story));
        }
    }

    private void handleErrorStory(int errorCode) {
        Log.d("story_status", "error :" + errorCode);
        String errorMessage = "Error";
        switch (errorCode) {
            case IN_VALID_TITLE:
                errorMessage = getResources().getString(R.string.error_title);
                break;
            case IN_VALID_CONTENT:
                errorMessage = getResources().getString(R.string.error_content);
                break;
            case IN_VALID_TOPICS:
                errorMessage = getResources().getString(R.string.error_topics);
                break;
            case TOPICS_NULL:
                errorMessage = getResources().getString(R.string.error_topic);
                break;
        }
        Snackbar.make(findViewById(R.id.activity_post), errorMessage, Snackbar.LENGTH_LONG).show();


    }

    private void publishStory(Story story) {
        SimpleStory simpleStory = story.getSimpleStory();
        Log.d("story_status", "published");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("stories").child(story.getId()).setValue(simpleStory, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d("story_status", databaseError.getMessage());

                } else {
                    Log.d("story_status", "No ERROR");
                }
            }
        });
    }

}
