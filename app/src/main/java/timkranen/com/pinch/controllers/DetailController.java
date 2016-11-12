package timkranen.com.pinch.controllers;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import rx.Observer;
import timkranen.com.pinch.R;
import timkranen.com.pinch.api.RequestListener;
import timkranen.com.pinch.api.github.GithubDataFetcher;
import timkranen.com.pinch.localstorage.LocalStorageManager;
import timkranen.com.pinch.model.GithubUser;

/**
 * @author tim on [11/12/16]
 */
public class DetailController extends BindingController {
    private static final String TAG = DetailController.class.getSimpleName();

    @BindView(R.id.created_at)
    protected TextView createdAtTextView;

    private String loginName;

    @Override
    protected int getLayoutResId() {
        return R.layout.detail_controller;
    }

    @Override
    protected void onViewBound(@NonNull View contentView) {
        startUserFetch();
    }

    private void startUserFetch() {
        //first we get the cached user
        getCachedUser();

        //fetch a new user object
        GithubDataFetcher.fetchUser(this.loginName, new RequestListener() {
            @Override
            public void onRequestCompleted() {
                getCachedUser();
            }

            @Override
            public void onRequestFailed(Throwable e) {
                Log.e(TAG, "Request failed, " + e.getMessage());
                //show error state
            }

            @Override
            public void onRequestEmpty() {

            }
        });

    }

    private void getCachedUser() {
        LocalStorageManager localStorageManager = new LocalStorageManager();
        if(localStorageManager.hasCachedUser(this.loginName)) {
            localStorageManager.getCachedUser(this.loginName)
                    .subscribe(new Observer<GithubUser>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(GithubUser githubUser) {
                            userFetched(githubUser);
                        }
                    });
        }
    }

    private void userFetched(GithubUser githubUser) {
        createdAtTextView.setText(githubUser.getCreatedAt().toString());
    }

    @Override
    protected int getControllerID() {
        return 1;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
