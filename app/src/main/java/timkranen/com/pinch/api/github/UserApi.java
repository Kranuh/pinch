package timkranen.com.pinch.api.github;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import timkranen.com.pinch.model.GithubSubscriber;
import timkranen.com.pinch.model.GithubUser;

/**
 * @author tim on [11/12/16]
 */

public interface UserApi {
    String BASE_URL = "https://api.github.com/users/";

    @GET("{loginName}")
    Observable<GithubUser> getUser(@Path("loginName") String loginName);
}
