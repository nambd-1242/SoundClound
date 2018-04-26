package com.framgia.mysoundcloud.screen.musicgenres;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.screen.BaseFragment;
import com.framgia.mysoundcloud.screen.main.MainViewConstract;
import com.framgia.mysoundcloud.utils.Constant;
import com.framgia.mysoundcloud.utils.EndlessScrollListener;
import com.framgia.mysoundcloud.widget.DialogManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class MusicGenresFragment extends BaseFragment implements
        MusicGenresContract.View, AdapterView.OnItemSelectedListener, View.OnClickListener,
        EndlessScrollListener.LoadMoreListener {

    private MusicGenresPresenter mPresenter;
    private MusicGenresAdapter mMusicGenresAdapter;
    private DialogManager mDialogManager;
    private ProgressDialog mProgressDialog;
    private Spinner mSpinnerGenres;
    private TextView mTextNumberTracks;
    private EndlessScrollListener mEndlessScrollListener;

    public static MusicGenresFragment newInstance(MainViewConstract.TrackListListener listListener) {
        MusicGenresFragment musicGenresFragment = new MusicGenresFragment();
        musicGenresFragment.setArguments(putBundle(listListener));
        return musicGenresFragment;
    }

    public MusicGenresFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initializeUI(View view) {

        mTrackListListener = getArguments().getParcelable(Constant.ARGUMENT_TRACK_LIST_LISTENER);

        mPresenter = new MusicGenresPresenter();
        mPresenter.setView(this);

        mTextNumberTracks = view.findViewById(R.id.text_top_50);

        ArrayAdapter<CharSequence> genresAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.music_genres, android.R.layout.simple_spinner_item);
        genresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerGenres = view.findViewById(R.id.spinner_genres);
        mSpinnerGenres.setAdapter(genresAdapter);
        mSpinnerGenres.setOnItemSelectedListener(this);

        initializeRecyclerView(view);

        mDialogManager = new DialogManager(getContext());

        ImageView imagePlayingList = view.findViewById(R.id.action_image_play_list);
        imagePlayingList.setOnClickListener(this);
    }

    @Override
    protected int getLayoutFragmentId() {
        return R.layout.fragment_music_genres;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_genres:
                mPresenter.loadTrack(Constant.MUSIC_GENRES[position],
                        Constant.LIMIT_DEFAULT, Constant.OFFSET_DEFAULT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void showTracks(ArrayList<Track> trackList) {
        if (mEndlessScrollListener.isLoading()) {
            mTracks = mMusicGenresAdapter.addMoreData(trackList);
            mEndlessScrollListener.setLoading(false);
            return;
        }

        mMusicGenresAdapter.replaceData(trackList);
        mTracks = trackList;
    }

    @Override
    public void showNoTrack() {
        if (mEndlessScrollListener.isLoading()) {
            mEndlessScrollListener.setLoading(false);
            return;
        }

        mMusicGenresAdapter.replaceData(new ArrayList<Track>());
        showDialogError();
    }

    @Override
    public void showLoadingTracksError(String message) {
        if (mEndlessScrollListener.isLoading()) {
            mEndlessScrollListener.setLoading(false);
        }
    }

    @Override
    public void showLoadingIndicator() {
        mProgressDialog = ProgressDialog.show(getContext(),
                Constant.MUSIC_GENRES[mSpinnerGenres.getSelectedItemPosition()],
                getString(R.string.msg_loading), true);
    }

    @Override
    public void hideLoadingIndicator() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_image_play_list:
                if (mTracks == null || mTracks.isEmpty() ||
                        mTrackListListener == null) break;
                mTrackListListener.onPlayedTrack(0, mTracks);
                break;
            default:
                break;
        }
    }

    @Override
    public void updateTextNumberTrack() {
        if (mTracks == null) return;
        mTextNumberTracks.setText(String.format("%s %d", getString(R.string.msg_top), mTracks.size()));
    }

    @Override
    public void requestLoadMore(int totalItemCount) {
        mPresenter.loadTrack(
                Constant.MUSIC_GENRES[mSpinnerGenres.getSelectedItemPosition()],
                Constant.LIMIT_DEFAULT, totalItemCount);
    }

    private void showDialogError() {
        if (mDialogManager == null) return;
        mDialogManager.dialogMessage(getString(R.string.msg_no_internet_connection),
                getString(R.string.title_error));
    }


    private void initializeRecyclerView(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mMusicGenresAdapter = new MusicGenresAdapter(getContext(), mTrackListListener);
        mEndlessScrollListener = new EndlessScrollListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mMusicGenresAdapter);
        recyclerView.addOnScrollListener(mEndlessScrollListener);
    }
}
