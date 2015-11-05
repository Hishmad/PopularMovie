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
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.data.ModelPoster;
import com.stockita.popularmovie.utility.Utilities;

import java.util.ArrayList;

/**
 * Created by hishmadabubakaralamudi on 10/23/15.
 */
public class PostersDetailViewAdapter extends RecyclerView.Adapter<PostersDetailViewAdapter.ViewHolderDetailView> {

    // Constant
    private static final String LOG_TAG = PostersDetailViewAdapter.class.getSimpleName();

    // Member variables
    private Context mContext;
    private ArrayList<ModelPoster> mModelPosterArrayList;

    // Constructor
    public PostersDetailViewAdapter(Context context) {
        mContext = context;
    }

    /**
     * Reload the cursor here with data from the LoaderManager.
     * We also can pass other small data.
     *
     * @param c
     */
    public void swapCursor(ArrayList list) {
        mModelPosterArrayList = list;
        // Notify back
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return mModelPosterArrayList.get(position).get_id();
    }

    @Override
    public int getItemCount() {
        return mModelPosterArrayList != null ? mModelPosterArrayList.size() : 0;
    }

    /**
     * Setup the view and inflate the layout xml
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public PostersDetailViewAdapter.ViewHolderDetailView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posters_card, parent, false);
        return new ViewHolderDetailView(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDetailView holder, int position) {

        // CreditsEntry
        if (mModelPosterArrayList != null) {

            // Cast Image
            String profilePath = mModelPosterArrayList.get(position).getPosterPath();
            holder.setPosterImage(mContext, profilePath);

        }
    }

    /**
     * Inner class
     */
    public class ViewHolderDetailView extends RecyclerView.ViewHolder {

        // Public state
        public CardView posterCard;
        public ImageView posterImage;

        // Constructor
        public ViewHolderDetailView(View itemView) {
            super(itemView);
            posterCard = (CardView) itemView.findViewById(R.id.postersCard);
            posterImage = (ImageView) itemView.findViewById(R.id.posterImage);

        }

        public void setPosterImage(Context context, String url) {
            if (posterImage != null) {
                Picasso.with(context).load(Utilities.PHOTO_BASE_URL + url).into(posterImage);
                posterImage.setBackgroundColor(Color.TRANSPARENT);
            }
        }

    }

}
