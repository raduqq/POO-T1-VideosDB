package action;

import database.UsersDB;
import database.VideosDB;
import entertainment.Season;
import entertainment.Show;
import entertainment.Video;
import user.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Command {
    /**
     *
     * @param username of user who favvs
     * @param videoTitle of video to be favved
     * @param videosDB videosDB
     * @param usersDB usersDB
     * @return message of command result
     */
    public static String favorite(final String username, final String videoTitle,
                                  final VideosDB videosDB, final UsersDB usersDB) {
        User currentUser = usersDB.findUserByUsername(username);
        assert currentUser != null;
        Map<String, Integer> currentHistory = currentUser.getHistory();
        List<String> currentFavVideos = currentUser.getFavoriteVideos();

        if (currentHistory.containsKey(videoTitle)) {
            if (currentFavVideos.contains(videoTitle)) {
                return "error -> " + videoTitle + " is already in favourite list";
            }

            // Adding video to user's favorite videos list
            currentUser.getFavoriteVideos().add(videoTitle);

            // Increasing video's favCount
            Objects.requireNonNull(videosDB.findVideoByName(videoTitle)).incFavCount();

            return "success -> " + videoTitle + " was added as favourite";
        }

        return "error -> " + videoTitle + " is not seen";
    }

    /**
     *
     * @param username of user who views
     * @param videoTitle of video to be viewed
     * @param videosDB videosDB
     * @param usersDB usersDB
     * @return message of command
     */
    public static String view(final String username, final String videoTitle,
                              final VideosDB videosDB, final UsersDB usersDB) {
        User currentUser = usersDB.findUserByUsername(username);
        assert currentUser != null;
        Map<String, Integer> currentHistory = currentUser.getHistory();

        if (currentHistory.containsKey(videoTitle)) {
            // Already viewed -> increse user's view count of current video
            currentHistory.put(videoTitle, currentHistory.get(videoTitle) + 1);
        } else {
            // Not viewed -> create entry in user's history
            currentHistory.put(videoTitle, 1);
        }

        // Through this command, user views given video only once
        Objects.requireNonNull(videosDB.findVideoByName(videoTitle)).incViewCount(1);

        return "success -> "
                + videoTitle
                + " was viewed with total views of "
                + currentHistory.get(videoTitle);
    }

    /**
     *
     * @param username of user who rates the movie
     * @param movieTitle of movie to be rated
     * @param grade to be given
     * @param videosDB videosDB
     * @param usersDB usersDB
     * @return message of command result
     */
    public static String rateMovie(final String username, final String movieTitle,
                                   final Double grade, final VideosDB videosDB,
                                   final UsersDB usersDB) {
        User currentUser = usersDB.findUserByUsername(username);
        assert currentUser != null;
        Map<String, Integer> currentHistory = currentUser.getHistory();

        Video currentMovie = videosDB.findVideoByName(movieTitle);

        if (currentHistory.containsKey(movieTitle)) {
            assert currentMovie != null;
            if (currentMovie.getRatings().containsKey(currentUser)) {
                return "error -> " + movieTitle + " has been already rated";
            }

            currentMovie.getRatings().put(currentUser, grade);
            currentUser.incNoRatingsGiven();
            return "success -> " + movieTitle + " was rated with " + grade + " by " + username;
        }

        return "error -> " + movieTitle + " is not seen";
    }

    /**
     *
     * @param username of user who rates season
     * @param showTitle of show to be rated
     * @param seasonNo of current season
     * @param grade to be given
     * @param videosDB to search in
     * @param usersDB to search in
     * @return command result message
     */
    public static String rateSeason(final String username, final String showTitle,
                                    final int seasonNo, final Double grade,
                                    final VideosDB videosDB, final UsersDB usersDB) {
        User currentUser = usersDB.findUserByUsername(username);
        assert currentUser != null;
        Map<String, Integer> currentHistory = currentUser.getHistory();

        Show currentShow = (Show) videosDB.findVideoByName(showTitle); // couldn't avoid downcast
        assert currentShow != null;
        Season currentSeason = currentShow.getSeasons().get(seasonNo - 1);

        if (currentHistory.containsKey(showTitle)) {
            if (currentSeason.getRatings().containsKey(currentUser)) {
                // actually, it's more like showTitle->seasonNo is already rated
                return "error -> " + showTitle + " has been already rated";
            }

            currentSeason.getRatings().put(currentUser, grade);
            currentUser.incNoRatingsGiven();
            return "success -> " + showTitle + " was rated with " + grade + " by " + username;
        }

        return "error -> " + showTitle + " is not seen";
    }
}
