package com.framgia.mysoundcloud.screen.search;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.screen.BaseFragment;
import com.framgia.mysoundcloud.screen.main.MainViewConstract;
import com.framgia.mysoundcloud.screen.musicgenres.MusicGenresAdapter;
import com.framgia.mysoundcloud.utils.Constant;
import com.framgia.mysoundcloud.utils.EndlessScrollListener;
import com.framgia.mysoundcloud.widget.DialogManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements SearchViewContract.View,
        EndlessScrollListener.LoadMoreListener {

    private SearchViewPresenter mPresenter;
    private MusicGenresAdapter mAdapter;
    private String mQuery;
    private DialogManager mDialogManager;
    private ProgressDialog mProgressDialog;
    private EndlessScrollListener mEndlessScrollListener;

    public static SearchFragment newInstance(MainViewConstract.TrackListListener listener) {
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(putBundle(listener));
        return searchFragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initializeUI(View view) {
        mTrackListListener = getArguments().getParcelable(Constant.ARGUMENT_TRACK_LIST_LISTENER);

        mPresenter = new SearchViewPresenter();
        mPresenter.setView(this);

        mDialogManager = new DialogManager(getContext());

        initializeRecyclerView(view);
    }

    @Override
    protected int getLayoutFragmentId() {
        return R.layout.fragment_search;
    }

    @Override
    public void showTracks(ArrayList<Track> trackList) {
        if (mEndlessScrollListener.isLoading()) {
            mTracks = mAdapter.addMoreData(trackList);
            mEndlessScrollListener.setLoading(false);
            return;
        }

        mAdapter.replaceData(trackList);
        mTracks = trackList;
    }

    @Override
    public void showNoTrack() {
        if (mEndlessScrollListener.isLoading()) {
            mEndlessScrollListener.setLoading(false);
            return;
        }

        mAdapter.replaceData(new ArrayList<Track>());

        mDialogManager.dialogMessage(getString(R.string.msg_no_song_found), "");
    }

    @Override
    public void showLoadingTracksError(String message) {
        if (mEndlessScrollListener.isLoading()) {
            mEndlessScrollListener.setLoading(false);
        }
    }

    @Override
    public void showLoadingIndicator() {
        mProgressDialog = ProgressDialog.show(getContext(), mQuery,
                getString(R.string.msg_loading), true);
    }

    @Override
    public void hideLoadingIndicator() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void requestLoadMore(int totalItemCount) {
        mPresenter.searchTrack(mQuery, totalItemCount);
    }

    public void submitQuery(String query) {
        mQuery = query;
        mPresenter.searchTrack(query, 0);
    }

    private void initializeRecyclerView(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new MusicGenresAdapter(getContext(), mTrackListListener);
        mEndlessScrollListener = new EndlessScrollListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(mEndlessScrollListener);
    }
}
