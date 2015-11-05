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
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stockita.popularmovie.R;
import com.stockita.popularmovie.data.ModelReview;

import java.util.ArrayList;

public class ReviewsDetailViewAdapter extends RecyclerView.Adapter<ReviewsDetailViewAdapter.ViewHolderDetailView> {

    // Constant
    private static final String LOG_TAG = TrailerDetailViewAdapter.class.getSimpleName();

    // Member variables
    private Context mContext;
    private ArrayList<ModelReview> mModelReviewArrayList;


    // Constructor
    public ReviewsDetailViewAdapter(Context context) {
        mContext = context;
    }

    /**
     * Get the data.
     * @param arrayList
     */
    public void swapCursor(ArrayList arrayList) {

        mModelReviewArrayList = arrayList;
        // Notify back
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {

        return mModelReviewArrayList.get(position).get_id();

    }

    @Override
    public int getItemCount() {
        return mModelReviewArrayList != null ? mModelReviewArrayList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.review_card;
    }

    /**
     * Setup the view and inflate the layout xml
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ReviewsDetailViewAdapter.ViewHolderDetailView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolderDetailView(view);
    }

    @Override
    public void onBindViewHolder(final ReviewsDetailViewAdapter.ViewHolderDetailView holder, final int position) {

        // ReviewEntry
        if (mModelReviewArrayList != null) {

            // User Name
            String userName = mModelReviewArrayList.get(position).getUserName();
            holder.setUserName(userName);

            // User comment
            String userComment = mModelReviewArrayList.get(position).getUserComment();
            holder.setUserReview(userComment);

        }
    }

    /**
     * Inner class
     */
    public class ViewHolderDetailView extends RecyclerView.ViewHolder {

        // Public state
        public CardView reviewsCard;
        private TextView userName;
        private TextView userReview;

        // Constructor
        public ViewHolderDetailView(View itemView) {
            super(itemView);
            reviewsCard = (CardView) itemView.findViewById(R.id.reviewsCard);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userReview = (TextView) itemView.findViewById(R.id.userReview);
        }

        public void setUserName(String data) {
            if (userName != null) {
                userName.setText(data);
            }
        }

        public void setUserReview(String data) {
            if (userReview != null) {
                userReview.setText(data);
            }
        }

    }


}
