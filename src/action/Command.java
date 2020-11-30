package action;

import database.UsersDB;
import database.VideosDB;
import entertainment.Season;
import entertainment.Show;
import entertainment.Video;
import user.User;

import java.util.List;
import java.util.Map;

public class Command {
    public static String favorite(String username, String videoTitle, VideosDB videosDB, UsersDB usersDB) {
        User currentUser = usersDB.findUserByUsername(username);
        Map<String, Integer> currentHistory = currentUser.getHistory();
        List<String> currentFavVideos = currentUser.getFavoriteVideos();

        if (currentHistory.containsKey(videoTitle)) {
            if (currentFavVideos.contains(videoTitle)) {
                return "error -> " + videoTitle + " is already in favourite list";
            }

            // Adding video to user's favorite videos list
            currentUser.getFavoriteVideos().add(videoTitle);

            // Increasing video's favCount
            videosDB.findVideoByName(videoTitle).incFavCount();

            return "success -> " + videoTitle + " was added as favourite";
        }

        return "error -> " + videoTitle + " is not seen";
    }

    public static String view(String username, String videoTitle, VideosDB videosDB, UsersDB usersDB) {
        User currentUser = usersDB.findUserByUsername(username);
        Map<String, Integer> currentHistory = currentUser.getHistory();

        if (currentHistory.containsKey(videoTitle)) {
            // Already viewed -> increse user's view count of current video
            currentHistory.put(videoTitle, currentHistory.get(videoTitle) + 1);
        } else {
            // Not viewed -> create entry in user's history
            currentHistory.put(videoTitle, 1);
        }

        // Through this command, user views given video only once
        videosDB.findVideoByName(videoTitle).incViewCount(1);

        return "success -> " + videoTitle + " was viewed with total views of " + currentHistory.get(videoTitle);
    }

    public static String rateMovie(String username, String movieTitle, Double grade, VideosDB videosDB, UsersDB usersDB) {
        User currentUser = usersDB.findUserByUsername(username);
        Map<String, Integer> currentHistory = currentUser.getHistory();

        Video currentMovie = videosDB.findVideoByName(movieTitle);

        if (currentHistory.containsKey(movieTitle)) {
            if(currentMovie.getRatings().containsKey(currentUser)) {
                return "error -> " + movieTitle + " has been already rated"; // replace with exact message
            }

            currentMovie.getRatings().put(currentUser, grade);
            currentUser.incNoRatingsGiven();
            return "success -> " + movieTitle + " was rated with " + grade + " by " + username;
        }

        return "error -> " + movieTitle + " is not seen";
    }

    public static String rateSeason(String username, String showTitle, int seasonNo, Double grade, VideosDB videosDB, UsersDB usersDB) {
        User currentUser = usersDB.findUserByUsername(username);
        Map<String, Integer> currentHistory = currentUser.getHistory();

        Show currentShow = (Show) videosDB.findVideoByName(showTitle); // couldn't avoid downcast
        Season currentSeason = currentShow.getSeasons().get(seasonNo - 1); // -1, ca ghici ce, sezonul 1 e la pozitia 0 in arraylist

        if (currentHistory.containsKey(showTitle)) {
            if (currentSeason.getRatings().containsKey(currentUser)) {
                // actually, it's more like showTitle->seasonNo is already rated
                return "error -> " + showTitle + " is already rated"; // replace with exact message
            }

            currentSeason.getRatings().put(currentUser, grade);
            currentUser.incNoRatingsGiven();
            return "success -> " + showTitle + " was rated with " + grade + " by " + username;
        }

        return "error -> " + showTitle + " is not seen";
    }
}
