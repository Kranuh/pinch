package timkranen.com.pinch.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.Router;

import butterknife.BindView;
import butterknife.ButterKnife;
import timkranen.com.pinch.R;
import timkranen.com.pinch.controllers.BindingController;
import timkranen.com.pinch.controllers.UserController;
import timkranen.com.pinch.controllers.MainController;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_container)
    FrameLayout mainContainer;

    @BindView(R.id.toolbar)
    Toolbar toolBar;

    private Router _router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);

        _router = Conductor.attachRouter(this, mainContainer, savedInstanceState);
        if (!_router.hasRootController()) {
            _router.setRoot(new MainController());
            addControllerChangeListener();
        }
    }

    /**
     * This will handle change events on an activity level,
     * with this we can set the back button etc.
     *
     * We hide the main container for the transition to avoid visual artifacts
     */
    private void addControllerChangeListener() {
        _router.addChangeListener(new ControllerChangeHandler.ControllerChangeListener() {
            @Override
            public void onChangeStarted(Controller to, Controller from, boolean isPush, ViewGroup container, ControllerChangeHandler handler) {
                if (to instanceof BindingController) {
                    setBackButtonVisibility(to instanceof UserController);
                }
            }

            @Override
            public void onChangeCompleted(Controller to, Controller from, boolean isPush, ViewGroup container, ControllerChangeHandler handler) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (_router != null) {
                    //if we had deeper detail views we would pop the stack until MainController
                    _router.popToRoot();
                    return true;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setBackButtonVisibility(boolean isVisible) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isVisible);
        getSupportActionBar().setDisplayShowHomeEnabled(isVisible);
    }

    @Override
    public void onBackPressed() {
        if (!_router.handleBack()) {
            super.onBackPressed();
        }
    }
}
