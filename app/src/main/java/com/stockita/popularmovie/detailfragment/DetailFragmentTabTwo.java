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
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stockita.popularmovie.R;
import com.stockita.popularmovie.activity.NewDetailActivity;
import com.stockita.popularmovie.data.ModelPoster;
import com.stockita.popularmovie.detailadapter.PostersDetailViewAdapter;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.utility.FetchAndParseDetail;
import com.stockita.popularmovie.utility.Utilities;

import java.util.ArrayList;

public class DetailFragmentTabTwo extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constant
    private static final String LOG_TAG = DetailFragmentTabTwo.class.getSimpleName();
    private static final int LOADER_IDE_POSTERS = 1;

    // Member variable
    private Context mContext;
    private String mMovieId;

    private PostersDetailViewAdapter mPosterDetailViewAdapter;

    // Empty constructor
    public DetailFragmentTabTwo(){}

    /**
     *  Saving instance state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(NewDetailActivity.KEY_MOVIE_ID, mMovieId);
        super.onSaveInstanceState(outState);
    }

    /**
     * on Create
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize context;
        mContext = getActivity();

        // savedInstanceState restored
        if (savedInstanceState != null) {
            mMovieId = savedInstanceState.getString(NewDetailActivity.KEY_MOVIE_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get the data that passed by the Recycler View Fragment.
        if (savedInstanceState == null) {

            mMovieId = Utilities.getMovieId(mContext, NewDetailActivity.KEY_MOVIE_ID);

            ContentResolver contentResolver = mContext.getContentResolver();

            // Poster data
            Cursor cursorPoster = contentResolver.query(ContractMovies.PosterEntry.CONTENT_URI,
                    null,
                    ContractMovies.PosterEntry.COLUMN_MOVIE_ID + "=?",
                    new String[]{mMovieId},
                    null);


            if (cursorPoster != null) {
                if (cursorPoster.getCount() == 0) {
                    Intent i = new Intent(mContext, FetchAndParseDetail.class);
                    i.putExtra("keyMovieId", mMovieId);
                    i.putExtra("keyOrigin", "3");
                    mContext.startService(i);

                }
            }
            cursorPoster.close();
        }

        // Initialize the view.
        View rootView = inflater.inflate(R.layout.fragment_detail_posters, container, false);

        // Initialize the view holder
        final ViewHolderDetailFragmentOther holder = new ViewHolderDetailFragmentOther(rootView);

        /* **** RecyclerView for Movie Posters **** */
        // Initialize Adapter
        mPosterDetailViewAdapter = new PostersDetailViewAdapter(mContext);
        // Initialize layoutManager
        StaggeredGridLayoutManager mStaggeredGridLayoutManagerPoster =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // Set the layout manager
        holder.recyclerViewPosterCard.setLayoutManager(mStaggeredGridLayoutManagerPoster);
        // Set the adapter
        holder.recyclerViewPosterCard.setAdapter(mPosterDetailViewAdapter);

        return rootView;
    }


    /* Data handling below here */

    /**
     * Reload the cursors

     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Start the loader, and initialize them.
        getLoaderManager().initLoader(LOADER_IDE_POSTERS, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Return new cursor with the following query parameters.
        CursorLoader loader = null;

        switch (id) {
            case LOADER_IDE_POSTERS:
                loader = new CursorLoader(mContext,
                        ContractMovies.PosterEntry.CONTENT_URI,
                        null,
                        ContractMovies.PosterEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{mMovieId},
                        null);
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // A switch-case is useful when dealing with multiple Loader/IDs
        switch (loader.getId()) {
            case LOADER_IDE_POSTERS:

                data.moveToFirst();

                int posterDatasize = data.getCount();

                ArrayList<ModelPoster> modelPosterArrayList = new ArrayList<>();

                for (int i=0; i < posterDatasize; i++) {
                    ModelPoster modelPoster = new ModelPoster();
                    modelPoster.set_id(data.getInt(ContractMovies.PosterEntry.INDEX_COL_ID));
                    modelPoster.setMovieId(data.getString(ContractMovies.PosterEntry.INDEX_COL_MOVIE_ID));
                    modelPoster.setPosterPath(data.getString(ContractMovies.PosterEntry.INDEX_COL_FILE_PATH));
                    modelPoster.setPostingTime(data.getLong(ContractMovies.PosterEntry.INDEX_POSTING_TIME));

                    modelPosterArrayList.add(modelPoster);
                    data.moveToNext();
                }

                mPosterDetailViewAdapter.swapCursor(modelPosterArrayList);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // For whatever reason, the Loader's data is now not available.
        // Remove any reference to the old data by replacing it with
        // a null Cursor.
        loader = null;
    }

    /**
     * Inner class for ViewHolder
     */
    public class ViewHolderDetailFragmentOther {
        RecyclerView recyclerViewPosterCard;

        public ViewHolderDetailFragmentOther(View view) {
            recyclerViewPosterCard = (RecyclerView) view.findViewById(R.id.fragmentDetailOtherPosters);

        }

    }
}
