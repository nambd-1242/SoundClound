package com.framgia.mysoundcloud.widget;

import android.app.Activity;

import com.framgia.mysoundcloud.data.model.Track;



public interface DialogManagerInterface {

    void dialogMessage(String msg, String title);

    void dialogButton(String msg, String title,
                      String positiveButton, String negativeButton, DialogListener mListener);

    void dismissDialog();

    void dialogAddToPlaylist(Activity activity, Track... tracks);

    interface DialogListener {
        void onDialogPositiveClick();

        void onDialogNegativeClick();
    }
}
