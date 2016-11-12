package timkranen.com.pinch.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import timkranen.com.pinch.R;
import timkranen.com.pinch.api.RequestListener;
import timkranen.com.pinch.api.github.GithubDataFetcher;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        GithubDataFetcher.fetchSubscribers("android/platform_frameworks_base", new RequestListener() {
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
