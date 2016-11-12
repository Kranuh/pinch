package timkranen.com.pinch.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;

import butterknife.BindView;
import butterknife.ButterKnife;
import timkranen.com.pinch.R;
import timkranen.com.pinch.controllers.MainController;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_container)
    FrameLayout mainContainer;

    private Router _router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        _router = Conductor.attachRouter(this, mainContainer, savedInstanceState);
        if(!_router.hasRootController()) {
            _router.setRoot(new MainController());
        }
    }

    @Override
    public void onBackPressed() {
        if (!_router.handleBack()) {
            super.onBackPressed();
        }
    }
}
