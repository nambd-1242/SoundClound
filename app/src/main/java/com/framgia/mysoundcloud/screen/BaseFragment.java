package com.framgia.mysoundcloud.screen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.screen.main.MainViewConstract;
import com.framgia.mysoundcloud.utils.Constant;

import java.util.List;


/**
 * Created by sonng266 on 01/03/2018.
 */

public abstract class BaseFragment extends Fragment {

    protected MainViewConstract.TrackListListener mTrackListListener;
    protected List<Track> mTracks;

    protected static Bundle putBundle(MainViewConstract.TrackListListener listener) {
        Bundle args = new Bundle();
        args.putParcelable(Constant.ARGUMENT_TRACK_LIST_LISTENER, listener);
        return args;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutFragmentId(), container, false);
        initializeUI(view);
        return view;
    }

    protected abstract void initializeUI(View view);

    protected abstract int getLayoutFragmentId();
}
