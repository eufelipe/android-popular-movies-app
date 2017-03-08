package com.eufelipe.popularmovies.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Movie {

    final static String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/";
    final static String SIZE_IMAGE = "w185";
    final static String SIZE_IMAGE_BACKDROP = "w600";

    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Integer id;
    private Boolean isAdult;
    private String overview;
    private Date releaseDate;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String poster;
    private String backdrop;
    private Double popularity;
    private Integer voteCount;
    private Integer voteAverage;
    private Boolean isVideo;

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


}
