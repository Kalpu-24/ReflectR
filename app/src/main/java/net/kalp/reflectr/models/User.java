package net.kalp.reflectr.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class User{
    private String username;
    private boolean is_profile_completed;
    private String email;
    private String phone;
    private String profile_pic;
    private String bio;
    private List<Topics> topics;
    @ServerTimestamp
    private String created_at;
    @ServerTimestamp
    private String updated_at;

    public User(String name, boolean isProfileCompleted, String email, String phone, String profile_pic, String bio) {
        this.username = name;
        is_profile_completed = isProfileCompleted;
        this.email = email;
        this.phone = phone;
        this.profile_pic = profile_pic;
        this.bio = bio;
    }

    public User(String name, boolean isProfileCompleted, String email, String phone, String profile_pic, String bio, List<Topics> topics) {
        this.username = name;
        is_profile_completed = isProfileCompleted;
        this.email = email;
        this.phone = phone;
        this.profile_pic = profile_pic;
        this.bio = bio;
        this.topics = topics;
    }

    public User(boolean isProfileCompleted,String phone) {
        this.is_profile_completed = isProfileCompleted;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isIs_profile_completed() {
        return is_profile_completed;
    }

    public void setIs_profile_completed(boolean is_profile_completed) {
        this.is_profile_completed = is_profile_completed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Topics> getTopics() {
        return topics;
    }

    public void setTopics(List<Topics> topics) {
        this.topics = topics;
    }

    @NonNull
    @Override
    public String toString(){
        return "User{" +
                "name='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                ", bio='" + bio + '\'' +
                ", topics=" + topics +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

}
