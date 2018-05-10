package com.framgia.mysoundcloud.screen.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.framgia.mysoundcloud.screen.playlistdetail.PlaylistDetailTracksFragment;
import com.framgia.mysoundcloud.screen.musicgenres.MusicGenresFragment;
import com.framgia.mysoundcloud.screen.playlist.PlaylistFragment;
import com.framgia.mysoundcloud.screen.profile.ProfileFragment;
import com.framgia.mysoundcloud.utils.Constant;

/**
 * Created by sonng266 on 27/02/2018.
 */

public class MusicGenresPagerAdapter extends FragmentPagerAdapter {

    public static final int TAB_NUMBER = 5;
    private MainViewConstract.TrackListListener mListener;

    MusicGenresPagerAdapter(FragmentManager supportFragmentManager, MainViewConstract.TrackListListener listener) {
        super(supportFragmentManager);
        this.mListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MusicGenresFragment.newInstance(mListener);
            case 1:
                return PlaylistFragment.newInstance(mListener);
            case 2:
                return PlaylistDetailTracksFragment.newInstance(mListener, Constant.DOWLOAD);
            case 3:
                return PlaylistDetailTracksFragment.newInstance(mListener, Constant.FAVORITE);
            case 4:
                return ProfileFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_NUMBER;
    }

}
