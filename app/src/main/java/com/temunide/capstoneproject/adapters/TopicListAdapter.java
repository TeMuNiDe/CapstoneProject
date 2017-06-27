package com.temunide.capstoneproject.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.temunide.capstoneproject.R;
import com.temunide.capstoneproject.utils.PreferenceUtils;


public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicViewHolder> {

private String[] topics;
private Context context;
private PreferenceUtils preferenceUtils;
private OnTopicClickedListener onTopicClickedListener;

    public TopicListAdapter(String[] topics,Context context,OnTopicClickedListener onTopicClickedListener) {
        this.topics = topics;
        this.context = context;
        this.preferenceUtils = PreferenceUtils.getInstance(context);
        this.onTopicClickedListener = onTopicClickedListener;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(new TextView(context));
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        holder.topicView.setText(topics[position]);
        holder.topicView.setBackgroundResource(preferenceUtils.isSubscribedTopic(topics[position])?android.R.color.holo_blue_dark:android.R.color.darker_gray);
    }

    @Override
    public int getItemCount() {
        return topics.length;
    }
    class TopicViewHolder extends RecyclerView.ViewHolder{
        TextView topicView;

       TopicViewHolder(TextView itemView) {
            super(itemView);
            topicView = itemView;
            topicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTopicClickedListener.onTopicClicked(topicView.getText().toString());
                }
            });
        }
    }

    public interface OnTopicClickedListener{
        void onTopicClicked(String topic);
    }
}
