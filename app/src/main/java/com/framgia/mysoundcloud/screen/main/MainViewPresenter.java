package com.framgia.mysoundcloud.screen.main;

/**
 * Created by sonng266 on 26/02/2018.
 */

public class MainViewPresenter implements MainViewConstract.Presenter {

    private MainViewConstract.View mView;

    @Override
    public void setView(MainViewConstract.View view) {
        this.mView = view;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {

    }
}
