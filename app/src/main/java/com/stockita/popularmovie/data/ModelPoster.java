package com.stockita.popularmovie.data;

/**
 * Created by hishmadabubakaralamudi on 11/1/15.
 */
public class ModelPoster {

    private int _id;
    private String movieId;
    private String posterPath;
    private long postingTime;

    public ModelPoster() {

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

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public long getPostingTime() {
        return postingTime;
    }

    public void setPostingTime(long postingTime) {
        this.postingTime = postingTime;
    }
}
