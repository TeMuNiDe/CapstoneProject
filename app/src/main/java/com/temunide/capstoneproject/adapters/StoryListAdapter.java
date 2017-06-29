package com.temunide.capstoneproject.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.temunide.capstoneproject.R;
import com.temunide.capstoneproject.utils.PreferenceUtils;
import com.temunide.capstoneproject.utils.Story;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StoryListAdapter extends RecyclerView.Adapter<StoryListAdapter.StoryListItemHolder> {
    private final Context context;
    private final OnStoryClickedListener onStoryClickedListener;
    private final ArrayList<Story> stories;
    private final PreferenceUtils preferenceUtils;
    private boolean isBookMarksOnly = false;


    public StoryListAdapter(Fragment context, ArrayList<Story> stories) {
        this.context = context.getContext();
        this.onStoryClickedListener = (OnStoryClickedListener) context;
        this.stories = stories;
        this.preferenceUtils = PreferenceUtils.getInstance(context.getContext());
    }

    public void setIsBookMarksOnly(boolean isBookMarksOnly) {
        this.isBookMarksOnly = isBookMarksOnly;
    }

    public void addStory(Story story) {
        if (isBookMarksOnly && !preferenceUtils.isBookMarkedStory(story))
            return;
        this.stories.add(story);
        sortStories(this.stories);
        notifyDataSetChanged();
    }

    public void removeStory(Story story) {
        this.stories.remove(story);
        sortStories(this.stories);
        notifyDataSetChanged();
    }

    public void clearAll() {
        this.stories.clear();
        notifyDataSetChanged();
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
    public StoryListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoryListItemHolder(LayoutInflater.from(context).inflate(R.layout.story_item, parent, false));
    }

    @Override
    public void onBindViewHolder(StoryListItemHolder holder, int position) {
        holder.bindStory(stories.get(position));
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public interface OnStoryClickedListener {
        void onStoryClicked(Story story, TextView item);
    }

    class StoryListItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.story_title)
        TextView storyTitle;
        @BindView(R.id.story_author)
        TextView storyAuthor;
        @BindView(R.id.story_topics)
        TextView storyTopics;
        @BindView(R.id.story_date)
        TextView storyDate;

        StoryListItemHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("item", storyTitle.getText().toString() + "::" + storyTitle.getTransitionName());
                    onStoryClickedListener.onStoryClicked((Story) itemView.getTag(), storyTitle);
                }
            });
        }

        void bindStory(Story story) {
            itemView.setTag(story);
            storyTitle.setText(story.getTitle());
            storyAuthor.setText(story.getAuthor());
            storyTopics.setText(story.getTopics());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(story.getTimeStamp());
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
            storyDate.setText(date);
        }


    }
}
