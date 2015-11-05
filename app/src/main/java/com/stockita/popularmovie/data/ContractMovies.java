package com.stockita.popularmovie.data;

/*
The MIT License (MIT)

Copyright (c) 2015 Hishmad Abubakar Al-Amudi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContractMovies {


    // Constant for the content URI
    public static final String SCHEME = "content://";
    public static final String AUTHORITY =
            "com.stockita.popularmovie.data.ContentProviderMovies";
    public static final String URI = SCHEME + AUTHORITY;

    // To make the CONTENT_URI we need SCHEME, AUTHORITY
    public static final Uri BASE_CONTENT_URI = Uri.parse(URI);


    // Directories
    public static final String DIR_MOVIES = "movies";
    public static final String DIR_GENRE = "genres";
    public static final String DIR_TRAILER = "trailers";
    public static final String DIR_REVIEW = "reviews";
    public static final String DIR_CREDIT = "casts";
    public static final String DIR_POSTER = "posters";

    public static final String DIR_FULL_MOVIES = "fullmovies";


    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(DIR_MOVIES).build();

        public static final Uri FULL_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(DIR_FULL_MOVIES).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "/" + DIR_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "/" + DIR_MOVIES;

        public static final String TABLE_NAME = "movies";

        // Column
        public static final String COLUMN_BACKDROP_PATH = "backdrops"; //1
        public static final String COLUMN_MOVIE_ID = "movieids"; //2
        public static final String COLUMN_MOVIE_TITLE = "movietitles"; //3
        public static final String COLUMN_ORIGINAL_LANGUAGE = "languages"; //4
        public static final String COLUMN_OVERVIEW = "overviews"; //5
        public static final String COLUMN_RELEASE_DATE = "releases"; //6
        public static final String COLUMN_POSTER_PATH = "posters"; //7
        public static final String COLUMN_POPULARITY = "popularitys"; //8
        public static final String COLUMN_AVERAGE_VOTE = "voteaverages"; //9
        public static final String COLUMN_VOTE_COUNT = "votecounts"; //10
        public static final String COLUMN_POSTING_TIME = "postingtime"; //11
        public static final String COLUMN_SORT_GROUP = "sortgroup"; //12


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFullMovieUri(long id) {
            return ContentUris.withAppendedId(FULL_CONTENT_URI, id);
        }

        // COLUMN INDEX...
        public static final int INDEX_COL_ID = 0;
        public static final int INDEX_COL_BACKDROP_PATH = 1;
        public static final int INDEX_COL_MOVIE_ID = 2;
        public static final int INDEX_COL_MOVIE_TITLE = 3;
        public static final int INDEX_COL_ORIGINAL_LANGUAGE = 4;
        public static final int INDEX_COL_OVERVIEW = 5;
        public static final int INDEX_COL_RELEASE_DATE = 6;
        public static final int INDEX_COL_POSTER_PATH = 7;
        public static final int INDEX_COL_POPULARITY = 8;
        public static final int INDEX_COL_AVERAGE_VOTE = 9;
        public static final int INDEX_COL_VOTE_COUNT = 10;
        public static final int INDEX_COL_POSTING_TIME = 11;
        public static final int INDEX_COL_SORT_GROUP = 12;

    }

    public static final class GenreEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(DIR_GENRE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "/" + DIR_GENRE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "/" + DIR_GENRE;

        public static final String TABLE_NAME = "genres";

        // Column
        public static final String COLUMN_MOVIE_ID = "movieids";
        public static final String COLUMN_GENRE = "genres";
        public static final String COLUMN_POSTING_TIME = "postingtime";
        public static final String COLUMN_SORT_GROUP = "sortgroup";


        public static Uri buildGenreUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // COLUMN INDEX...
        public static final int INDEX_COL_ID = 0;
        public static final int INDEX_COL_MOVIE_ID = 1;
        public static final int INDEX_COL_GENRE = 2;
        public static final int INDEX_COL_POSTING_TIME = 3;
        public static final int INDEX_COL_SORT_GROUP = 4;
    }

    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(DIR_TRAILER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "/" + DIR_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "/" + DIR_TRAILER;

        public static final String TABLE_NAME = "trailers";

        // Column
        public static final String COLUMN_MOVIE_ID = "movieids";
        public static final String COLUMN_TRAILER = "trailers";
        public static final String COLUMN_POSTING_TIME = "postingtime";

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // COLUMN INDEX...
        public static final int INDEX_COL_ID = 0;
        public static final int INDEX_COL_MOVIE_ID = 1;
        public static final int INDEX_COL_TRAILER = 2;
        public static final int INDEX_COL_POSTING_TIME = 3;

    }

    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(DIR_REVIEW).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "/" + DIR_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "/" + DIR_REVIEW;

        public static final String TABLE_NAME = "reviews";

        // Column
        public static final String COLUMN_MOVIE_ID = "movieids";
        public static final String COLUMN_USER_NAME = "usernames";
        public static final String COLUMN_USER_COMMENT = "usercomments";
        public static final String COLUMN_POSTING_TIME = "postingtime";

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // COLUMN INDEX...
        public static final int INDEX_COL_ID = 0;
        public static final int INDEX_COL_MOVIE_ID = 1;
        public static final int INDEX_COL_USER_NAME = 2;
        public static final int INDEX_COL_USER_COMMENT = 3;
        public static final int INDEX_COL_POSTING_TIME = 4;

    }

    public static final class CreditEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(DIR_CREDIT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "/" + DIR_CREDIT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "/" + DIR_CREDIT;

        public static final String TABLE_NAME = "cast";

        // Column
        public static final String COLUMN_MOVIE_ID = "movieids";
        public static final String COLUMN_CAST_ID = "castid";
        public static final String COLUMN_CHARACTER = "character";
        public static final String COLUMN_NAME = "playername";
        public static final String COLUMN_PROFILE_PATH = "profilepath";
        public static final String COLUMN_POSTING_TIME = "postingtime";

        public static Uri buildCreditUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // COLUMN INDEX
        public static final int INDEX_COL_ID = 0;
        public static final int INDEX_COL_MOVIE_ID = 1;
        public static final int INDEX_CAST_ID = 2;
        public static final int INDEX_CHARACTER = 3;
        public static final int INDEX_NAME = 4;
        public static final int INDEX_PROFILE_PATH = 5;
        public static final int INDEX_COL_POSTING_TIME = 6;
    }

    public static final class PosterEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(DIR_POSTER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "/" + DIR_POSTER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "/" + DIR_POSTER;

        public static final String TABLE_NAME = "posters";

        // Column
        public static final String COLUMN_MOVIE_ID = "movieid";
        public static final String COLUMN_FILE_PATH = "filepath";
        public static final String COLUMN_POSTING_TIME = "postingtime";

        public static Uri buildPosterUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // Column Indexes
        public static final int INDEX_COL_ID = 0;
        public static final int INDEX_COL_MOVIE_ID = 1;
        public static final int INDEX_COL_FILE_PATH = 2;
        public static final int INDEX_POSTING_TIME = 3;

    }

}
