package com.stockita.popularmovie.interfaces;

/**
 * Created by hishmadabubakaralamudi on 10/5/15.
 */
public interface CallThis {
    public void onItemSelectedMovieId(String movieId,
                                      String movieTitle,
                                      String releaseDate,
                                      String posterPath,
                                      String grade,
                                      String genre,
                                      String backDrop,
                                      String overview,
                                      String sortGroup);
}
