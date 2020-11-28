package entertainment;

import user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Movie extends Video {
    /**
     * Dictionary [user : givenRating]
     */
    private Map<User, Integer> ratings;

    public Map<User, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(Map<User, Integer> ratings) {
        this.ratings = ratings;
    }

    public Movie(final String title, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, cast, genres);
        this.setDuration(duration);
    }
}
