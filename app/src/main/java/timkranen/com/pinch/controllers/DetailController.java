package timkranen.com.pinch.controllers;

import android.support.annotation.NonNull;
import android.view.View;

import timkranen.com.pinch.R;

/**
 * @author tim on [11/12/16]
 */
public class DetailController extends BindingController {
    private static final String TAG = DetailController.class.getSimpleName();


    @Override
    protected int getLayoutResId() {
        return R.layout.detail_controller;
    }

    @Override
    protected void onViewBound(@NonNull View contentView) {

    }

    @Override
    protected int getControllerID() {
        return 1;
    }
}
