package com.eufelipe.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.application.App;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Movie implements Parcelable {

    private final static String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/";
    private final static String SIZE_IMAGE = "w185";
    private final static String SIZE_IMAGE_BACKDROP = "w600";

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @SerializedName("id")
    private int id;

    @SerializedName("adult")
    private boolean isAdult;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private Date releaseDate;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String poster;

    @SerializedName("backdrop_path")
    private String backdrop;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("video")
    private boolean isVideo;

    @SerializedName("tagline")
    @Expose
    private String tagline;

    @SerializedName("runtime")
    @Expose
    private int runtime;


    private Movie() {
    }

    public Integer getId() {
        return id;
    }

    private Movie setId(Integer id) {
        this.id = id;
        return this;
    }

    public Boolean getAdult() {
        return isAdult;
    }

    private Movie setAdult(Boolean adult) {
        isAdult = adult;
        return this;
    }

    public String getOverview() {
        if (overview == null) {
            return "";
        }
        return overview;
    }

    private Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    private Date getReleaseDate() {
        return releaseDate;
    }

    private Movie setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    private Movie setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    private Movie setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        }
        return title;
    }

    private Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    private String getPoster() {
        return poster;
    }

    private Movie setPoster(String poster) {
        this.poster = poster;
        return this;
    }

    private String getBackdrop() {
        return backdrop;
    }

    private Movie setBackdrop(String backdrop) {
        this.backdrop = backdrop;
        return this;
    }

    public Double getPopularity() {
        return popularity;
    }

    private Movie setPopularity(Double popularity) {
        this.popularity = popularity;
        return this;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    private Movie setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    private Movie setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    private Movie setIsVideo(Boolean video) {
        isVideo = video;
        return this;
    }

    public String getTagline() {
        return tagline;
    }

    private Movie setTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public int getRuntime() {
        return runtime;
    }

    private Movie setRuntime(int runtime) {
        this.runtime = runtime;
        return this;
    }

    public String getRuntimeDisplay() {
        if (getRuntime() == 0) {
            return null;
        }
        String min = App.mGlobalContext.getString(R.string.min);
        return String.format("%d %s", runtime, min);
    }


    public String getReleaseDateDisplay() {
        DateFormat dfmt = new SimpleDateFormat("MMMM',' yyyy");
        return dfmt.format(getReleaseDate());

    }

    public String getVoteAverageDisplay() {
        if (getVoteAverage() == 0) {
            return null;
        }

        return String.valueOf(getVoteAverage());

    }

    /**
     * Método para montar a imagem
     *
     * @return
     */
    public String getPosterImage() {
        if (getPoster() == null) {
            return null;
        }

        return String.format("%s%s%s", BASE_URL_IMAGE, SIZE_IMAGE, getPoster());
    }

    /**
     * Método para retornar a imagem de fundo de divulgação do filme
     *
     * @return
     */
    public String getBackdropImage() {
        if (getBackdrop() == null) {
            return null;
        }

        return String.format("%s%s%s", BASE_URL_IMAGE, SIZE_IMAGE_BACKDROP, getBackdrop());
    }


    /**
     * Parcelable
     */


    private Movie(Parcel in) {
        id = in.readInt();
        isAdult = in.readByte() != 0;
        overview = in.readString();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        poster = in.readString();
        backdrop = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        voteAverage = in.readDouble();
        isVideo = in.readByte() != 0;
        tagline = in.readString();
        runtime = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeByte((byte) (isAdult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(originalTitle);
        parcel.writeString(originalLanguage);
        parcel.writeString(title);
        parcel.writeString(poster);
        parcel.writeString(backdrop);
        parcel.writeDouble(popularity);
        parcel.writeInt(voteCount);
        parcel.writeDouble(voteAverage);
        parcel.writeByte((byte) (isVideo ? 1 : 0));
        parcel.writeString(tagline);
        parcel.writeInt(runtime);
    }
}
