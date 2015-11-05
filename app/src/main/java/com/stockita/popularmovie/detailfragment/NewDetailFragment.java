package com.stockita.popularmovie.detailfragment;

import android.database.Cursor;

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

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stockita.popularmovie.R;
import com.stockita.popularmovie.activity.NewDetailActivity;
import com.stockita.popularmovie.data.ModelCredit;
import com.stockita.popularmovie.data.ModelMovie;
import com.stockita.popularmovie.data.ModelTrailer;
import com.stockita.popularmovie.detailadapter.NewDetailAdapter;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.utility.FetchAndParseDetail;
import com.stockita.popularmovie.utility.Utilities;

import java.util.ArrayList;

public class NewDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constant
    private static final String LOG_TAG = NewDetailFragment.class.getSimpleName();
    public static final int LOADER_IDE_MOVIE_DETAIL = 1;
    public static final int LOADER_IDE_CAST = 3;
    public static final int LOADER_IDE_TRAILERS = 4;

    // Member variable
    private Context mContext;
    private String mMovieId;
    private String mSortGroup;
    private NewDetailAdapter mDetailAdapter;


    // Empty constructor
    public NewDetailFragment() {
    }

    /**
     * To survive screen/configuration changes
     *
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(NewDetailActivity.KEY_MOVIE_ID, mMovieId);
        outState.putString(NewDetailActivity.KEY_SORT_GROUP, mSortGroup);
        super.onSaveInstanceState(outState);
    }

    /**
     * onCreate
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize context;
        mContext = getActivity();

        // savedInstanceState restored
        if (savedInstanceState != null) {
            mMovieId = savedInstanceState.getString(NewDetailActivity.KEY_MOVIE_ID);
            mSortGroup = savedInstanceState.getString(NewDetailActivity.KEY_SORT_GROUP);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get the data that passed by the Recycler View Fragment.
        if (savedInstanceState == null) {

            // Data stored in SharedPreferences
            mMovieId = Utilities.getMovieId(mContext, NewDetailActivity.KEY_MOVIE_ID);
            mSortGroup = Utilities.getMovieId(mContext, NewDetailActivity.KEY_SORT_GROUP);

            /**
             * What the code below does, is they will check if the detail is already available in database
             * or not, before fetching detail data from the web.
             */
            ContentResolver contentResolver = mContext.getContentResolver();

            // Credit/Cast data
            Cursor cursorCredit = null;
            try {
                cursorCredit = contentResolver.query(ContractMovies.CreditEntry.CONTENT_URI,
                        null,
                        ContractMovies.CreditEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{mMovieId},
                        null);


                if (cursorCredit != null) {
                    if (cursorCredit.getCount() == 0) {
                        Intent i = new Intent(mContext, FetchAndParseDetail.class);
                        i.putExtra("keyMovieId", mMovieId);
                        i.putExtra("keyOrigin", "1");
                        mContext.startService(i);
                    }
                }
            } finally {
                cursorCredit.close();
            }

            // Trailer data
            Cursor cursorTrailer = null;
            try {
                cursorTrailer = contentResolver.query(ContractMovies.TrailerEntry.CONTENT_URI,
                        null,
                        ContractMovies.TrailerEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{mMovieId},
                        null);


                if (cursorTrailer != null) {
                    if (cursorTrailer.getCount() == 0) {
                        Intent i = new Intent(mContext, FetchAndParseDetail.class);
                        i.putExtra("keyMovieId", mMovieId);
                        i.putExtra("keyOrigin", "2");
                        mContext.startService(i);

                    }
                }
            } finally {
                cursorTrailer.close();
            }
        } // End if (savedInstanceState == null)

        // Initialize the view.
        View rootView = inflater.inflate(R.layout.new_fragment_detail, container, false);

        // Initialize the adapter
        mDetailAdapter = new NewDetailAdapter(mContext);

        // Initialize the recycler view
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.newDetailList);

        // Initialize the LayoutManager
        StaggeredGridLayoutManager mStaggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        // Set the layoutManager
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        // Set the adapter
        mRecyclerView.setAdapter(mDetailAdapter);

        return rootView;

    }

    // Data handling...
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Start the loader, and initialize them.
        getLoaderManager().initLoader(LOADER_IDE_MOVIE_DETAIL, null, this);
        getLoaderManager().initLoader(LOADER_IDE_CAST, null, this);
        getLoaderManager().initLoader(LOADER_IDE_TRAILERS, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Return new cursor with the following query parameters.
        CursorLoader loader = null;

        switch (id) {
            case LOADER_IDE_MOVIE_DETAIL:
                // The cursor for MovieEntry
                loader = new CursorLoader(mContext,
                        ContractMovies.MovieEntry.CONTENT_URI,
                        null,
                        ContractMovies.MovieEntry.COLUMN_MOVIE_ID + "=?" + " AND " + ContractMovies.MovieEntry.COLUMN_SORT_GROUP + "=?",
                        new String[]{mMovieId, mSortGroup},
                        null);
                break;

            case LOADER_IDE_CAST:
                // The cursor for CreditEntry
                loader = new CursorLoader(mContext,
                        ContractMovies.CreditEntry.CONTENT_URI,
                        null,
                        ContractMovies.CreditEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{mMovieId},
                        null);
                break;

            case LOADER_IDE_TRAILERS:
                // The cursor for TrailerEntry
                loader = new CursorLoader(mContext,
                        ContractMovies.TrailerEntry.CONTENT_URI,
                        null,
                        ContractMovies.TrailerEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{mMovieId},
                        null);
                break;
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Now we want to pack the data into an Array instead the cursor that
        // we will send to the adapter, however this is experiment for performance.

        switch (loader.getId()) {
            case LOADER_IDE_MOVIE_DETAIL:

                data.moveToFirst();

                ArrayList<ModelMovie> modelMovieArrayList = new ArrayList<>();

                int modelMovieSize = data.getCount();

                for (int i = 0; i < modelMovieSize; i++) {
                    ModelMovie modelMovie = new ModelMovie();
                    modelMovie.set_id(data.getInt(ContractMovies.MovieEntry.INDEX_COL_ID));
                    modelMovie.setMovieId(data.getString(ContractMovies.MovieEntry.INDEX_COL_MOVIE_ID));
                    modelMovie.setBackdropPath(data.getString(ContractMovies.MovieEntry.INDEX_COL_BACKDROP_PATH));
                    modelMovie.setMovieTitle(data.getString(ContractMovies.MovieEntry.INDEX_COL_MOVIE_TITLE));
                    modelMovie.setReleaseDate(data.getString(ContractMovies.MovieEntry.INDEX_COL_RELEASE_DATE));
                    modelMovie.setPosterPath(data.getString(ContractMovies.MovieEntry.INDEX_COL_POSTER_PATH));
                    modelMovie.setMoviePopularity(data.getDouble(ContractMovies.MovieEntry.INDEX_COL_POPULARITY));
                    modelMovie.setOverview(data.getString(ContractMovies.MovieEntry.INDEX_COL_OVERVIEW));
                    modelMovie.setVoteCount(data.getInt(ContractMovies.MovieEntry.INDEX_COL_VOTE_COUNT));
                    modelMovie.setPostingTime(data.getLong(ContractMovies.MovieEntry.INDEX_COL_POSTING_TIME));
                    modelMovie.setSortGroup(data.getString(ContractMovies.MovieEntry.INDEX_COL_SORT_GROUP));

                    modelMovieArrayList.add(modelMovie);
                    data.moveToNext();
                }

                mDetailAdapter.swapData(modelMovieArrayList, LOADER_IDE_MOVIE_DETAIL);
                break;

            case LOADER_IDE_CAST:

                data.moveToFirst();

                ArrayList<ModelCredit> modelCreditArrayList = new ArrayList<>();

                int modelCreditSize = data.getCount();

                for (int i = 0; i < modelCreditSize; i++) {
                    ModelCredit modelCredit = new ModelCredit();
                    modelCredit.set_id(data.getInt(ContractMovies.CreditEntry.INDEX_COL_ID));
                    modelCredit.setMovieId(data.getString(ContractMovies.CreditEntry.INDEX_COL_MOVIE_ID));
                    modelCredit.setPlayerName(data.getString(ContractMovies.CreditEntry.INDEX_NAME));
                    modelCredit.setProfilePath(data.getString(ContractMovies.CreditEntry.INDEX_PROFILE_PATH));
                    modelCredit.setPostingTime(data.getLong(ContractMovies.CreditEntry.INDEX_COL_POSTING_TIME));

                    modelCreditArrayList.add(modelCredit);
                    data.moveToNext();
                }

                mDetailAdapter.swapData(modelCreditArrayList, LOADER_IDE_CAST);
                break;

            case LOADER_IDE_TRAILERS:

                data.moveToFirst();

                ArrayList<ModelTrailer> modelTrailerArrayList = new ArrayList<>();

                int modelTrailerSize = data.getCount();

                for (int i = 0; i < modelTrailerSize; i++) {
                    ModelTrailer modelTrailer = new ModelTrailer();
                    modelTrailer.set_id(data.getInt(ContractMovies.TrailerEntry.INDEX_COL_ID));
                    modelTrailer.setMovieId(data.getString(ContractMovies.TrailerEntry.INDEX_COL_MOVIE_ID));
                    modelTrailer.setTrailer(data.getString(ContractMovies.TrailerEntry.INDEX_COL_TRAILER));
                    modelTrailer.setPostingTime(data.getLong(ContractMovies.TrailerEntry.INDEX_COL_POSTING_TIME));

                    modelTrailerArrayList.add(modelTrailer);
                    data.moveToNext();
                }

                mDetailAdapter.swapData(modelTrailerArrayList, LOADER_IDE_TRAILERS);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }
}
