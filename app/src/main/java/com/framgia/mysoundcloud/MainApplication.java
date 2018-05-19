package com.framgia.mysoundcloud;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import com.framgia.mysoundcloud.data.source.local.SharePreferences;
import com.framgia.mysoundcloud.data.source.local.TrackLocalDataSource;
import com.framgia.mysoundcloud.data.source.local.UserLocalDataSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

/**
 * Created by sonng266 on 12/03/2018.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TrackLocalDataSource.init(this);
        UserLocalDataSource.init(this);
        SharePreferences.init(this);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.framgia.mysoundcloud",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
}
