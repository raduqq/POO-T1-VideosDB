package action;

import database.UsersDB;
import database.VideosDB;
import entertainment.Video;
import user.User;

import java.util.Comparator;
import java.util.stream.Collectors;

public class Recommend {
    public static class Basic {
        // Standard
        
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
                    .collect(Collectors.joining(" / "));

            // Couldn't find a recommendation
            if (result.isEmpty()) {
                return "BestRatedUnseenRecommendation cannot be applied!";
            }

            return "BestRatedUnseenRecommendation result: " + result;
        }
    }

    public static class Premium {
        //TODO: Popular
        public static String popular(String username, UsersDB usersDB) {
            String result = null;
            User user = usersDB.findUserByUsername(username);

            // Attempting to get Premium recommendation for Basic user
            if (user.getSubscriptionType().equals("BASIC")) {
                return "PopularRecommendation cannot be applied!";
            }

            //TODO: magic

            // Couldn't find a recommendation
            if (result.isEmpty()) {
                return "PopularRecommendation cannot be applied!";
            }

            return "PopularRecommendation result: " + result;
        }

        //TODO: Favorite
        public static String favorite(String username, UsersDB usersDB) {
            String result = null;
            User user = usersDB.findUserByUsername(username);

            // Attempting to get Premium recommendation for Basic user
            if (user.getSubscriptionType().equals("BASIC")) {
                return "PopularRecommendation cannot be applied!";
            }

            //TODO: magic

            // Couldn't find a recommendation
            if (result.isEmpty()) {
                return "FavoriteRecommendation cannot be applied!";
            }

            return "FavoriteRecommendation result: " + result;
        }

        //Search
    }
}
