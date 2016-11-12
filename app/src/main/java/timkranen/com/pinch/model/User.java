package timkranen.com.pinch.model;

import java.util.Date;

/**
 * @author tim on [11/12/16]
 */

public interface User {
    Date getCreatedAt();
    int getPublicRepos();
    int getPublicGists();
    String getEmail();
    int getFollowers();
    int getFollowing();
}
