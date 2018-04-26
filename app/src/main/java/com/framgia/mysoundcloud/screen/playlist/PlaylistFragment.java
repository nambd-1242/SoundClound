package com.framgia.mysoundcloud.screen.playlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.Playlist;
import com.framgia.mysoundcloud.screen.main.MainViewConstract;
import com.framgia.mysoundcloud.utils.Constant;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment implements PlaylistContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static PlaylistFragment sPlaylistFragment;

    private List<Playlist> mPlaylists;
    private PlaylistContract.Presenter mPresenter;
    private MainViewConstract.TrackListListener mListener;
    private PlaylistAdapter mPlaylistAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static PlaylistFragment newInstance(MainViewConstract.TrackListListener listener) {
        if (sPlaylistFragment == null) {
            sPlaylistFragment = new PlaylistFragment();
            Bundle args = new Bundle();
            args.putParcelable(Constant.ARGUMENT_TRACK_LIST_LISTENER, listener);
            sPlaylistFragment.setArguments(args);
        }
        return sPlaylistFragment;
    }

    public PlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        mPresenter = new PlaylistPresenter();
        mPresenter.setView(this);

        mListener = getArguments().getParcelable(Constant.ARGUMENT_TRACK_LIST_LISTENER);

        setupUI(view);
        mPresenter.loadPlaylist();

        return view;
    }

    @Override
    public void showPlaylist(List<Playlist> playlists) {
        mPlaylists = playlists;
        mPlaylistAdapter.replaceData(playlists);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setupUI(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        mPlaylistAdapter = new PlaylistAdapter(getContext(), mListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mPlaylistAdapter);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadPlaylist();
    }
}
