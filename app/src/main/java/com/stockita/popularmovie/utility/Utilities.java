package com.stockita.popularmovie.utility;

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

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.stockita.popularmovie.data.ContractMovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Utilities {

    // Constant
    private static final String LOG_TAG = Utilities.class.getSimpleName();
    public static final String REQUEST_METHOD_GET = "GET";
    // Key to access themoviedb.org
    public static final String KEYS = "Your key here";
    // This is the URL sorted by rating
    public static final String URL_THE_MOVIE_DB_POPULAR_PageOne =
            "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=1&api_key=" + KEYS;
    public static final String URL_THE_MOVIE_DB_POPULAR_PageTwo =
            "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=2&api_key=" + KEYS;
    // This is the URL sorted by top rating
    public static final String URL_THE_MOVIE_DB_RATING_PageOne =
            "http://api.themoviedb.org/3/discover/movie?sort_by=vote_count.desc&page=1&api_key=" + KEYS;
    public static final String URL_THE_MOVIE_DB_RATING_PageTwo =
            "http://api.themoviedb.org/3/discover/movie?sort_by=vote_count.desc&page=2&api_key=" + KEYS;
    // This is the URL for upcoming.
    public static final String URL_THE_MOVIE_DB_UPCOMING_PageOne =
            "https://api.themoviedb.org/3/movie/upcoming?page=1&api_key=" + KEYS;
    public static final String URL_THE_MOVIE_DB_UPCOMING_PageTwo =
            "https://api.themoviedb.org/3/movie/upcoming?page=2&api_key=" + KEYS;
    // This is the URL for now playing.
    public static final String URL_THE_MOVIE_DB_NOWPLAYING_PageOne =
            "https://api.themoviedb.org/3/movie/now_playing?page=1&api_key=" + KEYS;
    public static final String URL_THE_MOVIE_DB_NOWPLAYING_PageTwo =
            "https://api.themoviedb.org/3/movie/now_playing?page=2&api_key=" + KEYS;
    // Searching
    public static final String URL_SEARCH_PAGE_ONE =
            "https://api.themoviedb.org/3/search/movie?api_key=" + KEYS + "&page=1&query=";
    public static final String URL_SEARCH_PAGE_TWO =
            "https://api.themoviedb.org/3/search/movie?api_key=" + KEYS + "&page=2&query=";
    // Photo base
    public static final String PHOTO_BASE_URL =
            "http://image.tmdb.org/t/p/w342";

    // This is the trailer & reviews BASE URL / {ID}
    public static final String MOVIE_BASE_URL =
            "https://api.themoviedb.org/3/movie/";
    // this is the trailer URL
    public static final String TRAILER_URL =
            "/videos?api_key=" + KEYS;
    // This is the user reviews URL
    public static final String REVIEWS_URL =
            "/reviews?api_key=" + KEYS;
    // This is the credit URL
    public static final String CREDITS_URL =
            "/credits?api_key=" + KEYS;
    // This is the images available for each movie.
    public static final String IMAGES_URL =
            "/images?api_key=" + KEYS;

    // Global variables
    private static ContentResolver sContentResolver;
    public static long sCurrentTime = 0;
    public static boolean sNetworkResponse = true;



    /**
     * This will make a GET request to a RESTful web.
     *
     * @return String of JSON format
     */
    public static String getMovieData(String uri, Context context) {

        BufferedReader reader = null;
        HttpURLConnection con = null;

        try {
            URL url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000 /* milliseconds */);
            con.setConnectTimeout(15000 /* milliseconds */);
            con.setRequestMethod(REQUEST_METHOD_GET);
            con.connect();

            int response = con.getResponseCode();

            if (response <200 || response >299) {
                Log.e(LOG_TAG, "connection failed: " + response);
                sNetworkResponse = false;
                return null;
            }

            InputStream inputStream = con.getInputStream();

            // Return null if no date
            if (inputStream == null) {
                Log.e(LOG_TAG, "inputStream returned null");
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            
            // Return null if no data
            if (builder.length() == 0) {
                Log.e(LOG_TAG, "builder return null");
                return null;
            }

            return builder.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            sNetworkResponse = false;
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
                if (con != null) con.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    /**
     * This is the method that will do the parsing and bulk inserting to tables,
     * MovieEntry, GenreEntry.
     *
     */
    public static void parseFeed(final Context context, String dataFetch, String key, String sortGroup) {

        // Instantiate the Content Resolver.
        sContentResolver = context.getContentResolver();

        // Timestamp.
        sCurrentTime = System.currentTimeMillis();

        // A container for MovieEntry for bulk insert
        ArrayList<ContentValues> lValuesMovieEntry = new ArrayList<>();

        // Lets rock n roll.
        try {

            // String to the root of JSONObject, the argument s is a String in JSON format.
            JSONObject rootObject = new JSONObject(dataFetch);

            // Call the rootObjet and get the key assign to String variable
            String rootObj = rootObject.get("results").toString();

            // Now the rootObj has the value of "result" which is a JSON Array
            // then assign it as argument into JSONArray object
            JSONArray ar = new JSONArray(rootObj);
            final int arSize = ar.length();

            // Iterate for each element in the JSONArray
            for (int i = 0; i < arSize; i++) {

                // Convert each element in ar into JSONOject
                // and pass the argument [i] which [i] is the index of each element.
                JSONObject obj = ar.getJSONObject(i);

                // Add Temp variable so we pass it to parseGenre() argument later.
                String movieIdHolder = obj.getString("id");

                // For MovieEntry
                ContentValues contentMovieValues = new ContentValues();
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_BACKDROP_PATH, obj.getString("backdrop_path"));
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_MOVIE_ID, String.valueOf(movieIdHolder));
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, obj.getString("original_language"));
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_MOVIE_TITLE, obj.getString("original_title"));
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_OVERVIEW, obj.getString("overview"));
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_RELEASE_DATE, obj.getString("release_date"));
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_POSTER_PATH, obj.getString("poster_path"));
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_POPULARITY, obj.getInt("popularity"));
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_AVERAGE_VOTE, obj.getDouble("vote_average"));
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_VOTE_COUNT, obj.getInt("vote_count"));
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_POSTING_TIME, sCurrentTime);
                contentMovieValues.put(ContractMovies.MovieEntry.COLUMN_SORT_GROUP, sortGroup);

                // Pack the object contentMovieValues into ArrayList<ContentValues> lValuesMovieEntry
                lValuesMovieEntry.add(contentMovieValues);

                // Begin of GenreEntry insert.
                // for extract sub array/field genre_ids
                JSONArray genre = obj.getJSONArray("genre_ids");
                parseGenre(context, movieIdHolder, genre, sCurrentTime, sortGroup);

            } // end for loop

            // MovieEntry
            // Bulk insert for MovieEntry.
            try {
                // Bulk insert
                ContentValues[] lInsertData = new ContentValues[lValuesMovieEntry.size()];
                lValuesMovieEntry.toArray(lInsertData);
                int lInsertedData = context.getContentResolver().bulkInsert(ContractMovies.MovieEntry.CONTENT_URI,
                        lInsertData);

            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
            }

            // Store the current time millis in SharedPreference so we can use it
            // the next time.
            setTimeStamp(context, key, sCurrentTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to parse and then insert the GENRE within the MovieEntry,
     * after parse then insert into GenreEntry.
     *
     * @throws JSONException
     */
    private static void parseGenre(Context context, String id, JSONArray genre, long currentTime, String sortGroup)
            throws JSONException {
        
        // Containers
        ContentValues lContentValuesGenre = new ContentValues();

        // iterate for each element in genre
        final int genreSize = genre.length();
        for (int j = 0; j < genreSize; j++) {

            // Genre list.
            String tempGenreList = genreList((Integer) genre.get(j));

            // Packing to ContentValues object
            lContentValuesGenre.put(ContractMovies.GenreEntry.COLUMN_MOVIE_ID, id);
            lContentValuesGenre.put(ContractMovies.GenreEntry.COLUMN_GENRE, tempGenreList);
            lContentValuesGenre.put(ContractMovies.GenreEntry.COLUMN_POSTING_TIME, currentTime);
            lContentValuesGenre.put(ContractMovies.GenreEntry.COLUMN_SORT_GROUP, sortGroup);

            try {
                context.getContentResolver().insert(ContractMovies.GenreEntry.CONTENT_URI, lContentValuesGenre);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    /**
     * Parse trailer link and insert them into TrailerEntry table.
     */
    public static void parseAndInsertTrailer(Context context, String jsonData, String movieId)
            throws JSONException {

        // Timestamp.
        final long currentTime = System.currentTimeMillis();

        // String to the root of JSONObject, the argument s is a String in JSON format.
        JSONObject rootObject = new JSONObject(jsonData);

        // call the rootObjet and get the key assign to String variable
        String rootObj = rootObject.get("results").toString();

        // Containers
        ContentValues lContentValuesTrailer = new ContentValues();

        // now the rootObj has the value of "result" which is a JSON Array
        // then assign it as argument into JSONArray object
        JSONArray ar = new JSONArray(rootObj);
        final int arSize = ar.length();

        // iterate for each element in the JSONArray
        for (int i = 0; i < arSize; i++) {
            // convert each element in ar into JSONOject
            // and pass the argument i which i is the index of each element
            JSONObject obj = ar.getJSONObject(i);

            // Packing to ContentValues object
            lContentValuesTrailer.put(ContractMovies.TrailerEntry.COLUMN_MOVIE_ID, movieId);
            lContentValuesTrailer.put(ContractMovies.TrailerEntry.COLUMN_TRAILER, obj.getString("key"));
            lContentValuesTrailer.put(ContractMovies.TrailerEntry.COLUMN_POSTING_TIME, currentTime);

            try {
                context.getContentResolver().insert(ContractMovies.TrailerEntry.CONTENT_URI, lContentValuesTrailer);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    /**
     * Parse reviews link and insert them into ReviewEntry table.
     *
     */
    public static void parseAndInsertReview(Context context, String jsonData, String id) throws JSONException {

        // Timestamp.
        final long currentTime = System.currentTimeMillis();

        // String to the root of JSONObject, the argument s is a String in JSON format.
        JSONObject rootObject = new JSONObject(jsonData);

        // call the rootObjet and get the key assign to String variable
        String rootObj = rootObject.get("results").toString();

        // Containers
        ContentValues lContentValueReview = new ContentValues();

        // now the rootObj has the value of "result" which is a JSON Array
        // then assign it as argument into JSONArray object
        JSONArray ar = new JSONArray(rootObj);
        final int arSize= ar.length();

        // iterate for each element in the JSONArray
        for (int i = 0; i < arSize; i++) {
            // convert each element in ar into JSONOject
            // and pass the argument i which i is the index of each element
            JSONObject obj = ar.getJSONObject(i);

            // Packing into ContentValues object
            lContentValueReview.put(ContractMovies.ReviewEntry.COLUMN_MOVIE_ID, String.valueOf(id));
            lContentValueReview.put(ContractMovies.ReviewEntry.COLUMN_USER_NAME, obj.getString("author"));
            lContentValueReview.put(ContractMovies.ReviewEntry.COLUMN_USER_COMMENT, obj.getString("content"));
            lContentValueReview.put(ContractMovies.ReviewEntry.COLUMN_POSTING_TIME, currentTime);

            try {
                context.getContentResolver().insert(ContractMovies.ReviewEntry.CONTENT_URI, lContentValueReview);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    /**
     * Parse and insert cast names and pictures into CreditEntry table

     */
    public static void parseAndInsertCredits(Context context, String jsonData, String id) throws JSONException {

        // Timestamp.
        final long currentTime = System.currentTimeMillis();

        // String to the root of JSONObject, the argument s is a String in JSON format.
        JSONObject rootObject = new JSONObject(jsonData);

        // call the rootObjet and get the key assign to String variable
        String rootObj = rootObject.get("cast").toString();

        // now the rootObj has the value of "cast" which is a JSON Array
        // then assign it as argument into JSONArray object
        JSONArray ar = new JSONArray(rootObj);
        final int arSize = ar.length();

        // Containers
        ContentValues lContentValueCredits = new ContentValues();

        // iterate for each element in the JSONArray
        for (int i = 0; i < arSize; i++) {
            // convert each element in ar into JSONOject
            // and pass the argument i which i is the index of each element
            JSONObject obj = ar.getJSONObject(i);

            // Packing into ContentValues object
            lContentValueCredits.put(ContractMovies.CreditEntry.COLUMN_MOVIE_ID, String.valueOf(id));
            lContentValueCredits.put(ContractMovies.CreditEntry.COLUMN_CAST_ID, obj.getString("cast_id"));
            lContentValueCredits.put(ContractMovies.CreditEntry.COLUMN_CHARACTER, obj.getString("character"));
            lContentValueCredits.put(ContractMovies.CreditEntry.COLUMN_NAME, obj.getString("name"));
            lContentValueCredits.put(ContractMovies.CreditEntry.COLUMN_PROFILE_PATH, obj.getString("profile_path"));
            lContentValueCredits.put(ContractMovies.CreditEntry.COLUMN_POSTING_TIME, currentTime);

            // Do insert now
            try {
                context.getContentResolver().insert(ContractMovies.CreditEntry.CONTENT_URI, lContentValueCredits);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    /**
     *
     * Parse and insert PosterEntry
     *
     * @throws JSONException
     */
    public static void parseAndInsertPosters(Context context, String jsonData, String id) throws JSONException {

        // Timestamp.
        final long currentTime = System.currentTimeMillis();

        // String to the root of JSONObject, the argument s is a String in JSON format.
        JSONObject rootObject = new JSONObject(jsonData);

        // call the rootObjet and get the key assign to String variable
        String rootObj = rootObject.get("backdrops").toString();

        // now the rootObj has the value of "backdrops" which is a JSON Array
        // then assign it as argument into JSONArray object
        JSONArray ar = new JSONArray(rootObj);
        final int arSize = ar.length();

        // Containers
        ContentValues lContentValuePoster = new ContentValues();

        // iterate for each element in the JSONArray
        for (int i = 0; i < arSize; i++) {
            // convert each element in ar into JSONOject
            // and pass the argument i which i is the index of each element
            JSONObject obj = ar.getJSONObject(i);

            // Packing into ContentValues object
            lContentValuePoster.put(ContractMovies.PosterEntry.COLUMN_MOVIE_ID, String.valueOf(id));
            lContentValuePoster.put(ContractMovies.PosterEntry.COLUMN_FILE_PATH, obj.getString("file_path"));
            lContentValuePoster.put(ContractMovies.PosterEntry.COLUMN_POSTING_TIME, currentTime);

            // Do insert now
            try {
                context.getContentResolver().insert(ContractMovies.PosterEntry.CONTENT_URI, lContentValuePoster);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
            }
        }
    }


    /**
     * Genre's code
     */
    public static String genreList(int genreCode)  {
        HashMap<Integer, String> genreMovieList = new HashMap<>();

        genreMovieList.put(28, "Action");
        genreMovieList.put(12, "Adventure");
        genreMovieList.put(16, "Animation");
        genreMovieList.put(35, "Comedy");
        genreMovieList.put(80, "Crime");
        genreMovieList.put(99, "Documentary");
        genreMovieList.put(18, "Drama");
        genreMovieList.put(10751, "Family");
        genreMovieList.put(14, "Fantasy");
        genreMovieList.put(10769, "Foreign");
        genreMovieList.put(36, "History");
        genreMovieList.put(27, "Horror");
        genreMovieList.put(10402, "Music");
        genreMovieList.put(9648, "Mystery");
        genreMovieList.put(10749, "Romance");
        genreMovieList.put(878, "Science Fiction");
        genreMovieList.put(10770, "TV Movie");
        genreMovieList.put(53, "Thriller");
        genreMovieList.put(10752, "War");
        genreMovieList.put(37, "Western");

        try {
            return genreMovieList.get(genreCode);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            return "No match";
        }
    }


    /**
     * Download and Parse
     */
    public static void downloadAllData(Context context) {
        // Download data and store the string in SharedPreferences
        String dataNowPlayingOne = getMovieData(URL_THE_MOVIE_DB_NOWPLAYING_PageOne, context);
        setMovieId(context, "dataNowPlayingOne", dataNowPlayingOne);

        String dataNowPlayingTwo = getMovieData(URL_THE_MOVIE_DB_NOWPLAYING_PageTwo, context);
        setMovieId(context, "dataNowPlayingTwo", dataNowPlayingTwo);

        String dataUpcomingOne = getMovieData(URL_THE_MOVIE_DB_UPCOMING_PageOne, context);
        setMovieId(context, "dataUpcomingOne", dataUpcomingOne);

        String dataUpcomingTwo = getMovieData(URL_THE_MOVIE_DB_UPCOMING_PageTwo, context);
        setMovieId(context, "dataUpcomingTwo", dataUpcomingTwo);

        String dataPopularOne = getMovieData(URL_THE_MOVIE_DB_POPULAR_PageOne, context);
        setMovieId(context, "dataPopularOne", dataPopularOne);

        String dataPopularTwo = getMovieData(URL_THE_MOVIE_DB_POPULAR_PageTwo, context);
        setMovieId(context, "dataPopularTwo", dataPopularTwo);

        String dataRatingOne = getMovieData(URL_THE_MOVIE_DB_RATING_PageOne, context);
        setMovieId(context, "dataRatingOne", dataRatingOne);

        String dataRatingTwo = getMovieData(URL_THE_MOVIE_DB_RATING_PageTwo, context);
        setMovieId(context, "dataRatingTwo", dataRatingTwo);



        // Delete all data before insert new data
        if (sNetworkResponse) {
            try {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(ContractMovies.MovieEntry.CONTENT_URI, null, null);
                contentResolver.delete(ContractMovies.GenreEntry.CONTENT_URI, null, null);
                contentResolver.delete(ContractMovies.CreditEntry.CONTENT_URI, null, null);
                contentResolver.delete(ContractMovies.TrailerEntry.CONTENT_URI, null, null);
                contentResolver.delete(ContractMovies.PosterEntry.CONTENT_URI, null, null);
                contentResolver.delete(ContractMovies.ReviewEntry.CONTENT_URI, null, null);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
            }

            // Parse and insert data into database
            parseFeed(context, getMovieId(context, "dataNowPlayingOne"), "nowplayingseven", "nowplaying");
            parseFeed(context, getMovieId(context, "dataNowPlayingTwo"), "nowplayingeight", "nowplaying");
            parseFeed(context, getMovieId(context, "dataUpcomingOne"), "upcomingfive", "upcoming");
            parseFeed(context, getMovieId(context, "dataUpcomingTwo"), "upcomingsix", "upcoming");
            parseFeed(context, getMovieId(context, "dataPopularOne"), "popularone", "popular");
            parseFeed(context, getMovieId(context, "dataPopularTwo"), "populartwo", "popular");
            parseFeed(context, getMovieId(context, "dataRatingOne"), "ratingthree", "rating");
            parseFeed(context, getMovieId(context, "dataRatingTwo"), "ratingfour", "rating");
        }
    }

    /**
     * Same as above but this one must get user input to search for a movie.
     */
    public static void downloadSearchData(Context context, String searchForAMovie) {

        // Download data and store the string in SharedPreferences
        String dataPopularOne = getMovieData(URL_SEARCH_PAGE_ONE + searchForAMovie, context);
        setMovieId(context, "searchMovieOne", dataPopularOne);

        String dataPopularTwo = getMovieData(URL_SEARCH_PAGE_TWO + searchForAMovie, context);
        setMovieId(context, "searchMovieTwo", dataPopularTwo);


        // Delete search data before insert new data
        try {
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.delete(ContractMovies.MovieEntry.CONTENT_URI,
                    ContractMovies.MovieEntry.COLUMN_SORT_GROUP + "=?", new String[]{"search"});
            contentResolver.delete(ContractMovies.GenreEntry.CONTENT_URI,
                    ContractMovies.GenreEntry.COLUMN_SORT_GROUP + "=?", new String[]{"search"});
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }

        // Parse and insert new data
        parseFeed(context, getMovieId(context, "searchMovieOne"), "searchOne", "search");
        parseFeed(context, getMovieId(context, "searchMovieTwo"), "searchTwo", "search");

    }

    /**
     * This is special method to fetch data when user click on detail movie.
     * This will only fetch and parse then insert to database for Movie Trailer.
     */
    public static void getTrailerData(Context context, String movieId) {
        try {
            // Make an HTTP request GET to the server and get JSON in String
            // Assign the result to String variable dataToFetchFrom.
            String dataToFetchFrom = getMovieData(MOVIE_BASE_URL + movieId + TRAILER_URL, context);

            // Parse and insert the JSON data into Database.
            // The first argument is the context, the second argument is the JSON
            // in String format, the third argument is the movie Id.
            parseAndInsertTrailer(context, dataToFetchFrom, movieId);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    /**
     * This is special method to fetch data when user click on detail movie.
     * This will only fetch and parse then insert to database for Movie Review.
     */
    public static void getReviewsData(Context context, String movieId) {
        try {
            // Make an HTTP request GET to the server and get JSON in String
            // Assign the result to String variable dataToFetchFrom.
            String dataToFetchFrom = getMovieData(MOVIE_BASE_URL + movieId + REVIEWS_URL, context);

            // Parse and insert the JSON data into Database.
            // The first argument is the context, the second argument is the JSON
            // in String format, the third argument is the movie Id.
            Utilities.parseAndInsertReview(context, dataToFetchFrom, movieId);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    /**
     * This is special method to fetch data when user click on detail movie.
     * This will only fetch and parse then insert to database for Movie Credits.
     */
    public static void getCreditesData(Context context, String movieId) {
        try {
            // Make an HTTP request GET to the server and get JSON in String
            // Assign the result to String variable dataToFetchFrom.
            String dataToFetchFrom = getMovieData(MOVIE_BASE_URL + movieId + CREDITS_URL, context);

            // Parse and insert the JSON data into Database.
            // The first argument is the context, the second argument is the JSON
            // in String format, the third argument is the movie Id.
            Utilities.parseAndInsertCredits(context, dataToFetchFrom, movieId);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    /**
     * This is special method to fetch data when user click on detail movie.
     * This will only fetch and parse then insert to database for Movie Backdrops images.
     */
    public static void getPosterData(Context context, String movieId) {
        try {
            // Make an HTTP request GET to the server and get JSON in String
            // Assign the result to String variable dataToFetchFrom.
            String dataToFetchFrom = getMovieData(MOVIE_BASE_URL + movieId + IMAGES_URL, context);

            // Parse and insert the JSON data into Database.
            // The first argument is the context, the second argument is the JSON
            // in String format, the third argument is the movie Id.
            Utilities.parseAndInsertPosters(context, dataToFetchFrom, movieId);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    /**
     * Returns true if the network is available or about to become available.
     */
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    /**
     * If the initial is new then it will notify that this is the first time.
     *
     */
    public static String getInitialStatusBeforeSync(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, value);
    }

    /**
     * After sync now, then change the initial from "new" to anything else, such "old"
     *
     */
    public static void setInitialStatusAfterSync(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, value).apply();
    }


    /**
     * This method to store the current time in millis, this will be used to delete
     * old data after fetching new data.
     */
    public static void setTimeStamp(Context context, String key, long timeStamp) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putLong(key, timeStamp).apply();
    }

    /**
     * This method to get the current time in millis that stored last time sync, so
     * we will delete all the rows that has this time stamp.
     */
    public static long getTimeStamp(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getLong(key, 0);

    }

    /**
     * Hide the keyboard
     */
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * We can use this method to store any String into SharedPreferences.
     */
    public static void setMovieId(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, value).apply();
    }

    /**
     * We can use this method to restore any String from SharedPReferences.

     */
    public static String getMovieId(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, "");
    }
}
