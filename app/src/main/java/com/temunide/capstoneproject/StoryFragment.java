package com.temunide.capstoneproject;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.temunide.capstoneproject.adapters.TopicListAdapter;
import com.temunide.capstoneproject.utils.PreferenceUtils;
import com.temunide.capstoneproject.utils.Story;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.temunide.capstoneproject.utils.Story.ROOT;

public class StoryFragment extends Fragment implements TopicListAdapter.OnTopicClickedListener {

    public static final String ARG_STORY = "story_argument";

    @BindView(R.id.story_title)TextView storyTitle;
    @BindView(R.id.story_content)TextView storyContent;
    @BindView(R.id.story_topics)RecyclerView topics;
    @BindView(R.id.book_mark)CheckBox bookMark;
    PreferenceUtils preferenceUtils;
    TopicListAdapter adapter;
    AlertDialog.Builder dialogBuilder;
    Story story;
    public StoryFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View storyView = inflater.inflate(R.layout.fragment_story, container, false);
        ButterKnife.bind(this,storyView);
        Bundle args = getArguments();
        if(args==null){
            args = getActivity().getIntent().getExtras();
        }
        story = args.getParcelable(ARG_STORY);
        if (story == null) {
            throw new IllegalArgumentException(getContext().getResources().getString(R.string.illegal_argument_exception,ARG_STORY));
        }
        dialogBuilder = new AlertDialog.Builder(getContext());
        preferenceUtils = PreferenceUtils.getInstance(getContext());
        bookMark.setChecked(preferenceUtils.isBookMarkedStory(story));
        storyTitle.setText(story.getTitle());
        storyContent.setText(story.getStory());
        adapter = new TopicListAdapter(story.getTopics().split(","),getContext(),this);
        topics.setAdapter(adapter);
        topics.setLayoutManager(new GridLayoutManager(getContext(),4){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        return storyView;

    }

    @Override
    public void onTopicClicked(String topic) {
        if(preferenceUtils.isSubscribedTopic(topic)){
         promptAndUnSubscribeIfYes(topic);
        }else {
            promptAndSubscribeIfYes(topic);
        }
    }

    @OnClick(R.id.book_mark)
    void toggleBookMark(CheckBox bookMark){
        if(bookMark.isChecked()){
           promptAndAddBookMark(story);
           bookMark.setChecked(false);
        }else {
           promptAndRemoveBookMark(story);
            bookMark.setChecked(true);
        }
    }

    void promptAndAddBookMark(final Story story){
        //// FIXME: 25-06-2017 add code to add BookMark and save Offline;
        dialogBuilder.setMessage(getContext().getResources().getString(R.string.book_mark_prompt,"add"));
        dialogBuilder.setPositiveButton(R.string.prompt_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               preferenceUtils.addBookMark(story);
                FirebaseDatabase.getInstance().getReference(ROOT).child(story.getId()).keepSynced(true);
                bookMark.setChecked(true);
            }
        });
        dialogBuilder.show();
    }

    void promptAndRemoveBookMark(final Story story){
        //// FIXME: 25-06-2017 add code to remove BookMark
        dialogBuilder.setMessage(getContext().getResources().getString(R.string.book_mark_prompt,"remove"));
        dialogBuilder.setPositiveButton(R.string.prompt_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                preferenceUtils.removeBookmark(story);
                FirebaseDatabase.getInstance().getReference(ROOT).child(story.getId()).keepSynced(false);
                bookMark.setChecked(false);
            }
        });
        dialogBuilder.show();
    }


    void promptAndSubscribeIfYes(String topic){
        //// FIXME: 25-06-2017  add code to prompt and Subscribe
    }
    void promptAndUnSubscribeIfYes(String topic){
        //// FIXME: 25-06-2017 add code to prompt and Un Subscribe
    }
}