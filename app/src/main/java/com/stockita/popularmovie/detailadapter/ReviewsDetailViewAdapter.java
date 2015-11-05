package com.stockita.popularmovie.detailadapter;

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

/**
 * Created by hishmadabubakaralamudi on 10/21/15.
 */
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
