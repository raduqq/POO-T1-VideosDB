package actor;

import database.VideosDB;
import entertainment.Video;

import java.util.ArrayList;
import java.util.Map;

public final class Actor {
    /**
     * actor name
     */
    private String name;
    /**
     * description of the actor's career
     */
    private String careerDescription;
    /**
     * videos starring actor
     */
    private ArrayList<String> filmography;
    /**
     * awards won by the actor
     */
    private final Map<ActorsAwards, Integer> awards;

    public Actor(final String name, final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    /**
     *
     * @param videosDB videosDB
     * @return average rating of actor based on movies he has played in
     */
    public Double getAverageRating(final VideosDB videosDB) {
        Double averageRating = (double) 0;
        int ratedMoviesCnt = 0;

        for (String videoName : filmography) {
            Video currentVideo = videosDB.findVideoByName(videoName);

            // Movie not found in database => no ratings given to it => skip it
            if (currentVideo == null) {
                continue;
            }

            /*
             Finding unrated movies => skipping them
             Finding rated movies => counting them
            */
            if (currentVideo.getAverageRating() > 0) {
                ratedMoviesCnt++;
                averageRating +=  currentVideo.getAverageRating();
            }
        }

        // Actor played in movies that weren't rated
        if (ratedMoviesCnt == 0) {
            return (double) 0;
        }

        // Actor played in movies that were rated
        return averageRating / ratedMoviesCnt;
    }

    /**
     * @return total number of awards won by actor
     */
    public Integer getTotalNoAwards() {
        Integer cnt = 0;

        for (Integer noAwards : awards.values()) {
            cnt += noAwards;
        }

        return cnt;
    }
}
