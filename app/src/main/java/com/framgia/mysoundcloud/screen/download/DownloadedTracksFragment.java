package com.framgia.mysoundcloud.screen.download;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.repository.TrackRepository;
import com.framgia.mysoundcloud.screen.BaseFragment;
import com.framgia.mysoundcloud.screen.main.MainViewConstract;
import com.framgia.mysoundcloud.utils.Constant;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadedTracksFragment extends BaseFragment
        implements DownloadViewContract.View, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, DownloadViewContract.DeleteTrackListener {

    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 2;
    private DownloadViewContract.Presenter mPresenter;
    private DownloadedTracksAdapter mDownloadedTracksAdapter;
    private TextView mTextNumberTracks;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static DownloadedTracksFragment newInstance(
            MainViewConstract.TrackListListener listListener) {
        DownloadedTracksFragment downloadedTracksFragment = new DownloadedTracksFragment();
        downloadedTracksFragment.setArguments(putBundle(listListener));
        return downloadedTracksFragment;
    }

    public DownloadedTracksFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initializeUI(View view) {
        mPresenter = new DownloadedTracksPresenter();
        mPresenter.setView(this);

        ImageView imagePlayList = view.findViewById(R.id.action_image_play_list);
        imagePlayList.setOnClickListener(this);

        mTextNumberTracks = view.findViewById(R.id.text_top_50);
        mTextNumberTracks.setText(R.string.msg_0_track);

        mTrackListListener = getArguments().getParcelable(Constant.ARGUMENT_TRACK_LIST_LISTENER);

        setupRecyclerView(view);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        initializePermissionStorage();
    }

    @Override
    protected int getLayoutFragmentId() {
        return R.layout.fragment_download;
    }

    @Override
    public void showTracks(ArrayList<Track> trackList) {
        mTextNumberTracks.setText(String.format("%d tracks", trackList.size()));
        mDownloadedTracksAdapter.replaceData(trackList);
        mTracks = trackList;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNoTrack() {
        mTextNumberTracks.setText(R.string.msg_0_track);
        mSwipeRefreshLayout.setRefreshing(false);
        mDownloadedTracksAdapter.replaceData(new ArrayList<Track>());
    }

    @Override
    public void showLoadingTracksError(String message) {
        mTextNumberTracks.setText(R.string.msg_0_track);
        mSwipeRefreshLayout.setRefreshing(false);
        mDownloadedTracksAdapter.replaceData(new ArrayList<Track>());
    }

    @Override
    public void showLoadingIndicator() {

    }

    @Override
    public void hideLoadingIndicator() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.loadTrack();
                } else {
                    Toast.makeText(getActivity(), R.string.msg_permission_denied, Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_image_play_list:
                if (mTrackListListener == null) break;
                mTrackListListener.onPlayedTrack(0, mTracks);
                break;
        }
    }

    @Override
    public void onRefresh() {
        initializePermissionStorage();
    }

    @Override
    public void onDeleteClicked(Track track) {
        if (!TrackRepository.getInstance().deleteTrack(track)) return;
        mPresenter.loadTrack();
    }

    private void initializePermissionStorage() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    Constant.PERMISSIONS, MY_PERMISSIONS_REQUEST_STORAGE);
        } else {
            mPresenter.loadTrack();
        }
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        mDownloadedTracksAdapter = new DownloadedTracksAdapter(getContext(),
                mTrackListListener, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mDownloadedTracksAdapter);
    }
}
