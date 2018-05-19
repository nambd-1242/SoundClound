package com.framgia.mysoundcloud.screen;



public interface BasePresenter<T> {

    void setView(T view);

    void onStart();

    void onStop();
}
