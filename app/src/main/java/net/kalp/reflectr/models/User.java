package net.kalp.reflectr.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Objects;


public class User{
    private String username;
    private boolean is_profile_completed;
    private String email;
    private Integer age;
    private String phone;
    private String profile_pic;
    private String bio;
    private String City;
    private String Country;
    private List<Topics> topics;
    private @ServerTimestamp Date created_at;
    private @ServerTimestamp Date updated_at;

    public User(String name,Boolean is_profile_completed, String email, Integer age, String phone, String profile_pic, String bio, String city, String country) {
        this.username = name;
        this.is_profile_completed = is_profile_completed;
        this.email = email;
        this.age = age;
        this.phone = phone;
        this.profile_pic = profile_pic;
        this.bio = bio;
        City = city;
        Country = country;
    }

    public User(boolean isProfileCompleted,String phone) {
        this.is_profile_completed = isProfileCompleted;
        this.phone = phone;
    }

    public User() {

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public List<Topics> getTopics() {
        return topics;
    }

    public void setTopics(List<Topics> topics) {
        this.topics = topics;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return isIs_profile_completed() == user.isIs_profile_completed() && getUsername().equals(user.getUsername()) && getEmail().equals(user.getEmail()) && getAge().equals(user.getAge()) && getPhone().equals(user.getPhone()) && getProfile_pic().equals(user.getProfile_pic()) && getBio().equals(user.getBio()) && getCity().equals(user.getCity()) && getCountry().equals(user.getCountry()) && Objects.equals(getTopics(), user.getTopics()) && Objects.equals(created_at, user.created_at) && Objects.equals(updated_at, user.updated_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), isIs_profile_completed(), getEmail(), getAge(), getPhone(), getProfile_pic(), getBio(), getCity(), getCountry(), getTopics(), created_at, updated_at);
    }

    public static class Builder {
        private String username;
        private String email;
        private Integer age;
        private boolean is_profile_completed;
        private String phone;
        private String profile_pic;
        private String bio;
        private String City;
        private String Country;
        private @ServerTimestamp Date created_at;
        private @ServerTimestamp Date updated_at;

        public Builder() {
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setAge(Integer age) {
            this.age = age;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setIs_profile_completed(boolean is_profile_completed) {
            this.is_profile_completed = is_profile_completed;
            return this;
        }

        public Builder setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
            return this;
        }

        public Builder setBio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder setCity(String city) {
            City = city;
            return this;
        }

        public Builder setCountry(String country) {
            Country = country;
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


        public User build() {
            return new User(username, is_profile_completed, email, age, phone, profile_pic, bio, City, Country);
        }
    }
}
