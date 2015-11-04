package com.stockita.popularmovie.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.utility.Utilities;

import org.json.JSONException;

/**
 * Created by hishmadabubakaralamudi on 10/9/15.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Constructor
     *
     * @param context
     * @param autoInitialize
     */
    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor
     *
     * @param context
     * @param autoInitialize
     * @param allowParallelSyncs
     */
    public MovieSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {


        /**
         * Put the data transfer code here.
         */
        Log.v(LOG_TAG, "Data fetch begin");
        try {
            Utilities.downloadAllData(getContext());
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

}

