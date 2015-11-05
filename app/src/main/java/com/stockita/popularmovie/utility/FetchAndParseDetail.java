package com.stockita.popularmovie.utility;

import android.app.IntentService;

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
