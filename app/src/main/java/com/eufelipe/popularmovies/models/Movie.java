package com.eufelipe.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

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

    final static String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/";
    final static String SIZE_IMAGE = "w185";
    final static String SIZE_IMAGE_BACKDROP = "w600";

    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private int id;
    private boolean isAdult;
    private String overview;
    private Date releaseDate;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String poster;
    private String backdrop;
    private double popularity;
    private int voteCount;
    private int voteAverage;
    private boolean isVideo;

    private String tagline;
    private int runtime;


    public Movie() {
    }

    public Integer getId() {
        return id;
    }

    public Movie setId(Integer id) {
        this.id = id;
        return this;
    }

    public Boolean getAdult() {
        return isAdult;
    }

    public Movie setAdult(Boolean adult) {
        isAdult = adult;
        return this;
    }

    public String getOverview() {
        if (overview == null) {
            return "";
        }
        return overview;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Movie setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public Movie setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public Movie setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        }
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPoster() {
        return poster;
    }

    public Movie setPoster(String poster) {
        this.poster = poster;
        return this;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public Movie setBackdrop(String backdrop) {
        this.backdrop = backdrop;
        return this;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Movie setPopularity(Double popularity) {
        this.popularity = popularity;
        return this;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Movie setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public Integer getVoteAverage() {
        return voteAverage;
    }

    public Movie setVoteAverage(Integer voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public Movie setIsVideo(Boolean video) {
        isVideo = video;
        return this;
    }

    public String getTagline() {
        return tagline;
    }

    public Movie setTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public int getRuntime() {
        return runtime;
    }

    public Movie setRuntime(int runtime) {
        this.runtime = runtime;
        return this;
    }


    public String getReleaseDateDisplay() {
        DateFormat dfmt = new SimpleDateFormat("MMMM',' yyyy");
        return dfmt.format(getReleaseDate());

    }


    public static Movie convertStringJsonForMovie(String jsonString) {
        Movie movie = null;
        try {
            JSONObject json = new JSONObject(jsonString);
            movie = parse(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movie;
    }

    public static List<Movie> convertStringJsonForListOfMovie(String jsonString) {

        JSONObject json = null;
        List<Movie> movies = new ArrayList<>();

        try {
            json = new JSONObject(jsonString);
            JSONArray results = json.getJSONArray("results");

            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonObject = results.getJSONObject(i);
                    Movie movie = parse(jsonObject);
                    if (movie != null) {
                        movies.add(movie);
                    }
                }
            }

            return movies;

        } catch (JSONException e) {
            return null;
        }
    }


    /**
     * @param jsonObject
     * @return
     * @description : Método para realizar o parse de JSONObject para Movie
     */
    public static Movie parse(JSONObject jsonObject) {

        Movie movie = new Movie();
        try {

            if (jsonObject.has("id")) {
                movie.setId(jsonObject.getInt("id"));
            }

            if (jsonObject.has("poster_path")) {
                movie.setPoster(jsonObject.getString("poster_path"));
            }

            if (jsonObject.has("adult")) {
                movie.setAdult(jsonObject.getBoolean("adult"));
            }

            if (jsonObject.has("overview")) {
                movie.setOverview(jsonObject.getString("overview"));
            }

            if (jsonObject.has("release_date")) {
                String releaseDate = jsonObject.getString("release_date");
                Date date = null;
                try {
                    date = simpleDateFormat.parse(releaseDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                movie.setReleaseDate(date);
            }

            if (jsonObject.has("original_title")) {
                movie.setOriginalTitle(jsonObject.getString("original_title"));
            }

            if (jsonObject.has("original_language")) {
                movie.setOriginalLanguage(jsonObject.getString("original_language"));
            }

            if (jsonObject.has("title")) {
                movie.setTitle(jsonObject.getString("title"));
            }
            if (jsonObject.has("backdrop_path")) {
                movie.setBackdrop(jsonObject.getString("backdrop_path"));
            }

            if (jsonObject.has("popularity")) {
                movie.setPopularity(jsonObject.getDouble("popularity"));
            }

            if (jsonObject.has("vote_count")) {
                movie.setVoteCount(jsonObject.getInt("vote_count"));
            }

            if (jsonObject.has("vote_average")) {
                movie.setVoteAverage(jsonObject.getInt("vote_average"));
            }
            if (jsonObject.has("video")) {
                movie.setIsVideo(jsonObject.getBoolean("video"));
            }

            if (jsonObject.has("runtime")) {
                movie.setRuntime(jsonObject.getInt("runtime"));
            }

            if (jsonObject.has("tagline")) {
                movie.setTagline(jsonObject.getString("tagline"));
            }

            return movie;


        } catch (JSONException e) {
            return null;
        }

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

        String url = String.format("%s%s%s", BASE_URL_IMAGE, SIZE_IMAGE, getPoster());
        return url;
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

        String url = String.format("%s%s%s", BASE_URL_IMAGE, SIZE_IMAGE_BACKDROP, getBackdrop());
        return url;
    }


    /**
     * Parcelable
     */


    protected Movie(Parcel in) {
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
        voteAverage = in.readInt();
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
        parcel.writeInt(voteAverage);
        parcel.writeByte((byte) (isVideo ? 1 : 0));
        parcel.writeString(tagline);
        parcel.writeInt(runtime);
    }
}
