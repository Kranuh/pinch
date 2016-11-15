package timkranen.com.pinch.controllers;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluelinelabs.conductor.rxlifecycle.ControllerEvent;
import com.trello.rxlifecycle.RxLifecycle;

import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.Subscription;
import timkranen.com.pinch.R;
import timkranen.com.pinch.localstorage.LocalStorageManager;
import timkranen.com.pinch.model.GithubSubscriber;
import timkranen.com.pinch.model.Subscriber;
import timkranen.com.pinch.recyclerview.adapters.GithubSubscriberAdapter;
import timkranen.com.pinch.utils.StateViewManager;

/**
 * @author tim on [11/11/16]
 *
 * MainController that shows the subscriber recyclerview
 */
public class MainController extends BindingController {
    private static final String TAG = MainController.class.getSimpleName();

    @BindView(R.id.subscriber_recycler_view)
    RecyclerView subscriberRecyclerView;

    @BindView(R.id.main_error_state)
    TextView mainErrorText;

    @BindView(R.id.main_progress)
    ProgressBar mainProgressBar;

    private StateViewManager stateViewManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.main_controller;
    }

    @Override
    protected void onViewBound(View contentView) {
        stateViewManager = new StateViewManager(subscriberRecyclerView, mainProgressBar, mainErrorText);

        stateViewManager.setInProgress(true);
        getCachedSubscribers();
    }

    @Override
    protected int getControllerID() {
        return 0;
    }

    /**
     * Fills the recyclerview with appropriate data
     * if the data is not available we show an empty text
     */
    private void getCachedSubscribers() {
        new LocalStorageManager(this, ControllerEvent.DESTROY).getCachedSubscribers()
                .subscribe(new Observer<List<GithubSubscriber>>() {
            @Override
            public void onCompleted() {
                //this is never called since its a list of items, we get 1 onNext
            }

            @Override
            public void onError(Throwable e) {
                stateViewManager.setInProgress(false);
                stateViewManager.setErrorState(getActivity().getString(R.string.no_subscribers_error));
            }

            @Override
            public void onNext(List<GithubSubscriber> githubSubscribers) {
                buildRecyclerView(githubSubscribers);
                stateViewManager.setInProgress(false);
            }
        });
    }

    private void buildRecyclerView(@NonNull List<GithubSubscriber> githubSubscribers) {
        subscriberRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        subscriberRecyclerView.setAdapter(new GithubSubscriberAdapter(getActivity(), githubSubscribers));
    }
}
