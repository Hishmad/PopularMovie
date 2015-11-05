package com.stockita.popularmovie.utility;

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

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.stockita.popularmovie.R;



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
            Log.v(LOG_TAG, getBaseContext().getString(R.string.info_user_check_connection));
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
