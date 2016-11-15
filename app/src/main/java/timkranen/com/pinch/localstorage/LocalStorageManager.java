package timkranen.com.pinch.localstorage;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.bluelinelabs.conductor.rxlifecycle.ControllerEvent;
import com.bluelinelabs.conductor.rxlifecycle.RxController;
import com.trello.rxlifecycle.RxLifecycle;

import java.util.List;

import io.paperdb.Paper;
import rx.Observable;
import rx.Subscriber;
import timkranen.com.pinch.model.GithubSubscriber;
import timkranen.com.pinch.model.GithubUser;

/**
 * @author tim on [11/10/16]
 *         <p>
 *         Pretty simple implementation of some kind of local storage to transfer data in te app
 *         I didn't go for a very complex or scalable solution (just picked a convenient) library, since
 *         the app isn't that big. I could've used a static class that passes data around but that is pretty
 *         ugly.
 */
public class LocalStorageManager {
    private static final String TAG = LocalStorageManager.class.getSimpleName();

    public static final String KEY_CACHED_SUBSCRIBERS = TAG + ".cachedSubscribers";


    private final RxController lifeCycleController;
    private final ControllerEvent controllerEvent;

    /**
     * Creates a LocalStorageManager, all operations done within
     * the LocalStorageManager are executed on;
     * @param lifeCycleController the controller on which the lifecycle depends
     * @param controllerEvent canceled when the controllerEvent is triggered
     */
    public LocalStorageManager(RxController lifeCycleController, ControllerEvent controllerEvent) {
        this.lifeCycleController = lifeCycleController;
        this.controllerEvent = controllerEvent;
    }

    public Observable<List<GithubSubscriber>> getCachedSubscribers() {
        Observable<List<GithubSubscriber>> observable = Observable.create(new Observable.OnSubscribe<List<GithubSubscriber>>() {
            @Override
            public void call(Subscriber<? super List<GithubSubscriber>> subscriber) {
                List<GithubSubscriber> cachedSubscribers = Paper.book().read(KEY_CACHED_SUBSCRIBERS);
                if (cachedSubscribers != null && !cachedSubscribers.isEmpty()) {
                    subscriber.onNext(cachedSubscribers);
                } else {
                    subscriber.onError(new IllegalStateException("No cached subscribers"));
                }
            }
        });

        observable.compose(lifeCycleController.<List<GithubSubscriber>>bindUntilEvent(controllerEvent));
        return observable;
    }

    public Observable<GithubUser> getCachedUser(final @NonNull String loginName) {
        Observable<GithubUser> observable = Observable.create(new Observable.OnSubscribe<GithubUser>() {
            @Override
            public void call(Subscriber<? super GithubUser> subscriber) {
                if (!loginName.isEmpty() && hasCachedUser(loginName)) {
                    GithubUser githubUser = Paper.book().read(loginName);

                    if (subscriber != null)
                        subscriber.onNext(githubUser);
                } else {
                    subscriber.onError(new IllegalStateException("No cached users"));
                }
            }
        });

        observable.compose(lifeCycleController.<GithubUser>bindUntilEvent(controllerEvent));
        return observable;
    }

    public boolean hasCachedSubscribers() {
        return Paper.book().exist(KEY_CACHED_SUBSCRIBERS);
    }

    public boolean hasCachedUser(String loginName) {
        return Paper.book().exist(loginName);
    }

    public Observable<List<GithubSubscriber>> cacheSubscribers(@NonNull final List<GithubSubscriber> githubSubscribers) {
        Observable<List<GithubSubscriber>> observable = Observable.create(new Observable.OnSubscribe<List<GithubSubscriber>>() {
            @Override
            public void call(Subscriber<? super List<GithubSubscriber>> subscriber) {
                if (githubSubscribers == null || githubSubscribers.isEmpty()) {
                    subscriber.onError(new IllegalStateException("githubSubscribers can't be null or empty"));
                }

                Paper.book().write(KEY_CACHED_SUBSCRIBERS, githubSubscribers);
                subscriber.onNext(githubSubscribers);
            }
        });

        observable.compose(lifeCycleController.<List<GithubSubscriber>>bindUntilEvent(controllerEvent));
        return observable;
    }

    public Observable<GithubUser> cacheUser(@NonNull final GithubUser githubUser) {
        Observable<GithubUser> observable = Observable.create(new Observable.OnSubscribe<GithubUser>() {
            @Override
            public void call(Subscriber<? super GithubUser> subscriber) {
                if (githubUser == null) {
                    subscriber.onError(new IllegalStateException("GithubUser can't be null"));
                }

                Paper.book().write(githubUser.getLogin(), githubUser);
                subscriber.onNext(githubUser);
            }
        });

        observable.compose(lifeCycleController.<GithubUser>bindUntilEvent(controllerEvent));
        return observable;
    }
}
