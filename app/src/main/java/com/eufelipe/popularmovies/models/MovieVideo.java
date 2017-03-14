package com.eufelipe.popularmovies.models;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieVideo implements Parcelable {

    private final String BASE_URL_IMAGE = "http://i3.ytimg.com/vi/%s/hqdefault.jpg";

    private String id;
    private String key;
    private String name;
    private int movieId;

    private MovieVideo() {
    }

    private MovieVideo(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        movieId = in.readInt();
    }

    public static final Creator<MovieVideo> CREATOR = new Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel in) {
            return new MovieVideo(in);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };

    public String getId() {
        return id;
    }

    private MovieVideo setId(String id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    private MovieVideo setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        return name;
    }

    private MovieVideo setName(String name) {
        this.name = name;
        return this;
    }

    public int getMovieId() {
        return movieId;
    }

    public MovieVideo setMovieId(int movieId) {
        this.movieId = movieId;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeInt(movieId);
    }


    public String getImage() {
        if (getKey() == null) {
            return null;
        }
        return String.format(BASE_URL_IMAGE, getKey());
    }


    public static List<MovieVideo> convertStringJsonForMovieVideo(String jsonString) {
        JSONObject json = null;
        List<MovieVideo> movieVideos = new ArrayList<>();

        try {
            json = new JSONObject(jsonString);
            JSONArray results = json.getJSONArray("results");

            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonObject = results.getJSONObject(i);
                    MovieVideo movieVideo = parse(jsonObject);
                    if (movieVideo != null) {
                        movieVideos.add(movieVideo);
                    }
                }
            }

            return movieVideos;

        } catch (JSONException e) {
            return null;
        }

    }


    private static MovieVideo parse(JSONObject jsonObject) {

        MovieVideo movieVideo = new MovieVideo();
        try {

            if (jsonObject.has("id")) {
                movieVideo.setId(jsonObject.getString("id"));
            }

            if (jsonObject.has("key")) {
                movieVideo.setKey(jsonObject.getString("key"));
            }

            if (jsonObject.has("name")) {
                movieVideo.setName(jsonObject.getString("name"));
            }

        } catch (JSONException e) {
            return null;
        }

        return movieVideo;
    }
}
