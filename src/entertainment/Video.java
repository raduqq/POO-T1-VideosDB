package entertainment;

import user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Video {
    /**
     * Video's title
     */
    private final String title;
    /**
     * The year the video was released
     */
    private final int year;
    /**
     * Video casting
     */
    private final ArrayList<String> cast;

    public int getYear() {
        return year;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     * Video genres
     */
    private final ArrayList<String> genres;
    /**
     * No. of times video was favved by users
     */
    private int favCount;
    /**
     * No. of times video was watched by users
     */
    private int viewCount;
    /**
     * Duration in minutes of a video
     */
    private int duration;
    /**
     * Dictionary [user : givenRating]
     */
    private final Map<User, Double> ratings;

    public Map<User, Double> getRatings() {
        return ratings;
    }

    public Video(final String title, final int year,
                 final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
        this.favCount = 0;
        this.viewCount = 0;
        this.ratings = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public int getFavCount() {
        return favCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    /**
     * increments the favCount
     * when favved by user
     */
    public void incFavCount() {
        this.favCount++;
    }

    /**
     * increases the viewCount by howMuch
     * when viewed by user
     * @param howMuch to increase the view count with
     */
    public void incViewCount(final int howMuch) {
        this.viewCount += howMuch;
    }

    /**
     * will be implemented by Movie and Show subclasses
     * @return average rating of current video
     */
    public abstract Double getAverageRating();
}
