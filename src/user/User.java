package user;

import java.util.ArrayList;
import java.util.Map;

public final class User {
    /**
     * User's username
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * The history of the movies seen
     */
    private final Map<String, Integer> history;
    /**
     * Movies added to favorites
     */
    private final ArrayList<String> favoriteVideos;
    /**
     * Total no. of ratings given by user (to all shows)
     */
    private int noRatingsGiven;

    public User(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteVideos) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteVideos = favoriteVideos;
        this.history = history;

        this.noRatingsGiven = 0; // initially, the user didn't rate anyone
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public ArrayList<String> getFavoriteVideos() {
        return favoriteVideos;
    }

    public int getNoRatingsGiven() {
        return noRatingsGiven;
    }

    /**
     * increments no of ratings given by this user
     */
    public void incNoRatingsGiven() {
        noRatingsGiven++;
    }

}
