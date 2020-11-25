package entertainment;

import user.User;

import java.util.HashMap;
import java.util.Map;

// aici il extind cum ma taie pl mea

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;

    public Map<User, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(Map<User, Integer> ratings) {
        this.ratings = ratings;
    }

    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * Dictionary [user : givenRating]
     */
    private Map<User, Integer> ratings;

    // all that counts, brothers
    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new HashMap<>();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + duration
                + '}';
    }
}

