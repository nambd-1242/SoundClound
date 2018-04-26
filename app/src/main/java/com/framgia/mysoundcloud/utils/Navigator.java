package com.framgia.mysoundcloud.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.framgia.mysoundcloud.R;

/**
 * Created by sonng266 on 25/02/2018.
 */

public class Navigator {

    private Activity mActivity;
    private Fragment mFragment;

    public Navigator(Activity activity) {
        this.mActivity = activity;
    }

    public Navigator(Fragment fragment) {
        this.mFragment = fragment;
        if (mFragment != null) {
            mActivity = mFragment.getActivity();
        }
    }

    private void startActivity(Intent intent, boolean isAnimate) {
        if (intent != null && mActivity != null) {
            mActivity.startActivity(intent);
            if (isAnimate) {
                setActivityTransactionAnimation(ActivityTransition.START);
            }
        }
    }

    public void startActivity(Class<? extends Activity> classz, boolean isAnimate) {
        if (mActivity != null) {
            Intent intent = new Intent(mActivity, classz);
            startActivity(intent, isAnimate);
        }
    }

    public void startActivity(Class<? extends Activity> classz, Bundle args, boolean isAnimate) {
        if (mActivity != null) {
            Intent intent = new Intent(mActivity, classz);
            intent.putExtras(args);
            startActivity(intent, isAnimate);
        }
    }

    public void finishActivity() {
        if (mActivity != null) {
            mActivity.finish();
            setActivityTransactionAnimation(ActivityTransition.FINISH);
        }
    }

    // Fragment

    public void goNextFragment(@IdRes int containerViewId, Fragment fragment,
                               boolean addToBackStack, int animation, String tag) {
        if (mFragment == null) return;

        FragmentTransaction transaction = mFragment.getFragmentManager().beginTransaction();
        setFragmentTransactionAnimation(transaction, animation);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.replace(containerViewId, fragment, tag);
        transaction.commitAllowingStateLoss();
        mFragment.getFragmentManager().executePendingTransactions();
    }

    public boolean goBackFragment() {
        if (mFragment == null) return false;
        boolean isShowPrevious = mFragment.getFragmentManager().getBackStackEntryCount() > 1;
        if (isShowPrevious) {
            mFragment.getFragmentManager().popBackStackImmediate();
        }
        return isShowPrevious;
    }

    // Child fragment

    public void goNextChildFragment(@IdRes int containerViewId, Fragment fragment,
                                    boolean addToBackStack, int animation, String tag) {
        FragmentTransaction transaction = mFragment.getChildFragmentManager().beginTransaction();
        setFragmentTransactionAnimation(transaction, animation);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.replace(containerViewId, fragment, tag);
        transaction.commitAllowingStateLoss();
        mFragment.getChildFragmentManager().executePendingTransactions();
    }

    public boolean goBackChildFragment() {
        boolean isShowPrevious = mFragment.getChildFragmentManager().getBackStackEntryCount() > 1;
        if (isShowPrevious) {
            mFragment.getChildFragmentManager().popBackStackImmediate();
        }
        return isShowPrevious;
    }

    private void setFragmentTransactionAnimation(
            FragmentTransaction transaction, @NavigateAnim int animation) {
        switch (animation) {
            case NavigateAnim.FADED:
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case NavigateAnim.RIGHT_LEFT:
                transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case NavigateAnim.LEFT_RIGHT:
                transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                        R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case NavigateAnim.BOTTOM_UP:
                transaction.setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_top_out,
                        R.anim.slide_top_in, R.anim.slide_bottom_out);
                break;
            case NavigateAnim.NONE:
                break;
            default:
                break;
        }
    }

    private void setActivityTransactionAnimation(@ActivityTransition int animation) {
        switch (animation) {
            case ActivityTransition.START:
                mActivity.overridePendingTransition(R.anim.translate_left, R.anim.translate_still);
                break;
            case ActivityTransition.FINISH:
                mActivity.overridePendingTransition(R.anim.translate_still, R.anim.translate_right);
                break;
            case ActivityTransition.NONE:
                break;
            default:
                break;
        }
    }

    @IntDef({
            NavigateAnim.RIGHT_LEFT, NavigateAnim.BOTTOM_UP, NavigateAnim.FADED, NavigateAnim.NONE,
            NavigateAnim.LEFT_RIGHT
    })
    @interface NavigateAnim {
        int NONE = 0;
        int RIGHT_LEFT = 1;
        int BOTTOM_UP = 2;
        int FADED = 3;
        int LEFT_RIGHT = 4;
    }

    @IntDef({ActivityTransition.NONE, ActivityTransition.START, ActivityTransition.FINISH})
    @interface ActivityTransition {
        int NONE = 0;
        int START = 1;
        int FINISH = 2;
    }

}
