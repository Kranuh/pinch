package timkranen.com.pinch.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;

import butterknife.BindView;
import butterknife.ButterKnife;
import timkranen.com.pinch.R;
import timkranen.com.pinch.api.RequestListener;
import timkranen.com.pinch.api.github.GithubDataFetcher;
import timkranen.com.pinch.controllers.SplashController;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.splash_container)
    FrameLayout splashContainer;

    private Router _router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        _router = Conductor.attachRouter(this, splashContainer, savedInstanceState);
        if(!_router.hasRootController()) {
            _router.setRoot(new SplashController());
        }
    }

    public void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
