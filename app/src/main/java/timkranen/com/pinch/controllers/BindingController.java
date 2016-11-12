package timkranen.com.pinch.controllers;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import timkranen.com.pinch.receivers.NavigationReceiver;

/**
 * @author tim on [11/11/16]
 *
 * Class that handles Butterknife binding/unbinding
 */
public abstract class BindingController extends Controller {
    private static final String TAG = BindingController.class.getSimpleName();

    protected Unbinder unbinder;

    public static int CONTROLLER_ID;

    private NavigationReceiver _navigationReceiver;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View contentView = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, contentView);

        CONTROLLER_ID = getControllerID();
        _navigationReceiver = new NavigationReceiver(getRouter());

        onViewBound(contentView);
        return contentView;
    }

    /**
     * As far as lifecycle goes, this is like onResume
     * @param view
     */
    @Override
    protected void onAttach(@NonNull View view) {
        if(_navigationReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(_navigationReceiver, NavigationReceiver.getIntentFilter());
        }
    }

    /**
     * This is like onPause
     * @param view
     */
    @Override
    protected void onDetach(@NonNull View view) {
        if(_navigationReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(_navigationReceiver);
        }
    }

    /**
     * Get the layout resource ID of the current controller
     * this view is inflated
     * @return the layout resource ID
     */
    protected abstract @LayoutRes int getLayoutResId();

    /**
     * Called when the layout (from getLayoutResID()) is inflated and
     * bound to ButterKnife
     * @param contentView
     */
    protected abstract void onViewBound(@NonNull View contentView);

    /**
     * Controller ID is used for navigation, the ID determines which
     * controller is going to be pushed
     * @return
     */
    protected abstract int getControllerID();

    @Override
    protected void onDestroyView(View view) {
        super.onDestroyView(view);

        unbinder.unbind();
    }
}
