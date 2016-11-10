package timkranen.com.pinch.api.github;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timkranen.com.pinch.api.RequestListener;
import timkranen.com.pinch.api.RestAdapter;
import timkranen.com.pinch.model.GithubSubscriber;

/**
 * @author tim on [11/9/16]
 */
public class SubscriberFetcher {
    private static final String TAG = SubscriberFetcher.class.getSimpleName();

    public interface SubscribersFetchedListener {
        void onFetchCompleted();
        void onFetchedError(Throwable e);
        void onNoResult();
    }

    public synchronized static void fetchSubscribers(String repoName, final RequestListener subscriberRequestListener) {
        SubscriberApi subscriberApi = RestAdapter.getAdapter(SubscriberApi.class, SubscriberApi.BASE_URL);
        subscriberApi.getSubscribers(repoName)
                .subscribeOn(Schedulers.io())
                .flatMap()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GithubSubscriber>>() {
                    @Override
                    public void onCompleted() {
                        //we don't use this since onNext is going to deliver all the results at once
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriberRequestListener.onRequestFailed(e);
                    }

                    @Override
                    public void onNext(List<GithubSubscriber> githubSubscribers) {
                        if(githubSubscribers != null && !githubSubscribers.isEmpty()) {
                            subscriberRequestListener.onRequestCompleted();
                        } else {
                            subscriberRequestListener.onRequestEmpty();
                        }
                    }
                });
    }
}
