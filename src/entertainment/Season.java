package entertainment;

import user.User;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * Dictionary [user : givenRating]
     */
    private final Map<User, Double> ratings;

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

    public Map<User, Double> getRatings() {
        return ratings;
    }

    /**
     * implements getAverageRating inherited from Video
     * @return average rating of current season
     */
    public Double getAverageRating() {
        if (getRatings().size() == 0) {
            return (double) 0;
        }

        Double averageRating = (double) 0;

        for (Double rating : ratings.values()) {
            averageRating += rating;
        }

        return averageRating / ratings.size();
    }
}

