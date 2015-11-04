package com.stockita.popularmovie.detailadapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stockita.popularmovie.R;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.data.ModelCredit;
import com.stockita.popularmovie.utility.Utilities;

import java.util.ArrayList;

/**
 * Created by hishmadabubakaralamudi on 10/22/15.
 */
public class CreditsDetailViewAdapter extends RecyclerView.Adapter<CreditsDetailViewAdapter.ViewHolderDetailView> {


    // Constant
    private static final String LOG_TAG = CreditsDetailViewAdapter.class.getSimpleName();

    // Member variables
    private Context mContext;
    private ArrayList<ModelCredit> mModelCreditArrayList;

    // Constructor
    public CreditsDetailViewAdapter(Context context, ArrayList<ModelCredit> arrayList) {
        mContext = context;
        mModelCreditArrayList = arrayList;

        // Notify
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return mModelCreditArrayList.get(position).get_id();

    }

    @Override
    public int getItemCount() {
        return mModelCreditArrayList != null ? mModelCreditArrayList.size() : 0;
    }

    /**
     * This will return the viewType that will be inflate in onCreateViewHolder();
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return R.layout.credits_card;
    }

    /**
     * Setup the view and inflate the layout xml
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CreditsDetailViewAdapter.ViewHolderDetailView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        return new ViewHolderDetailView(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDetailView holder, int position) {

        // CreditsEntry
        if (mModelCreditArrayList != null) {

            // Cast name
            String castName = mModelCreditArrayList.get(position).getPlayerName();
            holder.setCastName(castName);

            // Cast Image
            String profilePath = mModelCreditArrayList.get(position).getProfilePath();
            holder.setCastMovieProfile(mContext, profilePath);

        }
    }

    /**
     * Inner class
     */
    public class ViewHolderDetailView extends RecyclerView.ViewHolder {

        // Public variables
        public CardView creditsCard;
        private ImageView castMovieProfile;
        private TextView castName;

        // Constructor
        public ViewHolderDetailView(View itemView) {
            super(itemView);
            creditsCard = (CardView) itemView.findViewById(R.id.creditsCard);
            castMovieProfile = (ImageView) itemView.findViewById(R.id.castImage);
            castName = (TextView) itemView.findViewById(R.id.castName);

        }

        public void setCastName(String data) {
            if (castName != null) {
                castName.setText(data);
            }
        }

        public void setCastMovieProfile(Context context, String url) {
            if (castMovieProfile != null) {
                Picasso.with(context).load(Utilities.PHOTO_BASE_URL + url).into(castMovieProfile);
                castMovieProfile.setBackgroundColor(Color.TRANSPARENT);
            }
        }

    }


}
