package com.stockita.popularmovie.utility;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by hishmadabubakaralamudi on 10/20/15.
 */
public class FetchAndParseDetail extends IntentService {

    public static final String LOG_TAG = FetchAndParseDetail.class.getSimpleName();


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public FetchAndParseDetail() {
        super("fetchAndParseDetail");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.v(LOG_TAG, "fetch detail");

        String movieId = intent.getStringExtra("keyMovieId");
        int origin = Integer.parseInt(intent.getStringExtra("keyOrigin"));


        // Check for network before fetching data
        if (Utilities.isNetworkAvailable(getBaseContext())) {

            switch (origin) {
                case 1:
                {
                    Log.v("fetch", "creditEntry");
                    Utilities.getCreditesData(getBaseContext(), movieId);
                    break;
                }
                case 2:
                {
                    Log.v("fetch", "trailerEntry");
                    Utilities.getTrailerData(getBaseContext(), movieId);
                    break;
                }
                case 3:
                {
                    Log.v("fetch", "posterEntry");
                    Utilities.getPosterData(getBaseContext(), movieId);
                    break;
                }
                case 4:
                {
                    Log.v("fetch","reviewEntry");
                    Utilities.getReviewsData(getBaseContext(), movieId);
                    break;
                }
            }

        } else {
            Log.v(LOG_TAG, "No Network");
        }

    }
}
