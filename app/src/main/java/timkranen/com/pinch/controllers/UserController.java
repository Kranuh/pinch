package timkranen.com.pinch.controllers;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import butterknife.BindView;
import rx.Observer;
import timkranen.com.pinch.R;
import timkranen.com.pinch.api.RequestListener;
import timkranen.com.pinch.api.github.GithubDataFetcher;
import timkranen.com.pinch.localstorage.LocalStorageManager;
import timkranen.com.pinch.model.GithubUser;
import timkranen.com.pinch.ui.views.CountCircularView;

/**
 * @author tim on [11/12/16]
 */
public class UserController extends BindingController {
    private static final String TAG = UserController.class.getSimpleName();

    @BindView(R.id.user_created_at)
    protected TextView createdAtTextView;

    @BindView(R.id.user_avatar)
    protected ImageView avatarImageView;

    @BindView(R.id.user_login_name)
    protected TextView loginNameTextView;

    @BindView(R.id.repo_count)
    protected CountCircularView repoCount;

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
        createdAtTextView.setText("User since: " + githubUser.getCreatedAt().toString());
        loginNameTextView.setText(githubUser.getLogin());

        //this makes sure the image is round
        Glide.with(getActivity()).load(githubUser.getAvatarUrl()).asBitmap().into(new BitmapImageViewTarget(avatarImageView) {

            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                avatarImageView.setImageDrawable(circularBitmapDrawable);
            }
        });

        repoCount.setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        repoCount.setCount(githubUser.getPublicRepos());
        repoCount.setTitle("Public Repos");

    }

    @Override
    protected int getControllerID() {
        return 1;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
