package com.temunide.capstoneproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.temunide.capstoneproject.R;
import com.temunide.capstoneproject.utils.PreferenceUtils;


public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicViewHolder> {

    private final Context context;
    private final PreferenceUtils preferenceUtils;
    private final OnTopicClickedListener onTopicClickedListener;
    private String[] topics;

    public TopicListAdapter(String[] topics, Context context, OnTopicClickedListener onTopicClickedListener) {
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
        holder.topicView.setBackgroundResource(preferenceUtils.isSubscribedTopic(topics[position]) ? R.color.colorAccent : R.color.widgetBackground);
        holder.topicView.setTextColor(preferenceUtils.isSubscribedTopic(topics[position]) ? Color.WHITE : Color.BLACK);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return topics.length;
    }

    public interface OnTopicClickedListener {
        void onTopicClicked(String topic, int position);
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {
        final TextView topicView;

        TopicViewHolder(final TextView itemView) {
            super(itemView);
            topicView = itemView;
            topicView.setGravity(Gravity.CENTER);
            int padding = (int) itemView.getContext().getResources().getDimension(R.dimen.topic_padding);
            topicView.setPadding(padding, padding, padding, padding);
            topicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTopicClickedListener.onTopicClicked(topicView.getText().toString(), (int) itemView.getTag());
                }
            });
        }
    }
}
