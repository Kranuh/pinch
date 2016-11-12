package timkranen.com.pinch.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author tim on [11/12/16]
 */
public class GithubUser implements Subscriber, User {
    private static final String TAG = GithubUser.class.getSimpleName();

    private String login;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("public_repos")
    private int publicRepos;

    @SerializedName("public_gists")
    private int publicGists;

    private String email;

    private int followers;

    private int following;

    public GithubUser(String login, String avatarUrl, Date createdAt, int publicRepos, int publicGists, String email, int followers, int following) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.publicRepos = publicRepos;
        this.publicGists = publicGists;
        this.email = email;
        this.followers = followers;
        this.following = following;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public int getPublicRepos() {
        return publicRepos;
    }

    @Override
    public int getPublicGists() {
        return publicGists;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public int getFollowers() {
        return followers;
    }

    @Override
    public int getFollowing() {
        return following;
    }
}
