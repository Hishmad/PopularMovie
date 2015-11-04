package com.stockita.popularmovie.utility;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.IOException;


public class FetchAndParse extends IntentService {

    public static final String LOG_TAG = FetchAndParse.class.getSimpleName();

    public FetchAndParse() {
        super("FetchAndParse");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.v(LOG_TAG, "Data manual refresh");

        String searchForAMovie = intent.getStringExtra("search");

        // Check for network before fetching data
        if (!Utilities.isNetworkAvailable(getBaseContext())) {
            Log.v(LOG_TAG, "No network");
            return;
        } else {

            // Get only user search.
            if (searchForAMovie != null) {
                // Get the search result
                Utilities.downloadSearchData(getBaseContext(), searchForAMovie);
            } else {
                // Refresh
                Utilities.downloadAllData(getBaseContext());
            }
        }

    }

}
