package com.stockita.popularmovie.detailfragment;

import android.annotation.TargetApi;
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
import com.stockita.popularmovie.data.ModelReview;
import com.stockita.popularmovie.detailadapter.ReviewsDetailViewAdapter;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.utility.FetchAndParseDetail;
import com.stockita.popularmovie.utility.Utilities;

import java.util.ArrayList;

/**
 * Created by hishmadabubakaralamudi on 10/23/15.
 */
public class DetailFragmentTabThree extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constant
    private static final String LOG_TAG = DetailFragmentTabThree.class.getSimpleName();
    private static final int LOADER_IDE_REVIEWS = 1;

    // Member variable
    private Context mContext;
    private String mMovieId;
    private ReviewsDetailViewAdapter mReviewDetailViewAdapter;

    // Empty constructor
    public DetailFragmentTabThree() {
    }

    /**
     * Saving instance state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(NewDetailActivity.KEY_MOVIE_ID, mMovieId);
        super.onSaveInstanceState(outState);
    }

    /**
     * on Create
     *
     * @param savedInstanceState
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize context;
        mContext = getContext();

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

            // Review data
            Cursor cursorReview = contentResolver.query(ContractMovies.ReviewEntry.CONTENT_URI,
                    null,
                    ContractMovies.ReviewEntry.COLUMN_MOVIE_ID + "=?",
                    new String[]{mMovieId},
                    null);


            if (cursorReview != null) {
                if (cursorReview.getCount() == 0)  {
                    Intent i = new Intent(mContext, FetchAndParseDetail.class);
                    i.putExtra("keyMovieId", mMovieId);
                    i.putExtra("keyOrigin", "4");
                    mContext.startService(i);
                }
            }

            cursorReview.close();

        }

        // Initialize the view.
        View rootView = inflater.inflate(R.layout.fragment_detail_reviews, container, false);

        // Initialize the view holder
        final ViewHolderDetailFragmentReviews holder = new ViewHolderDetailFragmentReviews(rootView);

        /* **** RecyclerView for Cast **** */
        // Initialize Adapter
        mReviewDetailViewAdapter = new ReviewsDetailViewAdapter(mContext);
        // Initialize layoutManager
        StaggeredGridLayoutManager mStaggeredGridLayoutManagerReview =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // Set the layout manager
        holder.recyclerViewReviewCard.setLayoutManager(mStaggeredGridLayoutManagerReview);
        // Set the adapter.
        holder.recyclerViewReviewCard.setAdapter(mReviewDetailViewAdapter);

        return rootView;


    }

       /* Data handling below here */

    /**
     * Reload the cursors
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Start the loader, and initialize them.
        getLoaderManager().initLoader(LOADER_IDE_REVIEWS, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Return new cursor with the following query parameters.
        CursorLoader loader = null;

        switch (id) {
            case LOADER_IDE_REVIEWS:
                loader = new CursorLoader(mContext,
                        ContractMovies.ReviewEntry.CONTENT_URI,
                        null,
                        ContractMovies.ReviewEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{mMovieId},
                        null);
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // A switch-case is useful when dealing with multiple Loader/IDs
        switch (loader.getId()) {
            case LOADER_IDE_REVIEWS:

                data.moveToFirst();

                int reviewDataSize = data.getCount();

                ArrayList<ModelReview> modelReviewArrayList = new ArrayList<>();

                for (int i=0; i < reviewDataSize; i++) {
                    ModelReview modelReview = new ModelReview();
                    modelReview.set_id(data.getInt(ContractMovies.ReviewEntry.INDEX_COL_ID));
                    modelReview.setMovieId(data.getString(ContractMovies.ReviewEntry.INDEX_COL_MOVIE_ID));
                    modelReview.setUserName(data.getString(ContractMovies.ReviewEntry.INDEX_COL_USER_NAME));
                    modelReview.setUserComment(data.getString(ContractMovies.ReviewEntry.INDEX_COL_USER_COMMENT));
                    modelReview.setPostingTime(data.getLong(ContractMovies.ReviewEntry.INDEX_COL_POSTING_TIME));

                    modelReviewArrayList.add(modelReview);
                    data.moveToNext();
                }

                mReviewDetailViewAdapter.swapCursor(modelReviewArrayList);
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
    public class ViewHolderDetailFragmentReviews {
        RecyclerView recyclerViewReviewCard;

        public ViewHolderDetailFragmentReviews(View view) {
            recyclerViewReviewCard = (RecyclerView) view.findViewById(R.id.fragmentDetailOtherReviews);

        }

    }

}
