package net.kalp.reflectr.models;

import androidx.annotation.NonNull;

public class Media {
    private String type;
    private String url;

    public Media() {
    }

    public Media(String type, String url) {
        this.type = type;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NonNull
    @Override
    public String toString(){
        return "Media{" +
                "type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
