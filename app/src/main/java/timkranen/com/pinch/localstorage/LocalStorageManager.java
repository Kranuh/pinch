package timkranen.com.pinch.localstorage;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.util.List;

import io.paperdb.Paper;
import rx.Observable;
import rx.Subscriber;
import timkranen.com.pinch.model.GithubSubscriber;

/**
 * @author tim on [11/10/16]
 *
 * Pretty simple implementation of some kind of local storage to transfer data in te app
 * I didn't go for a very complex or scalable solution (just picked a convenient) library, since
 * the app isn't that big. I could've used a static class that passes data around but that is pretty
 * ugly.
 */
public class LocalStorageManager {
    private static final String TAG = LocalStorageManager.class.getSimpleName();

    public static final String KEY_CACHED_SUBSCRIBERS = TAG + ".cachedSubscribers";

    public Observable<List<GithubSubscriber>> getCachedSubscribers() {
         Observable<List<GithubSubscriber>> cacheObservable = Observable.create(new Observable.OnSubscribe<List<GithubSubscriber>>() {
             @Override
             public void call(Subscriber<? super List<GithubSubscriber>> subscriber) {
                 List<GithubSubscriber> cachedSubscribers = Paper.book().read(KEY_CACHED_SUBSCRIBERS);
                 if(cachedSubscribers != null && !cachedSubscribers.isEmpty()) {
                     subscriber.onNext(cachedSubscribers);
                 } else {
                     subscriber.onError(new IllegalStateException("No cached subscribers"));
                 }
             }
         });

        return cacheObservable;
    }

    public boolean hasCachedSubscribers() {
        return Paper.book().exist(KEY_CACHED_SUBSCRIBERS);
    }

    public Observable<List<GithubSubscriber>> cacheSubscribers(@NonNull final List<GithubSubscriber> githubSubscribers) {
        Observable<List<GithubSubscriber>> cacheObservable = Observable.create(new Observable.OnSubscribe<List<GithubSubscriber>>() {
            @Override
            public void call(Subscriber<? super List<GithubSubscriber>> subscriber) {
                if(githubSubscribers == null || githubSubscribers.isEmpty()) {
                    subscriber.onError(new IllegalStateException("githubSubscribers can't be null or empty"));
                }

                Paper.book().write(KEY_CACHED_SUBSCRIBERS, githubSubscribers);
                subscriber.onNext(githubSubscribers);
            }
        });

        return cacheObservable;
    }
}
