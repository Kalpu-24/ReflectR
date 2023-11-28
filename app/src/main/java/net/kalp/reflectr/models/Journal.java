package net.kalp.reflectr.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Journal {
    private String title;
    private String content;
    private String date;
    private String time;
    private Mood mood;
    private String owner_uid;
    private boolean is_private;
    private boolean is_media_present;
    private Media media;
    private List<String> emotion_tags;
    private String category;
    private @ServerTimestamp Date created_at;
    private @ServerTimestamp Date updated_at;

    public Journal() {
    }

    public Journal(String title, String content, String date, String time, Mood mood, String owner_uid, boolean is_private, boolean is_media_present, List<String> emotion_tags, String category) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.mood = mood;
        this.owner_uid = owner_uid;
        this.is_private = is_private;
        this.is_media_present = is_media_present;
        this.emotion_tags = emotion_tags;
        this.category = category;
    }

    public Journal(String title, String content, String date, String time, Mood mood, String owner_uid, boolean is_private, boolean is_media_present, Media media, List<String> emotion_tags, String category) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.mood = mood;
        this.owner_uid = owner_uid;
        this.is_private = is_private;
        this.is_media_present = is_media_present;
        this.media = media;
        this.emotion_tags = emotion_tags;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public String getOwner_uid() {
        return owner_uid;
    }

    public void setOwner_uid(String owner_uid) {
        this.owner_uid = owner_uid;
    }

    public boolean isIs_private() {
        return is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    public boolean isIs_media_present() {
        return is_media_present;
    }

    public void setIs_media_present(boolean is_media_present) {
        this.is_media_present = is_media_present;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public List<String> getEmotion_tags() {
        return emotion_tags;
    }

    public void setEmotion_tags(List<String> emotion_tags) {
        this.emotion_tags = emotion_tags;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @NonNull
    @Override
    public String toString(){
        return "Journal{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", mood='" + mood + '\'' +
                ", owner_uid='" + owner_uid + '\'' +
                ", is_private=" + is_private +
                ", is_media_present=" + is_media_present +
                ", media=" + media +
                ", emotion_tags=" + emotion_tags +
                ", category='" + category + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
    public static class Builder {
        private String title;
        private String content;
        private String date;
        private String time;
        private Mood mood;
        private String weather;
        private String location;
        private String owner_uid;
        private boolean is_private;
        private boolean is_media_present;
        private Media media;
        private List<String> emotion_tags;
        private String category;
        private @ServerTimestamp Date created_at;
        private @ServerTimestamp Date updated_at;

        public Builder() {
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        public Builder setTime(String time) {
            this.time = time;
            return this;
        }

        public Builder setMood(Mood mood) {
            this.mood = mood;
            return this;
        }

        public Builder setOwner_uid(String owner_uid) {
            this.owner_uid = owner_uid;
            return this;
        }

        public Builder setIs_private(boolean is_private) {
            this.is_private = is_private;
            return this;
        }

        public Builder setIs_media_present(boolean is_media_present) {
            this.is_media_present = is_media_present;
            return this;
        }

        public Builder setMedia(Media media) {
            this.media = media;
            return this;
        }

        public Builder setEmotion_tags(List<String> emotion_tags) {
            this.emotion_tags = emotion_tags;
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder setCreated_at(Date created_at) {
            this.created_at = created_at;
            return this;
        }

        public Builder setUpdated_at(Date updated_at) {
            this.updated_at = updated_at;
            return this;
        }

        public Journal build() {
            return new Journal(title, content, date, time, mood, owner_uid, is_private, is_media_present, media, emotion_tags, category);
        }
    }
}
