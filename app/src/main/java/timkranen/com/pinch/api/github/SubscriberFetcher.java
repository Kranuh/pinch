package timkranen.com.pinch.api.github;

import android.util.Log;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timkranen.com.pinch.api.RequestListener;
import timkranen.com.pinch.api.RestAdapter;
import timkranen.com.pinch.localstorage.LocalStorageManager;
import timkranen.com.pinch.model.GithubSubscriber;

/**
 * @author tim on [11/9/16]
 *
 * The main communication class for all subscriber API interaction
 *
 * When communicating with multiple endpoints this should become more abstracted
 *
 * The RxJava stuff is really weird to read if you have never seen it before, I haven't used it very much myself
 * but currently always use it in personal projects, I've commented the basics of how it works
 */
public class SubscriberFetcher {
    private static final String TAG = SubscriberFetcher.class.getSimpleName();

    public interface SubscribersFetchedListener {
        void onFetchCompleted();

        void onFetchedError(Throwable e);

        void onNoResult();
    }

    public synchronized static void fetchSubscribers(String repoName, final RequestListener subscriberRequestListener) {
        SubscriberApi subscriberApi = RestAdapter.getDebugAdapter(SubscriberApi.class, SubscriberApi.BASE_URL);
        subscriberApi.getSubscribers(repoName)
                //we make sure that all operations that are blocking are called off the main thread
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                //we get a result from the api, save it to cache, and pass it on to the subscribe call
                .flatMap(new Func1<List<GithubSubscriber>, Observable<List<GithubSubscriber>>>() {
                    @Override
                    public Observable<List<GithubSubscriber>> call(List<GithubSubscriber> githubSubscribers) {
                        if(githubSubscribers != null && !githubSubscribers.isEmpty()) {
                            return new LocalStorageManager().cacheSubscribers(githubSubscribers);
                        }

                        //callback for when the request failed, e.a. when we don't have valid data
                        subscriberRequestListener.onRequestFailed(new IllegalStateException("Request returned no subscribers."));
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                //we make sure that as soon as a result comes in we execute whatever comes after on the main thread
                //this means that whenever a callback is fired to the RequestListener it can be used on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                //everything is complete and we can fire a callback to get notified that all fetching/saving is done
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
                        if (githubSubscribers != null && !githubSubscribers.isEmpty()) {
                            Log.d(TAG, "github subscribers fetched");
                            subscriberRequestListener.onRequestCompleted();
                        } else {
                            subscriberRequestListener.onRequestEmpty();
                        }
                    }
                });
    }
}


