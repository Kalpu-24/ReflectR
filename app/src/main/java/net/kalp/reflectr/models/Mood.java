package net.kalp.reflectr.models;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.request.RequestOptions;

import java.util.Objects;

public class Mood {
    private Integer emoji;
    private String title;
    private Integer count;
    private Integer level;

    public Mood() {
    }

    public Mood(@DrawableRes Integer emoji, String title, Integer count, Integer level) {
        this.emoji = emoji;
        this.title = title;
        this.count = count;
        this.level = level;
    }
    public Mood(String title, Integer count, Integer level) {
        this.title = title;
        this.count = count;
        this.level = level;
    }

    public @DrawableRes Integer getEmoji() {
        return emoji;
    }

    public void setEmoji(@DrawableRes Integer emoji) {
        this.emoji = emoji;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void incrementCount() {
        this.count++;
    }

    public void decrementCount() {
        this.count--;
    }

    @NonNull
    @Override
    public String toString() {
        return "Mood{" +
                "emoji='" + emoji + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", level=" + level +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mood)) return false;
        Mood mood = (Mood) o;
        return getEmoji().equals(mood.getEmoji()) && getTitle().equals(mood.getTitle()) && getCount().equals(mood.getCount()) && getLevel().equals(mood.getLevel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmoji(), getTitle(), getCount(), getLevel());
    }

    public RequestOptions getOptions() {
        return new RequestOptions().override(100, 100);
    }
}
