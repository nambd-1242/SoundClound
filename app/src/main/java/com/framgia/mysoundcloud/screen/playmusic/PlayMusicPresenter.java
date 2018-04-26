package com.framgia.mysoundcloud.screen.playmusic;

/**
 * Created by sonng266 on 03/03/2018.
 */

public class PlayMusicPresenter implements PlayMusicContract.Presenter {

    private PlayMusicContract.View mView;

    @Override
    public void setView(PlayMusicContract.View view) {
        this.mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

}
