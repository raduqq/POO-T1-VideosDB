package action;

import database.GenreDB;
import database.UsersDB;
import database.VideosDB;
import entertainment.Video;
import user.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Recommend {
    public static class Basic {
        public static String standard(String username, UsersDB usersDB, VideosDB videosDB) {
            User user = usersDB.findUserByUsername(username);

            String result = videosDB.getVideoList().stream()
                            .filter(video -> !user.getHistory().containsKey(video.getTitle()))
                            .map(Video::getTitle)
                            .limit(1)
                            .collect(Collectors.joining());

            // Couldn't find a recommendation
            if (result.isEmpty()) {
                return "StandardRecommendation cannot be applied!";
            }

            return "StandardRecommendation result: " + result;
        }
        
        public static String bestUnseen(String username, UsersDB usersDB, VideosDB videosDB) {
            User user = usersDB.findUserByUsername(username);
            
            Comparator<Video> bestUnseenVideoComp = Comparator
                                                    .comparingDouble(Video::getAverageRating)
                                                    // First sorts by ratings in descending order
                                                    .reversed()
                                                    // Then ascendingly by index in database
                                                    .thenComparingInt(video -> videosDB.getVideoList().indexOf(video));

            String result = videosDB.getVideoList().stream()
                    // Filters out videos that aren't in user's view history
                    .filter(video -> !user.getHistory().containsKey(video.getTitle()))
                    .sorted(bestUnseenVideoComp)
                    .map(Video::getTitle)
                    // We need only one recommendation
                    .limit(1)
                    .collect(Collectors.joining());

            // Couldn't find a recommendation
            if (result.isEmpty()) {
                return "BestRatedUnseenRecommendation cannot be applied!";
            }

            return "BestRatedUnseenRecommendation result: " + result;
        }
    }

    public static class Premium {
        //TODO
        public static String popular(String username, UsersDB usersDB, GenreDB genreDB, VideosDB videosDB) {
            String result = null;
            User user = usersDB.findUserByUsername(username);

            // Attempting to get Premium recommendation for Basic user
            if (user.getSubscriptionType().equals("BASIC")) {
                return "PopularRecommendation cannot be applied!";
            }

            //TODO: create CustomGenre class, otherwise this won't work

            // Couldn't find a recommendation
            if (result == null || result.isEmpty()) {
                return "PopularRecommendation cannot be applied!";
            }

            return "PopularRecommendation result: " + result;
        }

        public static String favorite(String username, UsersDB usersDB, VideosDB videosDB) {
            User user = usersDB.findUserByUsername(username);

            // Attempting to get Premium recommendation for Basic user
            if (user.getSubscriptionType().equals("BASIC")) {
                return "PopularRecommendation cannot be applied!";
            }

            Comparator<Video> favoriteVideoComp = Comparator
                                                .comparingInt(Video::getFavCount)
                                                .reversed();

            String result = videosDB.getVideoList().stream()
                                            // Filters out videos that were not favved
                                            .filter(video -> video.getFavCount() > 0)
                                            // Filters out videos that were already watched
                                            .filter(video -> !user.getHistory().containsKey(video.getTitle()))
                                            .sorted(favoriteVideoComp)
                                            .map(Video::getTitle)
                                            .limit(1)
                                            .collect(Collectors.joining());

            // Couldn't find a recommendation
            if (result.isEmpty()) {
                return "FavoriteRecommendation cannot be applied!";
            }

            return "FavoriteRecommendation result: " + result;
        }

        public static String search(String username, String genreName, UsersDB usersDB, VideosDB videosDB) {
            User user = usersDB.findUserByUsername(username);

            // Attempting to get Premium recommendation for Basic user
            if (user.getSubscriptionType().equals("BASIC")) {
                return "SearchRecommendation cannot be applied!";
            }

            Comparator<Video> searchVideoComp = Comparator
                    .comparingInt(Video::getFavCount)
                    .thenComparing(Video::getTitle);

            List<String> result = videosDB.getVideoList().stream()
                                .filter(video -> video.getGenres().contains(genreName))
                                .filter(video -> !user.getHistory().containsKey(video.getTitle()))
                                .sorted(searchVideoComp)
                                .map(Video::getTitle)
                                .collect(Collectors.toList());

            // Couldn't find a recommendation
            if (result.isEmpty()) {
                return "SearchRecommendation cannot be applied!";
            }

            return "SearchRecommendation result: " + result;
        }
    }
}
