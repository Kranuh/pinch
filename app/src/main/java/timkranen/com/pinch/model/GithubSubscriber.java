package timkranen.com.pinch.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author tim on [11/9/16]
 */
public class GithubSubscriber implements Subscriber {
    private static final String TAG = GithubSubscriber.class.getSimpleName();

    private String login;

    @SerializedName("avatar_url")
    private String avatarUrl;

    public GithubSubscriber(String login, String avatarUrl) {
        this.login = login;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getAvatarUrl() {
        return avatarUrl;
    }
}
