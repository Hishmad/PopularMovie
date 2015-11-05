package com.stockita.popularmovie.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stockita.popularmovie.R;
import com.stockita.popularmovie.adapters.RatingRecyclerViewAdapter;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.data.ModelMovie;
import com.stockita.popularmovie.interfaces.CallThis;

import java.util.ArrayList;

/**
 * Created by hishmadabubakaralamudi on 10/16/15.
 */
public class RecyclerViewFragmentMovieRating extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constant
    public static final String LOG_TAG = RecyclerViewFragmentMoviePopular.class.getSimpleName();
    private static final int LOADER_IDE_ONE = 1;
    private static final String SORT_GROUP = "rating";

    // Member variable.
    private RatingRecyclerViewAdapter mRatingAdapter;

    // Empty constructor
    public RecyclerViewFragmentMovieRating() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Start the loader, and initialize them.
        getLoaderManager().initLoader(LOADER_IDE_ONE, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initialize the view
        View rootView = inflater.inflate(R.layout.recycler_view_movie_fragment, container, false);

        // Initialize the Adapter
        mRatingAdapter = new RatingRecyclerViewAdapter(getActivity());

        // Initialize the Recycler view
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);

        // Initialize the LayoutManager
        StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);

        // Set the LayoutManager
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        // Set the Adapter
        mRecyclerView.setAdapter(mRatingAdapter);

        // Set the click listener
        mRatingAdapter.setOnItemClickListenerRating(onItemClickListener);

        return rootView;

    }

    /**
     * Custom click listener
     * When the user click item in the list, this will be executed.
     */
    public RatingRecyclerViewAdapter.OnItemClickListenerRating onItemClickListener =
            new RatingRecyclerViewAdapter.OnItemClickListenerRating() {
                @Override
                public void onItemClick(View view, int position, String movieId, String sortGroup) {
                    // Pass the data to NewDetailFragment via MainActivity Callback interface (CallThis)
                    ((CallThis) getActivity()).onItemSelectedMovieId(movieId, sortGroup);
                }
            };


    /**
     * Instantiate CursorLoader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Return new cursor with the following query parameters.

        CursorLoader loader = null;

        switch (id) {
            case LOADER_IDE_ONE:
                loader = new CursorLoader(getActivity(),
                        ContractMovies.MovieEntry.CONTENT_URI,
                        null,
                        ContractMovies.MovieEntry.COLUMN_SORT_GROUP + "=?",
                        new String[]{SORT_GROUP},
                        ContractMovies.MovieEntry.COLUMN_VOTE_COUNT + " DESC");
                break;
        }

        return loader;
    }

    /**
     * This is where the loader finished loading the data into the memory,
     * now send the data to the adapter, by invoking swapCursor() method.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // A switch-case is useful when dealing with multiple Loader/IDs
        switch (loader.getId()) {
            case LOADER_IDE_ONE:
                // The asynchronous load is complete and the data
                // is now available for use. Only now we can associate
                // the required Cursor with the adapter.

                // Set the cursor to first position.
                data.moveToFirst();

                // Get the number of rows.
                int dataCount = data.getCount();

                // Instantiate new ArrayList<ModelMovie>
                ArrayList<ModelMovie> dataModelMovie = new ArrayList<>();

                // Add each row into an array element.
                for (int i=0; i < dataCount; i++) {
                    ModelMovie modelMovie = new ModelMovie();
                    modelMovie.set_id(data.getInt(ContractMovies.MovieEntry.INDEX_COL_ID));
                    modelMovie.setMovieId(data.getString(ContractMovies.MovieEntry.INDEX_COL_MOVIE_ID));
                    modelMovie.setMovieTitle(data.getString(ContractMovies.MovieEntry.INDEX_COL_MOVIE_TITLE));
                    modelMovie.setPosterPath(data.getString(ContractMovies.MovieEntry.INDEX_COL_POSTER_PATH));
                    modelMovie.setReleaseDate(data.getString(ContractMovies.MovieEntry.INDEX_COL_RELEASE_DATE));
                    modelMovie.setVoteCount(data.getInt(ContractMovies.MovieEntry.INDEX_COL_VOTE_COUNT));
                    modelMovie.setBackdropPath(data.getString(ContractMovies.MovieEntry.INDEX_COL_BACKDROP_PATH));
                    modelMovie.setOverview(data.getString(ContractMovies.MovieEntry.INDEX_COL_OVERVIEW));
                    modelMovie.setSortGroup(data.getString(ContractMovies.MovieEntry.INDEX_COL_SORT_GROUP));
                    dataModelMovie.add(modelMovie);
                    data.moveToNext();
                };

                // Pass the ArrayList to the Adapter.
                mRatingAdapter.swapCursor(dataModelMovie);
                break;
        }
    }

    /**
     * Make sure it is null, for now.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // For whatever reason, the Loader's data is now not available.
        // Remove any reference to the old data by replacing it with
        // a null Cursor.
        loader = null;
    }

}
