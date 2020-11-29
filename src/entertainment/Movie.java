package entertainment;

import user.User;

import java.util.ArrayList;
import java.util.Map;

public class Movie extends Video {
    @Override
    public Double getAverageRating() {
        Double averageRating = (double) 0;

        for (Double rating : getRatings().values()) {
            averageRating += rating;
        }

        return averageRating / getRatings().size();
    }

    public Movie(final String title, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, cast, genres);
        this.setDuration(duration);
    }
}
