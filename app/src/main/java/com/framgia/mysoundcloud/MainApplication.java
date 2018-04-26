package com.framgia.mysoundcloud;

import android.app.Application;

import com.framgia.mysoundcloud.data.source.local.TrackLocalDataSource;

/**
 * Created by sonng266 on 12/03/2018.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TrackLocalDataSource.init(this);
    }
}
