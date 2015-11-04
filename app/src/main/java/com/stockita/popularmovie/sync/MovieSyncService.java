package com.stockita.popularmovie.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * Created by hishmadabubakaralamudi on 10/10/15.
 */
public class MovieSyncService extends Service {

    // Storage for an instance of the sync adapter
    private static MovieSyncAdapter sMovieSyncAdapter = null;

    // Object to use as a thread-safe lock
    private static final Object sMovieSyncAdapterLock = new Object();


    /**
     * Instantiate the sync adapter object
     */
    @Override
    public void onCreate() {

        /**
         * Create the sync adapter as singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sMovieSyncAdapterLock) {
            if (sMovieSyncAdapter == null) {
                sMovieSyncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }

    }

    /**
     * Return an object that allows the system to invoke
     * the synce adapter.
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent){

        /**
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return sMovieSyncAdapter.getSyncAdapterBinder();

    }
}
