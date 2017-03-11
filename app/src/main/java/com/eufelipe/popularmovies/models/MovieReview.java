package com.eufelipe.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieReview implements Parcelable {

    private String id;
    private String author;
    private String content;
    private String url;

    public MovieReview() {

    }

    protected MovieReview(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    public String getId() {
        return id;
    }

    public MovieReview setId(String id) {
        this.id = id;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public MovieReview setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MovieReview setContent(String content) {
        this.content = content;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MovieReview setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(url);
    }

    public static List<MovieReview> convertStringJsonForMovieReviews(String jsonString) {
        JSONObject json = null;
        List<MovieReview> movieReviews = new ArrayList<>();

        try {
            json = new JSONObject(jsonString);
            JSONArray results = json.getJSONArray("results");

            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonObject = results.getJSONObject(i);
                    MovieReview movieReview = parse(jsonObject);
                    if (movieReview != null) {
                        movieReviews.add(movieReview);
                    }
                }
            }

            return movieReviews;

        } catch (JSONException e) {
            return null;
        }
    }

    public static MovieReview parse(JSONObject jsonObject) {

        MovieReview movieReview = new MovieReview();

        try {
            if (jsonObject.has("id")) {
                movieReview.setId(jsonObject.getString("id"));
            }

            if (jsonObject.has("author")) {
                movieReview.setAuthor(jsonObject.getString("author"));
            }

            if (jsonObject.has("content")) {
                movieReview.setContent(jsonObject.getString("content"));
            }
            if (jsonObject.has("url")) {
                movieReview.setUrl(jsonObject.getString("url"));
            }

        } catch (JSONException e) {
            return null;
        }

        return movieReview;
    }
}
