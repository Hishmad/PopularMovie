package com.stockita.popularmovie.detailadapter;

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

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stockita.popularmovie.R;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.data.ModelCredit;
import com.stockita.popularmovie.data.ModelMovie;
import com.stockita.popularmovie.data.ModelTrailer;
import com.stockita.popularmovie.detailfragment.NewDetailFragment;
import com.stockita.popularmovie.utility.Utilities;

import java.util.ArrayList;
import java.util.Locale;

public class NewDetailAdapter extends RecyclerView.Adapter<NewDetailAdapter.ViewHolder> {

    // Constant
    private static final String LOG_TAG = NewDetailAdapter.class.getSimpleName();
    private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    // Member variables
    private Context mContext;
    private ArrayList<ModelMovie> mModelMovies;
    private ArrayList<ModelCredit> mModelCredits;
    private ArrayList<ModelTrailer> mModelTrailers;


    public NewDetailAdapter(Context context) {
        mContext = context;
    }

    /**
     * Get the data from the data set Loader
     */
    public void swapData(ArrayList data, int selection) {

        switch (selection) {
            case NewDetailFragment.LOADER_IDE_MOVIE_DETAIL:
                mModelMovies = data;
                break;
            case NewDetailFragment.LOADER_IDE_CAST:
                mModelCredits =  data;
                break;
            case NewDetailFragment.LOADER_IDE_TRAILERS:
                mModelTrailers = data;
                break;
        }
        // Always notify.
        notifyDataSetChanged();
    }


    /**
     * Mapping the position with table's row _id
     */
    @Override
    public long getItemId(int position) {
        return mModelMovies.get(position).get_id();
    }

    /**
     * Return the count other wise if null return 0;
     */
    @Override
    public int getItemCount() {
        return mModelMovies != null ? mModelMovies.size() : 0;
    }

    /**
     * This method will get the position and returns a resource layout id (R.layout....)
     * So we can manipulate, such as different layout for each position, instead of a static layout
     * for all.
     */
    @Override
    public int getItemViewType(int position) {
        return R.layout.new_detail_view;
    }

    @Override
    public NewDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String movieId = null;
        String sortGroup = null;
        String backdropPath = null;

        if (mModelMovies != null) {

            // Movie Id and sort group
            movieId = mModelMovies.get(position).getMovieId();
            sortGroup = mModelMovies.get(position).getSortGroup();

            // Movie Title
            String movieTitle = mModelMovies.get(position).getMovieTitle();
            holder.setMovieTitle(movieTitle);

            // Movie Poster
            String moviePoster = mModelMovies.get(position).getPosterPath();
            holder.setMoviePoster(mContext, moviePoster);

            // Release Date
            String releaseDate = mModelMovies.get(position).getReleaseDate();
            holder.setReleaseDate(mContext, releaseDate);

            // Popularity
            double popularities = mModelMovies.get(position).getMoviePopularity();
            String popularitiesFormatted = String.format(Locale.ENGLISH, "%.2f", popularities);
            holder.setPopularities(mContext, popularitiesFormatted);

            // Synopsis
            String synopsis = mModelMovies.get(position).getOverview();
            holder.setOVerview(synopsis);

            // Backdrop path this is a temporary image for trailer.
            backdropPath = mModelMovies.get(position).getBackdropPath();

        }

        // Genre
        final String finalLMovieId = movieId;
        final String finalSortGroup = sortGroup;
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
                            new String[]{finalLMovieId, finalSortGroup}, null);

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
                holder.setMovieGenre(mContext, genre);

            }
        }.execute();


        /* **** RecyclerView for Cast **** */
        // Initialize
        CreditsDetailViewAdapter lCreditsDetailViewAdapter = new CreditsDetailViewAdapter(mContext, mModelCredits);
        // Initialize layoutManager
        StaggeredGridLayoutManager lStaggeredGridLayoutManagerCredits =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        // Set the layout manager
        if (holder.recyclerViewCast != null) {
            holder.recyclerViewCast.setLayoutManager(lStaggeredGridLayoutManagerCredits);
            // Set the adapter.
            holder.recyclerViewCast.setAdapter(lCreditsDetailViewAdapter);
        }

        /* **** RecyclerView for Trailer. **** */
        // Initialize the adapter
        TrailerDetailViewAdapter lTrailerDetailViewAdapter = new TrailerDetailViewAdapter(mContext, mModelTrailers, backdropPath);
        // Initialize the layout manager
        StaggeredGridLayoutManager lStaggeredGridLayoutManagerTrailer =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        // Set the layoutManager
        if (holder.recyclerViewTrailer != null) {
            holder.recyclerViewTrailer.setLayoutManager(lStaggeredGridLayoutManagerTrailer);
            // Set the adapter
            holder.recyclerViewTrailer.setAdapter(lTrailerDetailViewAdapter);
        }

        // Set the click listener for Trailer
        lTrailerDetailViewAdapter.setOnItemClickListenerTrailer(onItemClickListenerTrailer);

    }

    /**
     * Custom click listener
     * When the user click item in the list, this will be executed.
     * This will take to youtube and play the trailer.
     */
    public TrailerDetailViewAdapter.OnItemClickListenerTrailer onItemClickListenerTrailer =
            new TrailerDetailViewAdapter.OnItemClickListenerTrailer() {
                @Override
                public void onItemClick(View view, int position, String trailerLink) {
                    // Goto youtube.
                    if (Utilities.isNetworkAvailable(mContext)) {
                        if (trailerLink != null) {
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(YOUTUBE_URL + trailerLink)));
                        }
                    }
                }
            };


    /**
     * ViewHolder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        // State
        // MovieEntry
        public CardView placeCard;
        public RelativeLayout mainHolder;
        public RecyclerView recyclerViewCast, recyclerViewTrailer;
        private TextView movieTitle;
        private ImageView moviePoster;
        private TextView releaseDate;
        private TextView popularities;
        private TextView movieGenre;
        private TextView overView;


        // Constructor
        public ViewHolder(View view) {
            super(view);

            // MovieEntry
            // layout file: new_detail_view.xml
            placeCard = (CardView) view.findViewById(R.id.new_placeCardDetail);
            mainHolder = (RelativeLayout) view.findViewById(R.id.new_mainHolderDetail);
            recyclerViewCast = (RecyclerView) view.findViewById(R.id.new_recyclerViewCastCardDetail);
            recyclerViewTrailer = (RecyclerView) view.findViewById(R.id.new_recyclerViewTrailerCardDetail);
            movieTitle = (TextView) view.findViewById(R.id.new_movieTitleDetail);
            moviePoster = (ImageView) view.findViewById(R.id.new_moviePosterDetail);
            releaseDate = (TextView) view.findViewById(R.id.new_releaseDateDetail);
            popularities = (TextView) view.findViewById(R.id.new_gradeDetail);
            movieGenre = (TextView) view.findViewById(R.id.new_movieGenreDetail);
            overView = (TextView) view.findViewById(R.id.new_overviewDetail);
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

        public void setReleaseDate(Context context, String data) {
            if (releaseDate != null) {
                releaseDate.setText(context.getString(R.string.release_date) + data);
            }
        }

        public void setPopularities(Context context, String data) {
            if (popularities != null) {
                popularities.setText(context.getString(R.string.popularities_grade) + data);
            }
        }

        public void setMovieGenre(Context context, String data) {
            if (movieGenre != null) {
                movieGenre.setText(context.getString(R.string.genre_text) + data);
            }
        }

        public void setOVerview(String data) {
            if (overView != null) {
                overView.setText(data);
            }
        }

    }
}
