package com.framgia.mysoundcloud.screen.playmusic;


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
