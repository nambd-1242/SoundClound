package com.framgia.mysoundcloud.screen.profile;

import com.framgia.mysoundcloud.data.model.Playlist;
import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.data.repository.TrackRepository;
import com.framgia.mysoundcloud.data.repository.UserRepository;
import com.framgia.mysoundcloud.data.source.TrackDataSource;
import com.framgia.mysoundcloud.data.source.UserDataSource;
import com.framgia.mysoundcloud.data.source.local.SharePreferences;

import java.util.List;

/**
 * Created by sonng266 on 15/03/2018.
 */

public class ProfilePresenter implements ProfileContract.Presenter{

    private ProfileContract.View mView;

    @Override
    public void setView(ProfileContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }


    @Override
    public void loadData() {
        User user = SharePreferences.getInstance().getUser();
        if(user != null){
            UserRepository.getInstance().getInforUser(user.getId(), new UserDataSource.ResultCallBack<User>() {
                @Override
                public void onSuccess(User user) {
                    mView.initData(user);
                }

                @Override
                public void onFailure(String mes) {

                }
            });
        }
    }

    @Override
    public void doLogout() {

    }
}
