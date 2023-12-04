package net.kalp.reflectr.models;

import androidx.annotation.NonNull;

public class Media {
    private Integer type;
    private String url;

    public Media() {
    }

    public Media(Integer type, String url) {
        this.type = type;
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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
                "type='" + type.toString() + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof Media)) return false;
        Media media = (Media) o;
        return media.getType().equals(this.getType()) && media.getUrl().equals(this.getUrl());
    }

    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + this.getType().hashCode();
        result = 31 * result + this.getUrl().hashCode();
        return result;
    }
}
