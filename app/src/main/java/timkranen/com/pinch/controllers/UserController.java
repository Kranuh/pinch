package timkranen.com.pinch.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluelinelabs.conductor.rxlifecycle.ControllerEvent;
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
import timkranen.com.pinch.utils.StateViewManager;

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

    @BindView(R.id.gists_count)
    protected CountCircularView gistsCount;

    @BindView(R.id.followers_count)
    protected CountCircularView followersCount;

    @BindView(R.id.following_count)
    protected CountCircularView followingCount;

    @BindView(R.id.email_button)
    protected AppCompatButton emailButton;

    @BindView(R.id.user_content_container)
    protected ViewGroup userContentContainer;

    @BindView(R.id.user_progress)
    protected ProgressBar userProgressBar;

    @BindView(R.id.user_error_state)
    protected TextView userErrorState;

    private String loginName;
    private boolean didUserFetch = false;
    private StateViewManager stateViewManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.user_controller;
    }

    @Override
    protected void onViewBound(@NonNull View contentView) {
        stateViewManager = new StateViewManager(userContentContainer, userProgressBar, userErrorState);
        stateViewManager.setInProgress(true);
        startUserFetch();
    }

    private void startUserFetch() {
        //first we get the cached user
        getCachedUser();

        //fetch a new user object
        new GithubDataFetcher(this, ControllerEvent.DESTROY).fetchUser(this.loginName, new RequestListener() {
            @Override
            public void onRequestCompleted() {
                getCachedUser();
            }

            @Override
            public void onRequestFailed(Throwable e) {
                Log.e(TAG, e.getMessage());
                stateViewManager.setInProgress(false);
                if(!didUserFetch) {
                    if (e.getMessage().contains("Forbidden")) {
                        stateViewManager.setErrorState(getActivity().getString(R.string.no_access_error));
                    } else {
                        stateViewManager.setErrorState(getActivity().getString(R.string.failed_user_fetch));
                    }
                }
            }

            @Override
            public void onRequestEmpty() {

            }
        });

    }

    private void getCachedUser() {
        LocalStorageManager localStorageManager = new LocalStorageManager(this, ControllerEvent.DESTROY);
        if(localStorageManager.hasCachedUser(this.loginName)) {
            localStorageManager.getCachedUser(this.loginName)
                    .subscribe(new Observer<GithubUser>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, e.getMessage());

                            //this error is not failing so we don't show an error message here
                        }

                        @Override
                        public void onNext(GithubUser githubUser) {
                            stateViewManager.setInProgress(false);
                            userFetched(githubUser);
                        }
                    });
        }
    }

    private void userFetched(GithubUser githubUser) {
        didUserFetch = true;

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

       setCounts(githubUser);

        if(githubUser.getEmail() != null && !githubUser.getEmail().isEmpty()) {
            emailButton.setText("Email " + githubUser.getLogin());
            emailButton.setOnClickListener(new EmailClickListener(githubUser.getEmail()));
        } else {
            emailButton.setVisibility(View.GONE);
        }
    }

    private void setCounts(GithubUser user) {
        repoCount.setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        repoCount.setCount(user.getPublicRepos());
        repoCount.setTitle(getActivity().getString(R.string.public_repos));

        gistsCount.setColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        gistsCount.setCount(user.getPublicGists());
        gistsCount.setTitle(getActivity().getString(R.string.public_gists));

        followersCount.setColor(ContextCompat.getColor(getActivity(), R.color.deep_orange));
        followersCount.setCount(user.getFollowers());
        followersCount.setTitle(getActivity().getString(R.string.followers));

        followingCount.setColor(ContextCompat.getColor(getActivity(), R.color.lime));
        followingCount.setCount(user.getFollowing());
        followingCount.setTitle(getActivity().getString(R.string.following));
    }

    @Override
    protected int getControllerID() {
        return 1;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    private class EmailClickListener implements View.OnClickListener {

        private String email;

        public EmailClickListener(String email) {
            this.email = email;
        }

        @Override
        public void onClick(View view) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }
    }
}
