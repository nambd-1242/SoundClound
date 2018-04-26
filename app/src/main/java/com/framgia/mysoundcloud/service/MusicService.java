package com.framgia.mysoundcloud.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.Track;
import com.framgia.mysoundcloud.screen.main.MainActivity;
import com.framgia.mysoundcloud.utils.music.MusicPlayerController;
import com.framgia.mysoundcloud.utils.music.MusicPlayerManager;
import com.framgia.mysoundcloud.utils.music.PlaybackInfoListener;

import java.util.List;


public class MusicService extends Service {

    // Action
    public static final String ACTION_CHANGE_MEDIA_STATE = "ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT_TRACK = "ACTION_NEXT_TRACK";
    public static final String ACTION_PREVIOUS_TRACK = "ACTION_PREVIOUS_TRACK";
    public static final String ACTION_OPEN_PLAY_MUSIC_ACTIVITY = "ACTION_OPEN_PLAY_MUSIC_ACTIVITY";

    // Notification
    private static final int NOTIFY_ID = 1;
    private static final int ORDER_ACTION_PREVIOUS = 0;
    private static final int ORDER_ACTION_PLAY_PAUSE = 1;
    private static final int ORDER_ACTION_NEXT = 2;
    private static final String TITLE_ACTION_PREVIOUS = "Previous";
    private static final String TITLE_ACTION_PLAY = "Play";
    private static final String TITLE_ACTION_PAUSE = "Pause";
    private static final String TITLE_ACTION_NEXT = "Next";

    private final IBinder mBinder = new LocalBinder();

    private MusicPlayerManager mMusicPlayerManager;
    private NotificationCompat.Builder mBuilder;
    private PendingIntent pendingIntentOpenApp;
    private PendingIntent nextPendingIntent;
    private PendingIntent prevPendingIntent;
    private PendingIntent ptPlayPause;
    private Bitmap mBitmap;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * return START_NOT_STICKY : khi system bi kill neu service o trang thai started
     * service cung se bi kill va
     * se khong duoc goi de khoi chay lai
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        handleIntent(intent);
        return mBinder;
    }

    /**
     * Nhan intent tu notification de control nhac
     * Khi dang o trang thai PREPARE nhac se khong the PLAY - PAUSE
     * vi no chua load duoc ve buffer
     *
     * @param intent
     */
    public void handleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action == null) return;
        switch (action) {
            case ACTION_CHANGE_MEDIA_STATE:
                if (getMediaState() != PlaybackInfoListener.State.PREPARE) {
                    changeMediaState();
                }
                break;
            case ACTION_PREVIOUS_TRACK:
                playPrevious();
                break;
            case ACTION_NEXT_TRACK:
                playNext();
                break;
            default:
                break;
        }
    }

    /**
     * Goi den cac phuong thuc cho {@link MusicPlayerManager} xu ly
     *
     * @param playbackListener
     */

    public void setPlaybackListener(PlaybackInfoListener playbackListener) {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.setPlaybackInfoListener(playbackListener);
    }

    public Track getCurrentTrack() {
        return mMusicPlayerManager != null ? mMusicPlayerManager.getCurrentTrack() : null;
    }

    public void actionSeekTo(int userSelectedPosition) {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.seekTo(userSelectedPosition);
    }

    public void changeMediaState() {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.changeMediaState();
    }

    public void playNext() {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.playNextTrack();
    }

    public void playPrevious() {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.playPreviousTrack();
    }

    public int getMediaState() {
        if (mMusicPlayerManager == null) return PlaybackInfoListener.State.INVALID;
        return mMusicPlayerManager.getCurrentState();
    }

    /**
     * Viec su dung foreground service giup ung dung bi kill nhac van chay
     * Khi nhac dung se stop foreground, thuoc tinh false de khi dung nhac, notification khong bi mat
     * Voi cung NOTIFY_ID notification se duoc update
     */

    public void initializeNotification(@PlaybackInfoListener.State int state) {
        if (getCurrentTrack() == null) return;
        initializeBaseNotification();
        if (state == PlaybackInfoListener.State.PAUSE) {

            stopForeground(false);

            mBuilder.setOngoing(false)
                    .addAction(R.drawable.ic_skip_previous_black_24dp, TITLE_ACTION_PREVIOUS, prevPendingIntent)   // #0
                    .addAction(R.drawable.ic_play_arrow_black_36dp, TITLE_ACTION_PLAY, ptPlayPause)    // #1
                    .addAction(R.drawable.ic_skip_next_black_24dp, TITLE_ACTION_NEXT, nextPendingIntent)  // #2
                    .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(ORDER_ACTION_PREVIOUS, ORDER_ACTION_PLAY_PAUSE, ORDER_ACTION_NEXT));
            if (notificationManager == null) {
                notificationManager =
                        (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String name = "my_package_channel";
            String id = "my_package_channel_1";
            String description = "my_package_first_channel";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, name, importance);
                    mChannel.setDescription(description);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notificationManager.createNotificationChannel(mChannel);
                }
//                mBuilder = new NotificationCompat.Builder(this, id);
//
//                builder.setContentTitle(aMessage)  // required
//                        .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
//                        .setContentText(this.getString(R.string.app_name))  // required
//                        .setDefaults(Notification.DEFAULT_ALL)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent)
//                        .setTicker(aMessage)
//                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            }
//            else {
//                builder = new NotificationCompat.Builder(this);
//                builder.setContentTitle(aMessage)                           // required
//                        .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
//                        .setContentText(this.getString(R.string.app_name))  // required
//                        .setDefaults(Notification.DEFAULT_ALL)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent)
//                        .setTicker(aMessage)
//                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
//                        .setPriority(Notification.PRIORITY_HIGH);
//            }
            notificationManager.notify(NOTIFY_ID, mBuilder.build());

        } else {
            mBuilder.addAction(R.drawable.ic_skip_previous_black_24dp, TITLE_ACTION_PREVIOUS, prevPendingIntent)   // #0
                    .addAction(R.drawable.ic_pause_black_36dp, TITLE_ACTION_PAUSE, ptPlayPause)    // #2
                    .addAction(R.drawable.ic_skip_next_black_24dp, TITLE_ACTION_NEXT, nextPendingIntent)  // #1
                    .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(ORDER_ACTION_PREVIOUS, ORDER_ACTION_PLAY_PAUSE, ORDER_ACTION_NEXT));

            startForeground(NOTIFY_ID, mBuilder.build());   // startForeground need a notification
        }
    }

    private NotificationManager notificationManager;

    /**
     * Load image se bi delay do do tao notification truoc
     * neu image load xong thi update lai
     */
    public void loadImage() {
        if (getCurrentTrack() == null) return;
        Glide.with(this)
                .load(getCurrentTrack().getArtworkUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mBitmap = resource;

                        mBuilder.setLargeIcon(mBitmap);
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(NOTIFY_ID, mBuilder.build());
                    }
                });
    }

    private void initializeBaseNotification() {

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setAction(ACTION_OPEN_PLAY_MUSIC_ACTIVITY);
        pendingIntentOpenApp = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        Intent actionNextIntent = new Intent(getApplicationContext(), MusicService.class);
        actionNextIntent.setAction(ACTION_NEXT_TRACK);
        nextPendingIntent = PendingIntent.getService(getApplicationContext(), 0, actionNextIntent, 0);

        Intent actionPrevIntent = new Intent(getApplicationContext(), MusicService.class);
        actionPrevIntent.setAction(ACTION_PREVIOUS_TRACK);
        prevPendingIntent = PendingIntent.getService(getApplicationContext(), 0, actionPrevIntent, 0);

        Intent actionPlayIntent = new Intent(getApplicationContext(), MusicService.class);
        actionPlayIntent.setAction(ACTION_CHANGE_MEDIA_STATE);
        ptPlayPause = PendingIntent.getService(getApplicationContext(), 0, actionPlayIntent, 0);

        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "my_package_channel_1")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(getCurrentTrack().getTitle())
                .setContentText(getCurrentTrack().getPublisherMetadata().getArtist())
                .setColor(getResources().getColor(R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_music_note_deep_orange_500_24dp)
                .setContentIntent(pendingIntentOpenApp);
        if (mBitmap == null) {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher_foreground));
        } else {
            mBuilder.setLargeIcon(mBitmap);
        }
    }

    /**
     * Su dung mBitmap de check neu 1 bai nhac dang choi, image da duoc load,
     * thi khi update notification anh se khong can load lai
     */
    private void setLargeIconBuilder() {

    }

    public List<Track> getListTrack() {
        if (mMusicPlayerManager == null) return null;
        return mMusicPlayerManager.getListTrack();
    }

    public void playTrackAtPosition(int position, Track... tracks) {
        if (mMusicPlayerManager == null) {
            mMusicPlayerManager = new MusicPlayerController(this);
        }
        mMusicPlayerManager.playTrackAtPosition(position, tracks);
    }

    public void addToNextUp(Track track) {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.addToNextUp(track);
    }

    public int getCurrentTrackPosition() {
        return mMusicPlayerManager != null ? mMusicPlayerManager.getCurrentTrackPosition() : null;
    }

    public void changeLoopType() {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.changeLoopType();
    }

    @PlaybackInfoListener.LoopType
    public int getLoopType() {
        if (mMusicPlayerManager == null) return PlaybackInfoListener.LoopType.NO_LOOP;
        return mMusicPlayerManager.getLoopType();
    }

    public void changeShuffleState() {
        if (mMusicPlayerManager == null) return;
        mMusicPlayerManager.changeShuffleState();
    }

    public boolean isShuffle() {
        return mMusicPlayerManager != null && mMusicPlayerManager.isShuffle();
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
