package com.media.mediaplayer;


public class Songs {
    long id;
    String name;
    String artist;

    public Songs(long id, String name, String artist) {
        this.id = id;
        this.name = name;
        this.artist = artist;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }
}
