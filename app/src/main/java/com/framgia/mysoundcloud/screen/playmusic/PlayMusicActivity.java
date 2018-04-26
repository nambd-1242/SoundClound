package com.framgia.mysoundcloud.screen.playmusic;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.data.source.remote.DownloadTrackManager;
import com.framgia.mysoundcloud.utils.Constant;
import com.framgia.mysoundcloud.utils.StringUtil;
import com.framgia.mysoundcloud.service.MusicService;
import com.framgia.mysoundcloud.utils.music.PlaybackInfoListener;
import com.framgia.mysoundcloud.widget.DialogManager;

public class PlayMusicActivity extends AppCompatActivity
        implements PlayMusicContract.View, View.OnClickListener,
        DownloadTrackManager.DownloadListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    private DialogManager mDialogManager;
    private Track mCurrentTrack;
    private PlayMusicContract.Presenter mPresenter;
    private MusicService mMusicService;
    private boolean mBound;
    private SeekBar mSeekBar;
    private boolean mUserIsSeeking;
    private TextView mTextDuration;
    private TextView mTextEndTime;
    private ImageView mImagePlay;
    private ImageView mImageBackground;
    private ImageView mImageSong;
    private ProgressBar mProgressBar;
    private ImageView mImageLoop;
    private ImageView mImageShuffle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        setupUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this,
                MusicService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_show_next_up:
                if (mBound) {
                    BottomSheetDialogFragment bottomSheetDialogFragment =
                            NextUpDialogFragment.newInstance(mMusicService.getListTrack(),
                                    mNextUpListener, mMusicService.getCurrentTrackPosition());
                    bottomSheetDialogFragment.show(
                            getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_next_up, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_button_action_description:
                if (mCurrentTrack == null || mDialogManager == null) break;
                mDialogManager.dialogMessage(mCurrentTrack.getDescription(),
                        getString(R.string.title_description));
                break;
            case R.id.image_button_download:
                initializePermissionDownload();
                break;
            case R.id.image_play_next:
                if (mMusicService == null) break;
                mMusicService.playNext();
                break;
            case R.id.image_play_previous:
                if (mMusicService == null) break;
                mMusicService.playPrevious();
                break;
            case R.id.image_play_pause:
                if (mMusicService == null) break;
                mMusicService.changeMediaState();
                break;
            case R.id.image_shuffle:
                if (!mBound) break;
                mMusicService.changeShuffleState();
                changeIconShuffleState(mMusicService.isShuffle());
                break;
            case R.id.image_change_loop_type:
                if (!mBound) return;
                mMusicService.changeLoopType();
                changeIconLoopType(mMusicService.getLoopType());
                break;
            default:
                break;
        }
    }

    private void changeIconShuffleState(boolean isShuffle) {
        if (isShuffle) {
            mImageShuffle.setImageResource(R.drawable.ic_shuffle_deep_orange_500_48dp);
            return;
        }
        mImageShuffle.setImageResource(R.drawable.ic_shuffle_white_48dp);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new DownloadTrackManager(this, this)
                            .downloadTrack(mCurrentTrack);
                } else {
                    Toast.makeText(mMusicService, R.string.msg_permission_denied, Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

    @Override
    public void onDownloadError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloading() {
        Toast.makeText(this, getString(R.string.msg_downloading), Toast.LENGTH_SHORT).show();
    }

    public void showLoadingIndicator() {
        mProgressBar.setVisibility(View.VISIBLE);
        mImagePlay.setVisibility(View.INVISIBLE);
    }

    public void hideLoadingIndicator() {
        mProgressBar.setVisibility(View.GONE);
        mImagePlay.setVisibility(View.VISIBLE);
    }

    private void initializePermissionDownload() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    Constant.PERMISSIONS, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        } else {
            new DownloadTrackManager(this, this).downloadTrack(mCurrentTrack);
        }
    }

    private void setupUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPresenter = new PlayMusicPresenter();
        mPresenter.setView(this);
        mDialogManager = new DialogManager(this);
        mProgressBar = findViewById(R.id.progress_bar);
        mSeekBar = findViewById(R.id.seek_bar);
        mImageLoop = findViewById(R.id.image_change_loop_type);
        mImageLoop.setOnClickListener(this);
        mImageShuffle = findViewById(R.id.image_shuffle);
        mImageShuffle.setOnClickListener(this);
        mTextDuration = findViewById(R.id.text_duration);
        mTextEndTime = findViewById(R.id.text_end_time);
        mImagePlay = findViewById(R.id.image_play_pause);
        mImageBackground = findViewById(R.id.image_background);
        mImageSong = findViewById(R.id.image_song);
        mProgressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(
                        this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        setupSeekBar();
    }

    private void setupSeekBar() {
        mSeekBar.setMax(Constant.MAX_SEEK_BAR);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            /**
             * Su dung userSelectedPosition thay cho seekBar.getProgress() de
             * tranh truong hop khi nguoi dung bo tay ra, method getProgress chua duoc chay den
             * ma seek bar da duoc update lai
             */

            int userSelectedPosition = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    userSelectedPosition = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mUserIsSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mUserIsSeeking = false;
                if (mBound) {
                    mMusicService.actionSeekTo(userSelectedPosition);
                }
            }
        });
    }

    public void updateUIWithTrack() {
        if (mCurrentTrack == null || mMusicService == null) return;
        getSupportActionBar().setTitle(mCurrentTrack.getTitle());
        getSupportActionBar().setSubtitle(mCurrentTrack.getPublisherMetadata().getArtist());
        Glide.with(getApplicationContext())
                .load(mCurrentTrack.getArtworkUrl()).into(mImageBackground);
        Glide.with(getApplicationContext()).load(mCurrentTrack.getArtworkUrl()).into(mImageSong);
        mTextEndTime.setText(StringUtil.parseMilliSecondsToTimer(mCurrentTrack.getFullDuration()));
    }

    private void updateUIWithMediaState(@PlaybackInfoListener.State int state) {
        switch (state) {
            case PlaybackListener.State.PREPARE:
                showLoadingIndicator();
                break;
            case PlaybackListener.State.PLAYING:
                hideLoadingIndicator();
                mImagePlay.setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_pause_white_48dp));
                break;
            case PlaybackListener.State.PAUSE:
                hideLoadingIndicator();
                mImagePlay.setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_play_arrow_white_48dp));
                break;
            default:
                break;
        }
    }

    private void changeIconLoopType(@PlaybackInfoListener.LoopType int loopType) {
        switch (loopType) {
            case PlaybackListener.LoopType.NO_LOOP:
                mImageLoop.setImageResource(R.drawable.ic_repeat_white_48dp);
                break;
            case PlaybackInfoListener.LoopType.LOOP_ONE:
                mImageLoop.setImageResource(R.drawable.ic_repeat_one_deep_orange_500_48dp);
                break;
            case PlaybackInfoListener.LoopType.LOOP_LIST:
                mImageLoop.setImageResource(R.drawable.ic_repeat_deep_orange_500_48dp);
        }
    }

    private NextUpDialogFragment.NextUpItemClickedListener mNextUpListener =
            new NextUpDialogFragment.NextUpItemClickedListener() {
                @Override
                public void onTrackPositionClicked(int position) {
                    if (mBound) {
                        mMusicService.playTrackAtPosition(position);
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {

                }
            };

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBound = true;
            mMusicService = ((MusicService.LocalBinder) service).getService();

            mMusicService.setPlaybackListener(new PlaybackListener(true));
            mCurrentTrack = mMusicService.getCurrentTrack();

            updateUIWithTrack();
            updateUIWithMediaState(mMusicService.getMediaState());
            changeIconLoopType(mMusicService.getLoopType());
            changeIconShuffleState(mMusicService.isShuffle());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mMusicService.setPlaybackListener(null);
        }
    };

    private class PlaybackListener extends PlaybackInfoListener {

        PlaybackListener(boolean updatingProgressSeekBar) {
            super(updatingProgressSeekBar);
        }

        @Override
        public void onTrackChanged(Track track) {
            mCurrentTrack = track;
            updateUIWithTrack();
        }

        /**
         * Stop updating seek bar when user touches it
         *
         * @param percent
         */
        @Override
        public void onProgressUpdate(double percent) {
            if (!mUserIsSeeking) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mSeekBar.setProgress((int) percent, true);
                } else {
                    mSeekBar.setProgress((int) percent);
                }
            }
            mTextDuration.setText(StringUtil.parseMilliSecondsToTimer(
                    (long) ((mCurrentTrack.getFullDuration() / Constant.MAX_SEEK_BAR) * percent)));
        }

        @Override
        public void onStateChanged(int state) {
            updateUIWithMediaState(state);
        }
    }
}
