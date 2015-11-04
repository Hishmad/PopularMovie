package com.stockita.popularmovie.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.stockita.popularmovie.R;
import com.stockita.popularmovie.data.ContractMovies;

/**
 * Created by hishmadabubakaralamudi on 10/11/15.
 */
public class SyncUtility {

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = ContractMovies.AUTHORITY;

    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.stockita.popularmovie";

    // The account name
    public static final String ACCOUNT = "default_account";

    // Sync interval constant
    public static final long SECOND_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 1440L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECOND_PER_MINUTE;

    // Instance fields
    private static Account sAccount;
    private static ContentResolver sResolver;

    /**
     * execute the sync in interval mode
     * @param context
     */
    public static void execSyncInterval(Context context) {

        //MovieSyncAdapter.initializeSyncAdapter(this);
        sAccount = createSyncAccount(context);

        // Get the content resolver for your app
        sResolver = context.getContentResolver();

        // Turn on periodic syncing
        ContentResolver.setSyncAutomatically(sAccount, AUTHORITY, true);
        ContentResolver.addPeriodicSync(sAccount, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(createSyncAccount(context),
                AUTHORITY, bundle);
    }

    /**
     *
     * @param context
     * @return
     */
    private static Account createSyncAccount(Context context) {

        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(context.ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }
}
