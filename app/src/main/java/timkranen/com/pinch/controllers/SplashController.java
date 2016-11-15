package timkranen.com.pinch.controllers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.bluelinelabs.conductor.rxlifecycle.ControllerEvent;

import timkranen.com.pinch.R;
import timkranen.com.pinch.activities.SplashActivity;
import timkranen.com.pinch.api.RequestListener;
import timkranen.com.pinch.api.github.GithubDataFetcher;

/**
 * @author tim on [11/15/16]
 */
public class SplashController extends BindingController {
    private static final String TAG = SplashController.class.getSimpleName();

    @Override
    protected int getLayoutResId() {
        return R.layout.splash_controller;
    }

    @Override
    protected void onViewBound(@NonNull View contentView) {
        new GithubDataFetcher(this, ControllerEvent.DESTROY).fetchSubscribers("android/platform_frameworks_base", new RequestListener() {
            @Override
            public void onRequestCompleted() {
                launchMainActivity();
            }

            @Override
            public void onRequestFailed(Throwable e) {
                //show error state
            }

            @Override
            public void onRequestEmpty() {

            }
        });
    }

    private void launchMainActivity() {
        Activity activity = getActivity();
        if(activity != null && activity instanceof SplashActivity) {
            ((SplashActivity) activity).launchMainActivity();
        }
    }

    @Override
    protected int getControllerID() {
        return 0;
    }
}
