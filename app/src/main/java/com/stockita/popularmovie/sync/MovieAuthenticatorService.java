package com.stockita.popularmovie.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by hishmadabubakaralamudi on 10/6/15.
 */
public class MovieAuthenticatorService extends Service {

    private MovieAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new MovieAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
