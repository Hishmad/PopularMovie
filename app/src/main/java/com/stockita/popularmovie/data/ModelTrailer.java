package com.stockita.popularmovie.data;

/**
 * Created by hishmadabubakaralamudi on 11/1/15.
 */
public class ModelTrailer {

    private int _id;
    private String movieId;
    private String trailer;
    private long postingTime;

    public ModelTrailer() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public long getPostingTime() {
        return postingTime;
    }

    public void setPostingTime(long postingTime) {
        this.postingTime = postingTime;
    }
}
