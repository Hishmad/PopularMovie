package com.stockita.popularmovie.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stockita.popularmovie.R;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.data.ModelMovie;
import com.stockita.popularmovie.utility.Utilities;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by hishmadabubakaralamudi on 10/17/15.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolderSearch> {

    // Constant
    private static final String LOG_TAG = SearchRecyclerViewAdapter.class.getSimpleName();

    // This constant is the value of the sort_group column.
    public static final String SORT_GROUP = "search";

    // Member variables
    private Context mContext;
    private OnItemClickListenerSearch mItemClickListener;
    private ArrayList<ModelMovie> mListModelMovie;

    // Constructor
    public SearchRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    /**
     * Get the data
     */
    public void swapCursor(ArrayList<ModelMovie> data) {
        mListModelMovie = data;

        // Notify back
        notifyDataSetChanged();
    }

    /**
     * Mapping the position with table's row _id
     */
    @Override
    public long getItemId(int position) {
        return mListModelMovie.get(position).get_id();
    }

    /**
     * Return the count other wise if cursor null return 0;
     */
    @Override
    public int getItemCount() {
        return mListModelMovie != null ? mListModelMovie.size() : 0;
    }

    /**
     * Setup the view and inflate the layout xml
     */
    @Override
    public ViewHolderSearch onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row_movies, parent, false);
        return new ViewHolderSearch(view);
    }

    /**
     * Here we go.
     */
    @Override
    public void onBindViewHolder(final ViewHolderSearch holder, final int position) {

        // Local variables
        String lMovieId = null;


        // MovieEntry
        if (mListModelMovie != null) {

            // Movie Title
            String lMovieTitle = mListModelMovie.get(position).getMovieTitle();
            holder.setMovieTitle(lMovieTitle);

            // Poster path
            String lMoviePoster = mListModelMovie.get(position).getPosterPath();
            holder.setMoviePoster(mContext, lMoviePoster);

            // Release date
            String lReleaseDate = mListModelMovie.get(position).getReleaseDate();
            holder.setReleaseDate(mContext.getString(R.string.release_date) + lReleaseDate);

            // Popularity's
            double popularities = mListModelMovie.get(position).getMoviePopularity();
            String popularitiesFormatted = String.format(Locale.ENGLISH, "%.2f", popularities);
            String lPopularitiesAfterFormat = (mContext.getString(R.string.popularities_grade) + popularitiesFormatted);
            holder.setPopularities(lPopularitiesAfterFormat);

            // Data to pass around
            lMovieId = mListModelMovie.get(position).getMovieId();
            holder.hMovieID = lMovieId;
            holder.hSortGroup = mListModelMovie.get(position).getSortGroup();

        }

        // Genre
        final String finalLMovieId = lMovieId;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                StringBuilder builder = new StringBuilder();
                ContentResolver data = mContext.getContentResolver();
                Cursor lCursor = null;
                try {
                    lCursor = data.query(ContractMovies.GenreEntry.CONTENT_URI, null,
                            ContractMovies.GenreEntry.COLUMN_MOVIE_ID + "=?" +
                            " AND " + ContractMovies.GenreEntry.COLUMN_SORT_GROUP + "=?",
                            new String[]{finalLMovieId, SORT_GROUP}, null);

                    lCursor.moveToFirst();
                    for (int ii = 0; ii < lCursor.getCount(); ii++) {
                        if (lCursor.getString(ContractMovies.GenreEntry.INDEX_COL_MOVIE_ID).equals(finalLMovieId)) {
                            String genre = lCursor.getString(ContractMovies.GenreEntry.INDEX_COL_GENRE);
                            builder.append(genre);
                            builder.append(" ");
                        }
                        lCursor.moveToNext();
                    }

                } finally {
                    if (lCursor != null) lCursor.close();
                }
                return builder.toString();
            }

            @Override
            protected void onPostExecute(String genre) {
                super.onPostExecute(genre);
                holder.setMovieGenre(mContext.getString(R.string.genre_text) + genre);
                holder.hGenre = genre;
            }
        }.execute();

    }

    /**
     * Set up the click
     *
     */
    public void setOnItemClickListenerSearch(final OnItemClickListenerSearch itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    /**
     * Click interface that will be used by the Fragment, and implemented by the ViewHolder.
     */
    public interface OnItemClickListenerSearch {
        void onItemClick(View view, int position, String movieId, String sortGroup);
    }

    /**
     * Inner class
     */
    public class ViewHolderSearch extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Public state
        public CardView placeCard;
        public RelativeLayout mainHolder;
        private TextView movieTitle;
        private ImageView moviePoster;
        private TextView releaseDate;
        private TextView popularities;
        private TextView movieGenre;

        // This is not a view, but a String, that we will pass to it the movie id
        // from the cursor, so then this will pass the movie id to the RecyclerViewFragmentMovie...
        public String hMovieID;
        public String hMovieTitle;
        public String hReleaseDate;
        public String hPosterPath;
        public String hGrade;
        public String hGenre;
        public String hBackdrop;
        public String hOverview;
        public String hSortGroup;

        // Constructor
        public ViewHolderSearch(View view) {
            super(view);
            placeCard = (CardView) view.findViewById(R.id.placeCard);
            mainHolder = (RelativeLayout) view.findViewById(R.id.mainHolder);
            movieTitle = (TextView) view.findViewById(R.id.movieTitle);
            moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
            releaseDate = (TextView) view.findViewById(R.id.releaseDate);
            popularities = (TextView) view.findViewById(R.id.grade);
            movieGenre = (TextView) view.findViewById(R.id.movieGenre);

            // Click listener
            placeCard.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getLayoutPosition(), hMovieID, hSortGroup);
            }
        }

        public void setMovieTitle(String data) {
            if (movieTitle != null) {
                movieTitle.setText(data);
            }
        }

        public void setMoviePoster(Context context, String url) {
            if (moviePoster != null) {
                Picasso.with(context).load(Utilities.PHOTO_BASE_URL + url).into(moviePoster);
                moviePoster.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        public void setReleaseDate(String data) {
            if (releaseDate != null) {
                releaseDate.setText(data);
            }
        }

        public void setPopularities(String data) {
            if (popularities != null) {
                popularities.setText(data);
            }
        }

        public void setMovieGenre(String data) {
            if (movieGenre != null) {
                movieGenre.setText(data);
            }
        }
    }

}
