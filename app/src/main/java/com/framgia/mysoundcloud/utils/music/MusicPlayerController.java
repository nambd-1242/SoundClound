package com.framgia.mysoundcloud.utils.music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.service.MusicService;
import com.framgia.mysoundcloud.utils.Constant;
import com.framgia.mysoundcloud.utils.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by sonng266 on 04/03/2018.
 */

public class MusicPlayerController implements MusicPlayerManager{

    private static final int PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 1000;
    private static final int MSG_UPDATE_SEEK_BAR_POSITION = 0;

    private ScheduledExecutorService mExecutor;
    private Runnable mSeekBarPositionUpdateTask;
    private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private PlaybackInfoListener mListener;
    private int mCurrentTrackPosition;
    private List<Track> mTracks;
    @PlaybackInfoListener.State
    private int mState;
    private MusicService mMusicService;
    @PlaybackInfoListener.LoopType
    private int mLoopType = PlaybackInfoListener.LoopType.NO_LOOP;
    private boolean mIsShuffle;
    private List<Track> mOriginalTracks;

    public MusicPlayerController(MusicService musicService) {
        mMusicService = musicService;
    }

    /**
     * stop playing music
     */
    @Override
    public void release() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    /**
     * PAUSE <-> PLAY
     */
    @Override
    public void changeMediaState() {
        if (mMediaPlayer == null) return;

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            notifyChangingState(PlaybackInfoListener.State.PAUSE);
        } else {
            mMediaPlayer.start();
            notifyChangingState(PlaybackInfoListener.State.PLAYING);

            if (mListener == null) return;
            if (mListener.isUpdatingProgressSeekBar() && mExecutor == null) {
                startProgressCallback();
            }
        }
    }

    @Override
    public void playNextTrack() {
        if (mCurrentTrackPosition == mTracks.size() - 1) {
            if (mLoopType != PlaybackInfoListener.LoopType.LOOP_LIST) return;
            mCurrentTrackPosition = -1;
        }

        mCurrentTrackPosition++;
        prepareLoadingTrack();
    }

    @Override
    public void playPreviousTrack() {
        if (mCurrentTrackPosition == 0) return;
        mCurrentTrackPosition--;
        prepareLoadingTrack();
    }

    /**
     * {@link ScheduledExecutorService} for scheduling call {@link Runnable}
     * {@link Runnable} call {@link Handler} to send message
     * {@link Handler} receive message at UI thread to updating seek bar
     */
    @Override
    public void startProgressCallback() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }

        if (mSeekBarPositionUpdateTask == null) {
            mHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == MSG_UPDATE_SEEK_BAR_POSITION) {
                        updateProgressCallbackTask();
                        return true;
                    }
                    return false;
                }
            });

            mSeekBarPositionUpdateTask = new Runnable() {
                @Override
                public void run() {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(MSG_UPDATE_SEEK_BAR_POSITION);
                    }
                }
            };
        }

        mExecutor.scheduleAtFixedRate(mSeekBarPositionUpdateTask,
                0,
                PLAYBACK_POSITION_REFRESH_INTERVAL_MS,
                TimeUnit.MILLISECONDS);
    }

    /**
     * Stop updating seek bar
     */
    @Override
    public void endProgressCallback() {
        if (mExecutor == null) return;

        mExecutor.shutdownNow();
        mExecutor = null;
        mSeekBarPositionUpdateTask = null;

        if (mListener == null) return;
        mListener.onProgressUpdate(0);
    }

    @Override
    public void setPlaybackInfoListener(PlaybackInfoListener listener) {
        Log.d("AMEN", "setPlaybackInfoListener: " + listener);

        mListener = listener;

        if (listener != null && listener.isUpdatingProgressSeekBar()) {
            startProgressCallback();
        }
    }

    @Override
    public void seekTo(int percent) {
        if (getCurrentState() == PlaybackInfoListener.State.PAUSE ||
                getCurrentState() == PlaybackInfoListener.State.PLAYING) {
            mMediaPlayer.seekTo(mMediaPlayer.getDuration() / Constant.MAX_SEEK_BAR * percent);
        }
    }

    @Override
    public Track getCurrentTrack() {
        return mTracks != null ? mTracks.get(mCurrentTrackPosition) : null;
    }

    @Override
    public int getCurrentState() {
        return mState;
    }

    @Override
    public List<Track> getListTrack() {
        return mTracks;
    }

    @Override
    public void playTrackAtPosition(int position, Track... tracks) {
        if (tracks == null && mTracks == null) {
            notifyChangingState(PlaybackInfoListener.State.INVALID);
            return;
        }

        // Neu track duoc chon dang duoc choi se khong chay lai
        if ((tracks == null || tracks.length == 0) && mCurrentTrackPosition == position) return;

        // Play a new list
        if (tracks != null && tracks.length != 0) {
            mTracks = new ArrayList<>();
            Collections.addAll(mTracks, tracks);

            mIsShuffle = false;
        }

        mCurrentTrackPosition = position;
        prepareLoadingTrack();
    }

    @Override
    public void addToNextUp(Track track) {
        if (mTracks == null || mTracks.isEmpty()) return;
        mTracks.add(track);

        if (!mIsShuffle) return;
        mOriginalTracks.add(track);
    }

    @Override
    public int getCurrentTrackPosition() {
        return mCurrentTrackPosition;
    }

    @Override
    public int getLoopType() {
        return mLoopType;
    }

    @Override
    public void changeLoopType() {
        switch (mLoopType) {
            case PlaybackInfoListener.LoopType.NO_LOOP:
                mLoopType = PlaybackInfoListener.LoopType.LOOP_LIST;
                break;
            case PlaybackInfoListener.LoopType.LOOP_LIST:
                mLoopType = PlaybackInfoListener.LoopType.LOOP_ONE;
                break;
            case PlaybackInfoListener.LoopType.LOOP_ONE:
                mLoopType = PlaybackInfoListener.LoopType.NO_LOOP;
                break;
        }
    }

    @Override
    public boolean isShuffle() {
        return mIsShuffle;
    }

    @Override
    public void changeShuffleState() {
        if (!mIsShuffle) {
            mOriginalTracks = new ArrayList<>();
            mOriginalTracks.addAll(mTracks);

            Track currentTrack = mTracks.get(mCurrentTrackPosition);

            Collections.shuffle(mTracks);
            mCurrentTrackPosition = mTracks.indexOf(currentTrack);

            mIsShuffle = true;
            return;
        }

        // mIsShuffle = true
        Track currentTrack = mTracks.get(mCurrentTrackPosition);

        mTracks = new ArrayList<>();
        mTracks.addAll(mOriginalTracks);

        mCurrentTrackPosition = mTracks.indexOf(currentTrack);

        mIsShuffle = false;
    }

//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        mMediaPlayer.start();
//        notifyChangingState(PlaybackInfoListener.State.PLAYING);
//
//        if (mListener == null) return;
//        if (mListener.isUpdatingProgressSeekBar()) {
//            startProgressCallback();
//        }
//    }

    private void prepareLoadingTrack() {
        if (mTracks == null || mTracks.isEmpty()) {
            notifyChangingState(PlaybackInfoListener.State.INVALID);
            return;
        }

        endProgressCallback();
        release();

        notifyChangingState(PlaybackInfoListener.State.PREPARE);
        loadTrack();

        if (mListener == null) return;
        mListener.onTrackChanged(mTracks.get(mCurrentTrackPosition));
    }

    private void loadTrack() {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            String url = StringUtil.getUrlStreamTrack(mTracks.get(mCurrentTrackPosition).getUri());
            Log.d("TAG", "loadTrack: " + url);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnCompletionListener(mCompletion);
            mMediaPlayer.setOnPreparedListener(mOnPrepared);
            mMediaPlayer.setOnErrorListener(mErrorListener);
        } catch (IOException e) {
            Logger.getLogger(e.toString());
        }
    }

    final MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return true;
        }
    };
    final MediaPlayer.OnCompletionListener mCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            endProgressCallback();
            notifyChangingState(PlaybackInfoListener.State.PAUSE);
            handleCompletionWithLoopType();
        }
    };
    final MediaPlayer.OnPreparedListener mOnPrepared = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mMediaPlayer.start();
            notifyChangingState(PlaybackInfoListener.State.PLAYING);
            if (mListener == null) return;
            if (mListener.isUpdatingProgressSeekBar()) {
                startProgressCallback();
            }
        }
    };

    private void handleCompletionWithLoopType() {
        switch (mLoopType) {
            case PlaybackInfoListener.LoopType.NO_LOOP:
                break;
            case PlaybackInfoListener.LoopType.LOOP_ONE:
                prepareLoadingTrack();
                return;
            case PlaybackInfoListener.LoopType.LOOP_LIST:
                if (mCurrentTrackPosition != mTracks.size() - 1) break;
                mCurrentTrackPosition = -1;
                break;
        }

        playNextTrack();
    }

    private void updateProgressCallbackTask() {
        if (mMediaPlayer == null || !mMediaPlayer.isPlaying()) return;

        int currentPosition = mMediaPlayer.getCurrentPosition();
        if (mListener == null) return;

        mListener.onProgressUpdate((double) currentPosition /
                mMediaPlayer.getDuration() * Constant.MAX_SEEK_BAR);
    }

    private void notifyChangingState(@PlaybackInfoListener.State int state) {
        mState = state;
        if (mMusicService != null) {
            if (state == PlaybackInfoListener.State.PREPARE) mMusicService.loadImage();
            mMusicService.initializeNotification(state);
        }

        if (mListener == null) return;
        mListener.onStateChanged(mState);
    }
}
