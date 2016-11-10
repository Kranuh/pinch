package timkranen.com.pinch.api.github;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import timkranen.com.pinch.model.GithubSubscriber;

/**
 * @author tim on [11/9/16]
 *
 * Service class for Retrofit, defines subscriber API endpoint
 */
public interface SubscriberApi {
    String BASE_URL = "https://api.github.com/repos/android/";

    @GET("{repoName}/subscribers")
    Observable<List<GithubSubscriber>> getSubscribers(@Path("repoName") String repoName);
}
