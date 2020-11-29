package entertainment;

import java.util.ArrayList;

public class Show extends Video {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Season> seasons;

    public Show(final String title, final ArrayList<String> cast,
                           final ArrayList<String> genres,
                           final int numberOfSeasons, final ArrayList<Season> seasons,
                           final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        this.computeDuration();
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void computeDuration() {
        this.setDuration(0);

        for (Season season : seasons) {
            this.setDuration(this.getDuration() + season.getDuration());
        }
    }

    @Override
    public Double getAverageRating() {
        Double averageRating = (double) 0;

        for (Season season : seasons) {
            averageRating += season.getAverageRating();
        }

        return averageRating / numberOfSeasons;
    }

    @Override
    public String toString() {
        return "Show{" +
                "numberOfSeasons=" + numberOfSeasons +
                ", seasons=" + seasons +
                '}' + '\n';
    }
}
