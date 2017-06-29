package com.temunide.capstoneproject.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Pair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by varma on 25-06-2017.
 */

public class Story implements Parcelable {
    private String title;
    private String story;
    private String author;
    private String topics;
    private long timeStamp;
    private String id;
    public static final String ROOT  = "stories";
    private static final int STORY_NULL = -1;
    public static final int STORY_VALID = 0;
    public static final int IN_VALID_TITLE = 1;
    public static final int IN_VALID_CONTENT = 2;
    public static final int TOPICS_NULL = 3;
    public static final int IN_VALID_TOPICS = 4;

    public static class Builder{
        private String title;
        private String story;
        private String topics;
       public Builder addTitle(String title){
            this.title = title;
           return this;
        }
        public Builder addStory(String story){
            this.story = story;
            return this;
        }

        public Builder addTopics(String topics){
            this.topics = topics;
            return this;
        }
        public Story build(SimpleStory simpleStory){
            return new Story(simpleStory.getTitle(),simpleStory.getStory(),simpleStory.getAuthor(),simpleStory.getTopics(),simpleStory.getTimeStamp(),simpleStory.getId());
        }



       public Story build(){
           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
           if(user==null){
               return null;
           }
           Pair<Long,String> pair = generateID(user);
           String author = user.getDisplayName();
           return new Story(title,story,author,topics,pair.first,pair.second);
        }

    }
    private Story(String title, String story, String author, String topics, long timeStamp,String id) {
        this.title = title;
        this.story = story;
        this.author = author;
        this.topics = topics;
        this.timeStamp = timeStamp;
        this.id = id;
    }
    public static int validateStory(Story story){
        if(story==null){
            return STORY_NULL;
        }if(story.getTitle()==null||TextUtils.isEmpty(story.getTitle())){
            return IN_VALID_TITLE;
        } if(story.getStory()==null||TextUtils.isEmpty(story.getStory())){
            return IN_VALID_CONTENT;
        } if(story.getTopics()==null||TextUtils.isEmpty(story.getTopics())){
            return TOPICS_NULL;
        }if((story.getTopics().split(",").length!=story.getTopics().length()-story.getTopics().replace(",","").length()+1)||story.getTopics().split(",").length>3||story.getTopics().contains(",,")){
            return IN_VALID_TOPICS;
        }
        return STORY_VALID;
    }
    private static Pair<Long,String> generateID(FirebaseUser user){
        long stamp = System.currentTimeMillis();
        return new Pair<>(stamp, String.valueOf(stamp) + user.getUid());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public  String getStory() {
        return story;
    }

    public  void setStory(String story) {
        this.story = story;
    }

    public  String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public  String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


   private Story(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.story = in.readString();
        this.author = in.readString();
        this.topics = in.readString();
        this.timeStamp = in.readLong();
    }


    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(story);
        dest.writeString(author);
        dest.writeString(topics);
        dest.writeLong(timeStamp);
    }


   public SimpleStory getSimpleStory(){
       SimpleStory simpleStory = new SimpleStory();
       simpleStory.setStory(story);
       simpleStory.setTitle(title);
       simpleStory.setAuthor(author);
       simpleStory.setId(id);
       simpleStory.setTopics(topics);
       simpleStory.setTimeStamp(timeStamp);
       return simpleStory;
   }

    public static class SimpleStory{
        private String title;
        private String story;
        private String author;
        private String topics;
        private long timeStamp;
        private String id;

        public SimpleStory(){}
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStory() {
            return story;
        }

        public void setStory(String story) {
            this.story = story;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTopics() {
            return topics;
        }

        public void setTopics(String topics) {
            this.topics = topics;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Story){
         return ((Story)obj).getId().equals(this.getId());
        }else {
            return super.equals(obj);
        }
    }
}
