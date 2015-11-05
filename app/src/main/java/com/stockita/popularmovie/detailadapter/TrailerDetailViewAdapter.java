package com.stockita.popularmovie.detailadapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stockita.popularmovie.R;
import com.stockita.popularmovie.data.ModelTrailer;
import com.stockita.popularmovie.utility.Utilities;

import java.util.ArrayList;

/**
 * Created by hishmadabubakaralamudi on 10/20/15.
 */
public class TrailerDetailViewAdapter extends RecyclerView.Adapter<TrailerDetailViewAdapter.ViewHolderDetailView> {

    // Constant
    private static final String LOG_TAG = TrailerDetailViewAdapter.class.getSimpleName();

    // Member variables
    private Context mContext;
    private ArrayList<ModelTrailer> mModelTrailerArrayList;
    private OnItemClickListenerTrailer mItemClickListener;
    private String mBackdropPath;

    // Constructor
    public TrailerDetailViewAdapter(Context context, ArrayList<ModelTrailer> arrayList, String backdropPath) {
        mContext = context;
        mModelTrailerArrayList = arrayList;
        mBackdropPath = backdropPath;

        // Notify
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return mModelTrailerArrayList.get(position).get_id();
    }

    @Override
    public int getItemCount() {
        return mModelTrailerArrayList != null ? mModelTrailerArrayList.size() : 0;
    }

    /**
     * Setup the view and inflate the layout xml
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public TrailerDetailViewAdapter.ViewHolderDetailView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_card, parent, false);
        return new ViewHolderDetailView(view);
    }

    @Override
    public void onBindViewHolder(final TrailerDetailViewAdapter.ViewHolderDetailView holder, final int position) {

        // TrailerEntry
        if (mModelTrailerArrayList != null) {

            // Trailer Image
            holder.setTrailerMovieBackdrop(mContext, mBackdropPath);

            // Trailer link to youtube. (URL)
            String trailerLink = mModelTrailerArrayList.get(position).getTrailer();
            holder.hTrailerLink = trailerLink;
        }

    }

    /**
     * Set up the click
     *
     * @param itemClickListener
     */
    public void setOnItemClickListenerTrailer(final OnItemClickListenerTrailer itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    /**
     * Interface for click listener that will be use by the NewDetailFragment class.
     */
    public interface OnItemClickListenerTrailer {
        void onItemClick(View view, int position, String trailerLink);
    }

    /**
     * Inner class
     */
    public class ViewHolderDetailView extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Public state
        public CardView trailerCard;
        private ImageView trailerMovieBackdrop;

        // Data
        public String hTrailerLink;

        // Constructor
        public ViewHolderDetailView(View itemView) {
            super(itemView);
            trailerCard = (CardView) itemView.findViewById(R.id.trailerCard);
            trailerMovieBackdrop = (ImageView) itemView.findViewById(R.id.trailerImage);

            // Click listener
            trailerCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getLayoutPosition(), hTrailerLink);
            }
        }

        public void setTrailerMovieBackdrop(Context context, String url) {
            if (trailerMovieBackdrop != null) {
                Picasso.with(context).load(Utilities.PHOTO_BASE_URL + url).into(trailerMovieBackdrop);
                trailerMovieBackdrop.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }


}
