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
public class UpcomingRecyclerViewAdapter extends RecyclerView.Adapter<UpcomingRecyclerViewAdapter.ViewHolderUpcoming> {

    // Constant
    private static final String LOG_TAG = UpcomingRecyclerViewAdapter.class.getSimpleName();

    // This constant is the value of the sort_group column.
    private static final String SORT_GROUP = "upcoming";


    // Member variables
    private Context mContext;
    private OnItemClickListenerUpcoming mItemClickListener;
    private ArrayList<ModelMovie> mListModelMovie;


    // Constructor
    public UpcomingRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    /**
     * Get data
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
    public ViewHolderUpcoming onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row_movies, parent, false);
        return new ViewHolderUpcoming(view);
    }

    /**
     * Here we go.
     */
    @Override
    public void onBindViewHolder(final ViewHolderUpcoming holder, final int position) {

        String lMovieId = null;

        // MovieEntry
        if (mListModelMovie != null) {

            // Movie Title
            String movieTitle = mListModelMovie.get(position).getMovieTitle();
            holder.setMovieTitle(movieTitle);

            // Poster path
            String moviePoster = mListModelMovie.get(position).getPosterPath();
            holder.setMoviePoster(mContext, moviePoster);

            // Release date
            String releaseDate = mListModelMovie.get(position).getReleaseDate();
            holder.setReleaseDate(mContext.getString(R.string.release_date) + releaseDate);

            // Popularity
            double popularities = mListModelMovie.get(position).getMoviePopularity();
            String popularitiesFormatted = String.format(Locale.ENGLISH, "%.2f", popularities);
            String popularitiesAfterFormat = (mContext.getString(R.string.popularities_grade) + popularitiesFormatted);
            holder.setPopularities(popularitiesAfterFormat);

            // Pass the data around
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
                    lCursor = data.query(ContractMovies.GenreEntry.CONTENT_URI, null, ContractMovies.GenreEntry.COLUMN_MOVIE_ID + "=?" +
                            " AND " + ContractMovies.GenreEntry.COLUMN_SORT_GROUP + "=?", new String[]{finalLMovieId, SORT_GROUP}, null);

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
     */
    public void setOnItemClickListenerUpcoming(final OnItemClickListenerUpcoming itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    /**
     * Click interface that will be used by the Fragment, and implemented by the ViewHolder.
     */
    public interface OnItemClickListenerUpcoming {
        void onItemClick(View view, int position, String movieId, String sortGroup);
    }

    /**
     * Inner class
     */
    public class ViewHolderUpcoming extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Public state
        public CardView placeCard;
        public RelativeLayout mainHolder;
        private TextView movieTitle;
        private ImageView moviePoster;
        private TextView releaseDate;
        private TextView popularities;
        private TextView movieGenre;

        // Pass the data around
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
        public ViewHolderUpcoming(View view) {
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
