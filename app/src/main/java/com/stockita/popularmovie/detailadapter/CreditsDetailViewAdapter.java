package com.stockita.popularmovie.detailadapter;

import android.support.v7.widget.RecyclerView;

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

import android.content.Context;
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
import com.stockita.popularmovie.data.ModelCredit;
import com.stockita.popularmovie.utility.Utilities;

import java.util.ArrayList;

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
