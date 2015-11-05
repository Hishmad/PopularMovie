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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;


public class ContentProviderMovies extends ContentProvider {

    // Constant

    private static final String LOG_TAG = ContentProviderMovies.class.getSimpleName();
    private static final String ERROR_UNKNOWN_URI = "Unknown URI: ";
    private static final String ERROR_FILED_TO_INSERT = "Failed to insert row into URI: ";

    private static final int MOVIE_ID = 100;
    private static final int MOVIE_ENTRY = 101;

    private static final int GENRE_ID = 200;
    private static final int GENRE_ENTRY = 201;

    private static final int TRAILER_ID = 300;
    private static final int TRAILER_ENTRY = 301;

    private static final int REVIEW_ID = 400;
    private static final int REVIEW_ENTRY = 401;

    private static final int CREDIT_ID = 500;
    private static final int CREDIT_ENTRY = 501;

    private static final int POSTER_ID = 600;
    private static final int POSTER_ENTRY = 601;

    private static final int MOVIE_FULL = 1000;
    private static final int MOVIE_FULL_DETAIL = 1001;

    private static final SQLiteQueryBuilder sQueryBuilder;

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private DatabaseHelper dbHelper;

    static {

        sQueryBuilder = new SQLiteQueryBuilder();
        sQueryBuilder.setTables(
                ContractMovies.MovieEntry.TABLE_NAME + " LEFT OUTER JOIN " +
                        ContractMovies.GenreEntry.TABLE_NAME + " USING (" + ContractMovies.MovieEntry._ID + ")" +
                        " LEFT OUTER JOIN " + ContractMovies.TrailerEntry.TABLE_NAME +
                        " USING (" + ContractMovies.MovieEntry._ID + ")" + " LEFT OUTER JOIN " +
                        ContractMovies.ReviewEntry.TABLE_NAME + " USING (" + ContractMovies.MovieEntry._ID + ")" +
                        " LEFT OUTER JOINT " + ContractMovies.CreditEntry.TABLE_NAME +
                        " USING (" + ContractMovies.MovieEntry._ID + ")" + " LEFT OUTER JOIN " +
                        ContractMovies.PosterEntry.TABLE_NAME + " USING (" + ContractMovies.MovieEntry._ID + ")");

    }

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ContractMovies.AUTHORITY;

        matcher.addURI(authority, ContractMovies.DIR_MOVIES + "/#", MOVIE_ID);
        matcher.addURI(authority, ContractMovies.DIR_GENRE + "/#", GENRE_ID);
        matcher.addURI(authority, ContractMovies.DIR_TRAILER + "/#", TRAILER_ID);
        matcher.addURI(authority, ContractMovies.DIR_REVIEW + "/#", REVIEW_ID);
        matcher.addURI(authority, ContractMovies.DIR_CREDIT + "/#", CREDIT_ID);
        matcher.addURI(authority, ContractMovies.DIR_POSTER + "/#", POSTER_ID);

        matcher.addURI(authority, ContractMovies.DIR_MOVIES, MOVIE_ENTRY);
        matcher.addURI(authority, ContractMovies.DIR_GENRE, GENRE_ENTRY);
        matcher.addURI(authority, ContractMovies.DIR_TRAILER, TRAILER_ENTRY);
        matcher.addURI(authority, ContractMovies.DIR_REVIEW, REVIEW_ENTRY);
        matcher.addURI(authority, ContractMovies.DIR_CREDIT, CREDIT_ENTRY);
        matcher.addURI(authority, ContractMovies.DIR_POSTER, POSTER_ENTRY);

        matcher.addURI(authority, ContractMovies.DIR_FULL_MOVIES + "/#", MOVIE_FULL_DETAIL);
        matcher.addURI(authority, ContractMovies.DIR_FULL_MOVIES, MOVIE_FULL);

        return matcher;
    }

    /**
     * Instantiate the Database.
     * @return
     */
    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {

            case MOVIE_ENTRY:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case GENRE_ENTRY:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.GenreEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case TRAILER_ENTRY:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case REVIEW_ENTRY:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CREDIT_ENTRY:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.CreditEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case POSTER_ENTRY:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.PosterEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MOVIE_ID:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.MovieEntry.TABLE_NAME,
                        projection,
                        ContractMovies.MovieEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case GENRE_ID:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.GenreEntry.TABLE_NAME,
                        projection,
                        ContractMovies.GenreEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case TRAILER_ID:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.TrailerEntry.TABLE_NAME,
                        projection,
                        ContractMovies.TrailerEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case REVIEW_ID:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.ReviewEntry.TABLE_NAME,
                        projection,
                        ContractMovies.ReviewEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CREDIT_ID:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.CreditEntry.TABLE_NAME,
                        projection,
                        ContractMovies.CreditEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case POSTER_ID:
                cursor = dbHelper.getReadableDatabase().query(
                        ContractMovies.PosterEntry.TABLE_NAME,
                        projection,
                        ContractMovies.PosterEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MOVIE_FULL_DETAIL:
                String[] fullDetailProjection = {
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_BACKDROP_PATH,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_MOVIE_TITLE,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_MOVIE_ID,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_ORIGINAL_LANGUAGE,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_OVERVIEW,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_RELEASE_DATE,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_POSTER_PATH,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_POPULARITY,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_AVERAGE_VOTE,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_VOTE_COUNT,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_POSTING_TIME,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_SORT_GROUP
                };

                cursor = sQueryBuilder.query(dbHelper.getReadableDatabase(),
                        fullDetailProjection,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry._ID,
                        null,
                        sortOrder);
                break;

            case MOVIE_FULL:
                String[] fullProjection = {
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_MOVIE_ID,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_MOVIE_TITLE,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry.COLUMN_POSTER_PATH
                };

                cursor = sQueryBuilder.query(dbHelper.getReadableDatabase(),
                        fullProjection,
                        null,
                        selectionArgs,
                        ContractMovies.MovieEntry.TABLE_NAME + "." + ContractMovies.MovieEntry._ID,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException(ERROR_UNKNOWN_URI + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_FULL_DETAIL:
                return ContractMovies.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_ID:
                return ContractMovies.MovieEntry.CONTENT_ITEM_TYPE;
            case GENRE_ID:
                return ContractMovies.GenreEntry.CONTENT_ITEM_TYPE;
            case TRAILER_ID:
                return ContractMovies.TrailerEntry.CONTENT_ITEM_TYPE;
            case REVIEW_ID:
                return ContractMovies.ReviewEntry.CONTENT_ITEM_TYPE;
            case CREDIT_ID:
                return ContractMovies.CreditEntry.CONTENT_ITEM_TYPE;
            case POSTER_ID:
                return ContractMovies.PosterEntry.CONTENT_ITEM_TYPE;
            case MOVIE_ENTRY:
                return ContractMovies.MovieEntry.CONTENT_TYPE;
            case GENRE_ENTRY:
                return ContractMovies.GenreEntry.CONTENT_TYPE;
            case TRAILER_ENTRY:
                return ContractMovies.TrailerEntry.CONTENT_TYPE;
            case REVIEW_ENTRY:
                return ContractMovies.ReviewEntry.CONTENT_TYPE;
            case CREDIT_ENTRY:
                return ContractMovies.CreditEntry.CONTENT_TYPE;
            case POSTER_ENTRY:
                return ContractMovies.PosterEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException(ERROR_UNKNOWN_URI + uri);
        }

    }

    /**
     * Insert new record
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE_ENTRY: {
                long _id = db.insert(ContractMovies.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ContractMovies.MovieEntry.buildMovieUri(_id);
                } else {
                    throw new android.database.SQLException(ERROR_FILED_TO_INSERT + uri);
                }
                break;
            }

            case GENRE_ENTRY: {
                long _id = db.insert(ContractMovies.GenreEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ContractMovies.GenreEntry.buildGenreUri(_id);
                } else {
                    throw new android.database.SQLException(ERROR_FILED_TO_INSERT + uri);
                }
                break;
            }

            case TRAILER_ENTRY: {
                long _id = db.insert(ContractMovies.TrailerEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ContractMovies.TrailerEntry.buildTrailerUri(_id);
                } else {
                    throw new android.database.SQLException(ERROR_FILED_TO_INSERT + uri);
                }
                break;
            }

            case REVIEW_ENTRY: {
                long _id = db.insert(ContractMovies.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ContractMovies.ReviewEntry.buildReviewUri(_id);
                } else {
                    throw new android.database.SQLException(ERROR_FILED_TO_INSERT + uri);
                }
                break;
            }

            case CREDIT_ENTRY: {
                long _id = db.insert(ContractMovies.CreditEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ContractMovies.CreditEntry.buildCreditUri(_id);
                } else {
                    throw new android.database.SQLException(ERROR_FILED_TO_INSERT + uri);
                }
                break;
            }

            case POSTER_ENTRY: {
                long _id = db.insert(ContractMovies.PosterEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ContractMovies.PosterEntry.buildPosterUri(_id);
                } else {
                    throw new android.database.SQLException(ERROR_FILED_TO_INSERT + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException(ERROR_UNKNOWN_URI + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
     * Bulk insert/add
     * @param uri
     * @param values
     * @return
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int valueLength = values.length;
        super.bulkInsert(uri, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return valueLength;
    }

    /**
     * Delete record
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowDeleted;

        switch (match) {
            case MOVIE_ENTRY:
                rowDeleted = db.delete(
                        ContractMovies.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case GENRE_ENTRY:
                rowDeleted = db.delete(
                        ContractMovies.GenreEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case TRAILER_ENTRY:
                rowDeleted = db.delete(
                        ContractMovies.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case REVIEW_ENTRY:
                rowDeleted = db.delete(
                        ContractMovies.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case CREDIT_ENTRY:
                rowDeleted = db.delete(
                        ContractMovies.CreditEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case POSTER_ENTRY:
                rowDeleted = db.delete(
                        ContractMovies.PosterEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case MOVIE_ID:
                rowDeleted = db.delete(
                        ContractMovies.MovieEntry.TABLE_NAME,
                        ContractMovies.MovieEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException(ERROR_UNKNOWN_URI + uri);
        } // end switch

        // null deletes all rows
        if (selection == null || rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    /**
     * Update record
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE_ENTRY:
                rowsUpdated = db.update(ContractMovies.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case GENRE_ENTRY:
                rowsUpdated = db.update(ContractMovies.GenreEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TRAILER_ENTRY:
                rowsUpdated = db.update(ContractMovies.TrailerEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REVIEW_ENTRY:
                rowsUpdated = db.update(ContractMovies.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CREDIT_ENTRY:
                rowsUpdated = db.update(ContractMovies.CreditEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case POSTER_ENTRY:
                rowsUpdated = db.update(ContractMovies.PosterEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(ERROR_UNKNOWN_URI + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    /**
     * A Class to build the database
     */
    private class DatabaseHelper extends SQLiteOpenHelper {

        // Constant
        private static final int DATABASE_VERSION = 1;
        private static final String LOG_TAG = "sql-statements";
        public static final String DATABASE_NAME = "themoviedb.db";

        // Constructor
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Create new table
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {

            final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                    ContractMovies.MovieEntry.TABLE_NAME + " (" +
                    ContractMovies.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                    ContractMovies.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                    ContractMovies.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                    ContractMovies.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT, " +
                    ContractMovies.MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                    ContractMovies.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                    ContractMovies.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                    ContractMovies.MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                    ContractMovies.MovieEntry.COLUMN_POPULARITY + " INTEGER, " +
                    ContractMovies.MovieEntry.COLUMN_AVERAGE_VOTE + " DOUBLE, " +
                    ContractMovies.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                    ContractMovies.MovieEntry.COLUMN_POSTING_TIME + " BIGINT, " +
                    ContractMovies.MovieEntry.COLUMN_SORT_GROUP + " TEXT)";

            final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE " +
                    ContractMovies.GenreEntry.TABLE_NAME + " (" +
                    ContractMovies.GenreEntry._ID + " INTEGER PRIMARY KEY, " +
                    ContractMovies.GenreEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                    ContractMovies.GenreEntry.COLUMN_GENRE + " TEXT, " +
                    ContractMovies.GenreEntry.COLUMN_POSTING_TIME + " BIGINT, " +
                    ContractMovies.GenreEntry.COLUMN_SORT_GROUP + " TEXT)";

            final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " +
                    ContractMovies.TrailerEntry.TABLE_NAME + " (" +
                    ContractMovies.TrailerEntry._ID + " INTEGER, " +
                    ContractMovies.TrailerEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                    ContractMovies.TrailerEntry.COLUMN_TRAILER + " TEXT, " +
                    ContractMovies.TrailerEntry.COLUMN_POSTING_TIME + " BIGINT) ";

            final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " +
                    ContractMovies.ReviewEntry.TABLE_NAME + " (" +
                    ContractMovies.ReviewEntry._ID + " INTEGER, " +
                    ContractMovies.ReviewEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                    ContractMovies.ReviewEntry.COLUMN_USER_NAME + " TEXT, " +
                    ContractMovies.ReviewEntry.COLUMN_USER_COMMENT + " TEXT, " +
                    ContractMovies.ReviewEntry.COLUMN_POSTING_TIME + " BIGINT) ";

            final String SQL_CREATE_CREDIT_TABLE = "CREATE TABLE " +
                    ContractMovies.CreditEntry.TABLE_NAME + " (" +
                    ContractMovies.CreditEntry._ID + " INTEGER, " +
                    ContractMovies.CreditEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                    ContractMovies.CreditEntry.COLUMN_CAST_ID + " TEXT, " +
                    ContractMovies.CreditEntry.COLUMN_CHARACTER + " TEXT, " +
                    ContractMovies.CreditEntry.COLUMN_NAME + " TEXT, " +
                    ContractMovies.CreditEntry.COLUMN_PROFILE_PATH + " TEXT, " +
                    ContractMovies.CreditEntry.COLUMN_POSTING_TIME + " BIGINT) ";

            final String SQL_CREATE_POSTER_TABLER = "CREATE TABLE " +
                    ContractMovies.PosterEntry.TABLE_NAME + " (" +
                    ContractMovies.PosterEntry._ID + " INTEGER, " +
                    ContractMovies.PosterEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                    ContractMovies.PosterEntry.COLUMN_FILE_PATH + " TEXT, " +
                    ContractMovies.PosterEntry.COLUMN_POSTING_TIME + " BIGINT) ";

            db.execSQL(SQL_CREATE_MOVIE_TABLE);
            db.execSQL(SQL_CREATE_GENRE_TABLE);
            db.execSQL(SQL_CREATE_TRAILER_TABLE);
            db.execSQL(SQL_CREATE_REVIEW_TABLE);
            db.execSQL(SQL_CREATE_CREDIT_TABLE);
            db.execSQL(SQL_CREATE_POSTER_TABLER);

        }

        /**
         * Upgrade
         * @param db
         * @param i
         * @param i1
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {

            db.execSQL("DROP TABLE IF EXISTS " + ContractMovies.ReviewEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ContractMovies.TrailerEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ContractMovies.GenreEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ContractMovies.MovieEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ContractMovies.CreditEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ContractMovies.CreditEntry.TABLE_NAME);

            onCreate(db);
        }
    }

}
