package net.kalp.reflectr.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Journal {
    private String title;
    private String content;
    private Date date;
    private Time time;
    private String mood;
    private String weather;
    private String location;
    private String topic;
    private String owner_uid;
    private boolean is_private;
    private boolean is_media_present;
    private Media media;
    private List<String> emotion_tags;
    @ServerTimestamp
    private String created_at;
    @ServerTimestamp
    private String updated_at;

    public Journal() {
    }

    public Journal(String title, String content, Date date, Time time, String mood, String weather, String location, String topic, String owner_uid, boolean is_private, boolean is_media_present, List<String> emotion_tags) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.mood = mood;
        this.weather = weather;
        this.location = location;
        this.topic = topic;
        this.owner_uid = owner_uid;
        this.is_private = is_private;
        this.is_media_present = is_media_present;
        this.emotion_tags = emotion_tags;
    }

    public Journal(String title, String content, Date date, Time time, String mood, String weather, String location, String topic, String owner_uid, boolean is_private, boolean is_media_present, Media media, List<String> emotion_tags) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.mood = mood;
        this.weather = weather;
        this.location = location;
        this.topic = topic;
        this.owner_uid = owner_uid;
        this.is_private = is_private;
        this.is_media_present = is_media_present;
        this.media = media;
        this.emotion_tags = emotion_tags;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    @NonNull
    @Override
    public String toString(){
        return "Journal{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", mood='" + mood + '\'' +
                ", weather='" + weather + '\'' +
                ", location='" + location + '\'' +
                ", topic='" + topic + '\'' +
                ", owner_uid='" + owner_uid + '\'' +
                ", is_private=" + is_private +
                ", is_media_present=" + is_media_present +
                ", media=" + media +
                ", emotion_tags=" + emotion_tags +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
