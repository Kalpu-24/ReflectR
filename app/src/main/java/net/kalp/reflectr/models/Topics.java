package net.kalp.reflectr.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class Topics {
    private String topic;
    private String owner_uid;
    private boolean is_private;
    private List<Journal> journals;
    @ServerTimestamp
    private String created_at;
    @ServerTimestamp
    private String updated_at;

    public Topics() {
    }

    public Topics(String topic, String owner_uid, boolean is_private) {
        this.topic = topic;
        this.owner_uid = owner_uid;
        this.is_private = is_private;
    }

    public Topics(String topic, String owner_uid, boolean is_private, List<Journal> journals) {
        this.topic = topic;
        this.owner_uid = owner_uid;
        this.is_private = is_private;
        this.journals = journals;
    }

    public String getTopic() {
        return topic;
    }

    public String getOwner_uid() {
        return owner_uid;
    }

    public boolean isIs_private() {
        return is_private;
    }

    public List<Journal> getJournals() {
        return journals;
    }

    public void setJournals(List<Journal> journals) {
        this.journals = journals;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setOwner_uid(String owner_uid) {
        this.owner_uid = owner_uid;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    @NonNull
    @Override
    public String toString() {
        return "Topics{" +
                "topic='" + topic + '\'' +
                ", owner_uid='" + owner_uid + '\'' +
                ", is_private=" + is_private +
                ", journals=" + journals +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

}
