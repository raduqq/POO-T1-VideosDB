package entertainment;

import java.util.ArrayList;

public final class Movie extends Video {
    @Override
    public Double getAverageRating() {
        if (getRatings().size() == 0) {
            return (double) 0;
        }

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
