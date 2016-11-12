package timkranen.com.pinch.recyclerview.viewholders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import timkranen.com.pinch.R;
import timkranen.com.pinch.controllers.UserController;
import timkranen.com.pinch.model.GithubSubscriber;
import timkranen.com.pinch.receivers.NavigationReceiver;

/**
 * @author tim on [11/11/16]
 */
public class GithubSubscriberViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = GithubSubscriberViewHolder.class.getSimpleName();

    protected @BindView(R.id.subscriber_avatar_image)
    ImageView avatarImage;

    protected @BindView(R.id.subscriber_login_name)
    TextView loginName;

    private View content;

    private OnSubscriberClickedListener onSubscriberClickedListener;

    public GithubSubscriberViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        content = itemView;

        onSubscriberClickedListener = new OnSubscriberClickedListener();
        content.setOnClickListener(onSubscriberClickedListener);
    }

    public void update(final @NonNull Context context, @NonNull GithubSubscriber subscriber) {
        loginName.setText(subscriber.getLogin());

        //this makes sure the image is round
        Glide.with(context).load(subscriber.getAvatarUrl()).asBitmap().into(new BitmapImageViewTarget(avatarImage) {

            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                avatarImage.setImageDrawable(circularBitmapDrawable);
            }
        });


        onSubscriberClickedListener.setContext(context);
        onSubscriberClickedListener.setLoginName(subscriber.getLogin());

    }

    private class OnSubscriberClickedListener implements View.OnClickListener {

        private Context context;
        private String loginName;

        public void setContext(Context context) {
            this.context = context;
        }

        public void setLoginName(String loginName) { this.loginName = loginName; }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(NavigationReceiver.ACTION_NAVIGATE_TO);
            intent.putExtra(NavigationReceiver.EXTRA_CONTROLLER_ID, UserController.CONTROLLER_ID);
            intent.putExtra(NavigationReceiver.EXTRA_DETAIL_LOGIN_NAME, loginName);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }
}
