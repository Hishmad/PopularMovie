package com.stockita.popularmovie.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.LoaderManager;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stockita.popularmovie.R;
import com.stockita.popularmovie.adapters.SearchRecyclerViewAdapter;
import com.stockita.popularmovie.adapters.UpcomingRecyclerViewAdapter;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.data.ModelMovie;
import com.stockita.popularmovie.interfaces.CallThis;
import com.stockita.popularmovie.utility.FetchAndParse;
import com.stockita.popularmovie.utility.Utilities;

import java.util.ArrayList;

/**
 * Created by hishmadabubakaralamudi on 10/17/15.
 */
public class RecyclerViewFragmentMovieSearch extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constant...
    public static final String LOG_TAG = RecyclerViewFragmentMovieSearch.class.getSimpleName();

    // This constant is the KEY to load the Cursor with MovieEntry data.
    public static final int LOADER_IDE_ONE = 1;

    // This constant is the value of the sort_group column.
    public static final String SORT_GROUP = "search";

    // The Loader object.
    private SearchRecyclerViewAdapter mSearchAdapter;

    // The CallBack interface object, to pass data from this fragment to the MainActivity.java
    private CallThis mActivity;

    // Empty constructor.
    public RecyclerViewFragmentMovieSearch() {
    }

    /**
     * Here we fire the loaders.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Start the loader, and initialize them.
        getLoaderManager().initLoader(LOADER_IDE_ONE, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * We attach the CallBack interface instance to pass data to the MainActivity.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (CallThis) context;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initialize the view
        View rootView = inflater.inflate(R.layout.recycler_view_search_fragment, container, false);

        // Initialize ViewHolder
        final ViewHolder holder = new ViewHolder(rootView);

        // Search for a movie
        // Get the user input from the EditText
        if (holder.hSearchEditText != null) {

            // When user click this button, conduct HTTP request for a search and fetch the data.
            holder.hSearchButtonIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (!Utilities.isNetworkAvailable(getContext())) {
                            Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final String searchForAMovie = holder.hSearchEditText.getText().toString();
                        holder.hSearchEditText.setText("");
                        holder.hSearchEditText.setHint("Search for a movie");

                        // Hide the keyboard.
                        Utilities.hideKeyboard(getActivity());

                        // Fetch the data
                        Intent i = new Intent(getActivity(), FetchAndParse.class);
                        i.putExtra(SORT_GROUP, searchForAMovie);
                        getActivity().startService(i);

                    }
                }
            });
        }

        /**
         * RecyclerView below here...
         */

        // Initialize the Adapter
        mSearchAdapter = new SearchRecyclerViewAdapter(getContext());

        // Initialize the LayoutManager
        StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);

        // Set the LayoutManager
        holder.hRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        // Set the Adapter
        holder.hRecyclerView.setAdapter(mSearchAdapter);

        // Set the click listener
        mSearchAdapter.setOnItemClickListenerSearch(onItemClickListener);


        return rootView;

    }

    /**
     * Custom click listener
     * When the user click item in the list, this will be executed.
     */
    public SearchRecyclerViewAdapter.OnItemClickListenerSearch onItemClickListener =
            new SearchRecyclerViewAdapter.OnItemClickListenerSearch() {
                @Override
                public void onItemClick(View view,
                                        int position,
                                        String movieId,
                                        String movieTitle,
                                        String releaseDate,
                                        String posterPath,
                                        String grade,
                                        String genre,
                                        String backDrop,
                                        String overview,
                                        String sortGroup) {

                    // Pass the data to NewDetailFragment via MainActivity Callback interface (CallThis)
                    mActivity.onItemSelectedMovieId(movieId, movieTitle, releaseDate, posterPath, grade, genre, backDrop, overview, sortGroup);
                }
            };


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
                        null); // sorted
                break;
        }

        return loader;
    }

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
                for (int i = 0; i < data.getCount(); i++) {
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

                // Pass the ArrayList to the adapter.
                mSearchAdapter.swapCursor(dataModelMovie);
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
     * Hide the keyboard
     */
    @Override
    public void onPause() {
        super.onPause();
        Utilities.hideKeyboard(getActivity());
    }

    // Helper class for ViewHolder.
    public class ViewHolder {

        // The RecyclerView object.
        public RecyclerView hRecyclerView;
        // The EditText object.
        public EditText hSearchEditText;
        // The ImageBUtton object.
        public ImageButton hSearchButtonIcon;

        // Constructor
        public ViewHolder(View viewItem) {
            hRecyclerView = (RecyclerView) viewItem.findViewById(R.id.listSearch);
            hSearchEditText = (EditText) viewItem.findViewById(R.id.searchEditText);
            hSearchButtonIcon = (ImageButton) viewItem.findViewById(R.id.searchButton);
        }

    }
}
