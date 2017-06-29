package com.temunide.capstoneproject.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreferenceUtils {
    public static final String BOOKMARKED_STORIES = "bookmarked_stories";
    private static final String SUBSCRIBED_TOPICS = "subscribed_topics";
    private final SharedPreferences preferences;

    private PreferenceUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtils getInstance(Context context) {
        return new PreferenceUtils(context);
    }

    public boolean isSubscribedTopic(String topic) {
        String topics = preferences.getString(SUBSCRIBED_TOPICS, " ");
        return Arrays.asList(topics.split(",")).contains(topic);
    }

    public boolean isBookMarkedStory(Story story) {
        String bookmarks = preferences.getString(BOOKMARKED_STORIES, " ");
        return Arrays.asList(bookmarks.split(",")).contains(story.getId());
    }

    public void subscribeToTopic(String topic) {

        List<String> topics = new ArrayList<>(Arrays.asList(preferences.getString(SUBSCRIBED_TOPICS, " ").split(",")));
        topics.add(topic);
        String newTopics = TextUtils.join(",", topics);
        preferences.edit().putString(SUBSCRIBED_TOPICS, newTopics).apply();
    }

    public void unSubscribeTopic(String topic) {
        List<String> topics = new ArrayList<>(Arrays.asList(preferences.getString(SUBSCRIBED_TOPICS, " ").split(",")));
        topics.remove(topic);
        String newTopics = TextUtils.join(",", topics);
        preferences.edit().putString(SUBSCRIBED_TOPICS, newTopics).apply();
    }

    public void addBookMark(Story story) {
        List<String> bookmarks = new ArrayList<>(Arrays.asList(preferences.getString(BOOKMARKED_STORIES, " ").split(",")));
        bookmarks.add(story.getId());
        String newBookmarks = TextUtils.join(",", bookmarks);
        preferences.edit().putString(BOOKMARKED_STORIES, newBookmarks).apply();
    }

    public void removeBookmark(Story story) {
        List<String> bookmarks = new ArrayList<>(Arrays.asList(preferences.getString(BOOKMARKED_STORIES, " ").split(",")));
        bookmarks.remove(story.getId());
        String newBookmarks = TextUtils.join(",", bookmarks);
        preferences.edit().putString(BOOKMARKED_STORIES, newBookmarks).apply();
    }


}
