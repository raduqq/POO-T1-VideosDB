package action;

import database.UsersDB;
import database.VideosDB;
import entertainment.Movie;
import entertainment.Video;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Command {
    public String favorite(String username, String videoTitle, VideosDB videosDB, UsersDB usersDB) {
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

    public String view(String username, String videoTitle, VideosDB videosDB, UsersDB usersDB) {
        User currentUser = usersDB.findUserByUsername(username);
        Map<String, Integer> currentHistory = currentUser.getHistory();

        if (currentHistory.containsKey(videoTitle)) {
            // Already viewed -> increse user's view count of current video
            currentHistory.put(videoTitle, currentHistory.get(videoTitle) + 1);
        } else {
            // Not viewed -> create entry in user's history
            currentHistory.put(videoTitle, 1);
        }

        videosDB.findVideoByName(videoTitle).incViewCount();

        return "success -> " + videoTitle + " was viewed with total views of " + currentHistory.get(videoTitle);
    }

    public String rateMovie(String username, String movieTitle, Integer grade, VideosDB videosDB, UsersDB usersDB) {
        User currentUser = usersDB.findUserByUsername(username);
        Map<String, Integer> currentHistory = currentUser.getHistory();

        Video currentMovie = videosDB.findVideoByName(movieTitle);

        if (currentHistory.containsKey(movieTitle)) {
            // check if already rated => error
            // if not, rate
        }

        return "error -> " + movieTitle + " is not seen";
    }

    public String rateSeason(String username, String videoTitle, VideosDB videosDB, UsersDB usersDB) {
        return null;
    }

}
