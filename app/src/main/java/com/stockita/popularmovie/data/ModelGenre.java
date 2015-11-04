package com.stockita.popularmovie.data;

/**
 * Created by hishmadabubakaralamudi on 11/1/15.
 */
public class ModelGenre {

    private int _id;
    private String movieId;
    private String genre;
    private long postingTime;
    private String sortGroup;

    public ModelGenre() {
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public long getPostingTime() {
        return postingTime;
    }

    public void setPostingTime(long postingTime) {
        this.postingTime = postingTime;
    }

    public String getSortGroup() {
        return sortGroup;
    }

    public void setSortGroup(String sortGroup) {
        this.sortGroup = sortGroup;
    }
}
