package com.temunide.capstoneproject.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.temunide.capstoneproject.R;
import com.temunide.capstoneproject.utils.Story;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StoryListAdapter extends RecyclerView.Adapter<StoryListAdapter.StoryListItemHolder>{
private Context context;
private OnStoryClickedListener onStoryClickedListener;
private ArrayList<Story> stories;

    public StoryListAdapter(Activity context, ArrayList<Story> stories) {
        this.context = context;
        this.onStoryClickedListener = (OnStoryClickedListener) context;
        this.stories = stories;
    }
public void addStory(Story story){
    this.stories.add(story);
    sortStories(this.stories);
    notifyDataSetChanged();
}
public void removeStory(Story story){
    Log.d("removed : "," "+this.stories.indexOf(story));
    this.stories.remove(story);
    sortStories(this.stories);
    notifyDataSetChanged();
}

private void sortStories(ArrayList<Story> stories){
    Collections.sort(stories, new Comparator<Story>() {
        @Override
        public int compare(Story o1, Story o2) {
            return Long.compare(o2.getTimeStamp(),o1.getTimeStamp());
        }
    });
}




    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }

    @Override
    public StoryListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoryListItemHolder(LayoutInflater.from(context).inflate(R.layout.story_item,parent,false));
    }

    @Override
    public void onBindViewHolder(StoryListItemHolder holder, int position) {
        holder.bindStory(stories.get(position));
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    class StoryListItemHolder extends RecyclerView.ViewHolder {
     @BindView(R.id.story_title)TextView storyTitle;
     @BindView(R.id.story_author)TextView storyAuthor;
     @BindView(R.id.story_topics)TextView storyTopics;
     @BindView(R.id.story_date)TextView storyDate;

    public StoryListItemHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStoryClickedListener.onStoryClicked((Story)itemView.getTag());
            }
        });
    }
    void bindStory(Story story){
        itemView.setTag(story);
        storyTitle.setText(story.getTitle());
        storyAuthor.setText(story.getAuthor());
        storyTopics.setText(story.getTopics());
        storyDate.setText(String.valueOf(story.getTimeStamp()));
        // FIXME: 25-06-2017 change the code to display formatted date instead of timestamp
    }



}



public interface OnStoryClickedListener{
    void onStoryClicked(Story story);
}
}
