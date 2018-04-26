package com.framgia.mysoundcloud.screen;

/**
 * Created by sonng266 on 26/02/2018.
 */

public interface BasePresenter<T> {
    void setView(T view);

    void onStart();

    void onStop();
}
