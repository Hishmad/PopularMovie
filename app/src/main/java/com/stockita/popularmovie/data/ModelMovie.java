package com.stockita.popularmovie.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hishmadabubakaralamudi on 10/31/15.
 */
public class ModelMovie implements Parcelable {

    private int _id;
    private String backdropPath;
    private String movieId;
    private String movieTitle;
    private String originalLanguage;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private double moviePopularity;
    private double averageVote;
    private int voteCount;



    private long postingTime;
    private String sortGroup;

    public ModelMovie() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getMoviePopularity() {
        return moviePopularity;
    }

    public void setMoviePopularity(double moviePopularity) {
        this.moviePopularity = moviePopularity;
    }

    public double getAverageVote() {
        return averageVote;
    }

    public void setAverageVote(double averageVote) {
        this.averageVote = averageVote;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
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

    protected ModelMovie(Parcel in) {
        _id = in.readInt();
        backdropPath = in.readString();
        movieId = in.readString();
        movieTitle = in.readString();
        originalLanguage = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        moviePopularity = in.readDouble();
        averageVote = in.readDouble();
        voteCount = in.readInt();
        postingTime = in.readLong();
        sortGroup = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(backdropPath);
        dest.writeString(movieId);
        dest.writeString(movieTitle);
        dest.writeString(originalLanguage);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeDouble(moviePopularity);
        dest.writeDouble(averageVote);
        dest.writeInt(voteCount);
        dest.writeLong(postingTime);
        dest.writeString(sortGroup);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ModelMovie> CREATOR = new Parcelable.Creator<ModelMovie>() {
        @Override
        public ModelMovie createFromParcel(Parcel in) {
            return new ModelMovie(in);
        }

        @Override
        public ModelMovie[] newArray(int size) {
            return new ModelMovie[size];
        }
    };
}
