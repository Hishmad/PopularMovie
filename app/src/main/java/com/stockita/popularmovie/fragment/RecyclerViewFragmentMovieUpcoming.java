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
import com.stockita.popularmovie.adapters.UpcomingRecyclerViewAdapter;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.data.ModelMovie;
import com.stockita.popularmovie.interfaces.CallThis;


import java.util.ArrayList;

/**
 * Created by hishmadabubakaralamudi on 10/16/15.
 */
public class RecyclerViewFragmentMovieUpcoming extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constant...
    private static final String LOG_TAG = RecyclerViewFragmentMovieUpcoming.class.getSimpleName();

    // This constant is the KEY to load the Cursor with MovieEntry data.
    private static final int LOADER_IDE_ONE = 1;

    // This constant is the value of the sort_group column.
    private static final String SORT_GROUP = "upcoming";

    // The Adapter object.
    private UpcomingRecyclerViewAdapter mUpcomingAdapter;

    // Empty constructor.
    public RecyclerViewFragmentMovieUpcoming() {
    }

    /**
     * Here we fire the loaders.
     */
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

        // Initialize the ViewHolder instance
        ViewHolder holder = new ViewHolder(rootView);

        // Initialize the Adapter
        mUpcomingAdapter = new UpcomingRecyclerViewAdapter(getActivity());

        // Initialize the LayoutManager
        StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);

        // Set the LayoutManager
        holder.hRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        // Set the Adapter
        holder.hRecyclerView.setAdapter(mUpcomingAdapter);

        // Set the click listener
        mUpcomingAdapter.setOnItemClickListenerUpcoming(onItemClickListener);

        return rootView;

    }

    /**
     * Custom click listener
     * When the user click item in the list, this will be executed.
     */
    public UpcomingRecyclerViewAdapter.OnItemClickListenerUpcoming onItemClickListener =
            new UpcomingRecyclerViewAdapter.OnItemClickListenerUpcoming() {
                @Override
                public void onItemClick(View view,
                                        int position,
                                        String movieId,
                                        String sortGroup) {

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
                        null);
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
                for (int i=0; i < data.getCount(); i++) {
                    ModelMovie modelMovie = new ModelMovie();
                    modelMovie.set_id(data.getInt(ContractMovies.MovieEntry.INDEX_COL_ID));
                    modelMovie.setMovieId(data.getString(ContractMovies.MovieEntry.INDEX_COL_MOVIE_ID));
                    modelMovie.setMovieTitle(data.getString(ContractMovies.MovieEntry.INDEX_COL_MOVIE_TITLE));
                    modelMovie.setPosterPath(data.getString(ContractMovies.MovieEntry.INDEX_COL_POSTER_PATH));
                    modelMovie.setReleaseDate(data.getString(ContractMovies.MovieEntry.INDEX_COL_RELEASE_DATE));
                    modelMovie.setMoviePopularity(data.getDouble(ContractMovies.MovieEntry.INDEX_COL_POPULARITY));
                    modelMovie.setBackdropPath(data.getString(ContractMovies.MovieEntry.INDEX_COL_BACKDROP_PATH));
                    modelMovie.setOverview(data.getString(ContractMovies.MovieEntry.INDEX_COL_OVERVIEW));
                    modelMovie.setSortGroup(data.getString(ContractMovies.MovieEntry.INDEX_COL_SORT_GROUP));
                    dataModelMovie.add(modelMovie);
                    data.moveToNext();
                };

                // Pass the arrayList to the adapter.
                mUpcomingAdapter.swapCursor(dataModelMovie);
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

    // Helper class for ViewHolder.
    public class ViewHolder {
        // The RecyclerView object
        public RecyclerView hRecyclerView;

        public ViewHolder(View viewItem) {
            hRecyclerView = (RecyclerView) viewItem.findViewById(R.id.list);
        }
    }

}
