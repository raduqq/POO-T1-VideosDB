package action;

import database.GenreDB;
import database.UsersDB;
import database.VideosDB;
import entertainment.Video;
import user.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Recommend {
    public static class Basic {
        /**
         * standard recommendation strategy
         * @param username username
         * @param usersDB usersDB
         * @param videosDB videosDB
         * @return recommendation result
         */
        public static String standard(final String username,
                                      final UsersDB usersDB,
                                      final VideosDB videosDB) {
            User user = usersDB.findUserByUsername(username);

            String result = videosDB.getVideoList().stream()
                            .filter(video -> {
                                assert user != null;
                                return !user.getHistory().containsKey(video.getTitle());
                            })
                            .map(Video::getTitle)
                            .limit(1)
                            .collect(Collectors.joining());

            // Couldn't find a recommendation
            if (result.isEmpty()) {
                return "StandardRecommendation cannot be applied!";
            }

            return "StandardRecommendation result: " + result;
        }

        /**
         * best unseen recommendation strategy
         * @param username username
         * @param usersDB usersDB
         * @param videosDB videosDB
         * @return recommendation result
         */
        public static String bestUnseen(final String username,
                                        final UsersDB usersDB,
                                        final VideosDB videosDB) {
            User user = usersDB.findUserByUsername(username);

            Comparator<Video> bestUnseenVideoComp = Comparator
                                                    .comparingDouble(Video::getAverageRating)
                                                    // First sorts by ratings in descending order
                                                    .reversed()
                                                    // Then ascendingly by index in database
                                                    .thenComparingInt(video -> videosDB
                                                                            .getVideoList()
                                                                            .indexOf(video));

            String result = videosDB.getVideoList().stream()
                    // Filters out videos that aren't in user's view history
                    .filter(video -> {
                        assert user != null;
                        return !user.getHistory().containsKey(video.getTitle());
                    })
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
        /**
         *
         * popular recommendation strategy
         * ranks genre by their popularity
         * @param username username
         * @param usersDB to search in
         * @param genreDB to search in
         * @param videosDB to search in
         * @return first non-viewed video from most popular genre
         */
        public static String popular(final String username,
                                     final UsersDB usersDB,
                                     final GenreDB genreDB,
                                     final VideosDB videosDB) {
            User user = usersDB.findUserByUsername(username);

            // Attempting to get Premium recommendation for Basic user
            assert user != null;
            if (user.getSubscriptionType().equals("BASIC")) {
                return "PopularRecommendation cannot be applied!";
            }

            // Build the comparator
            Comparator<Object> popularGenreComp = Comparator
                                                .comparing(genreName -> genreDB.
                                                        getPopularity(String.valueOf(genreName),
                                                                videosDB))
                                                .reversed();

            // Build the stream
            List<String> popularGenres = genreDB.getGenreMap().keySet().stream()
                                        .sorted(popularGenreComp)
                                        .collect(Collectors.toList());

            for (String genreName : popularGenres) {
                for (String videoName : genreDB.getGenreMap().get(genreName)) {
                    if (!user.getHistory().containsKey(videoName)) {
                        return "PopularRecommendation result: " + videoName;
                    }
                }
            }

            // Couldn't find a recommendation
            return "PopularRecommendation cannot be applied!";

        }

        /**
         * favorite recommendation strategy
         * @param username username
         * @param usersDB usersDB
         * @param videosDB videosDB
         * @return recommendation result
         */
        public static String favorite(final String username,
                                      final UsersDB usersDB,
                                      final VideosDB videosDB) {
            User user = usersDB.findUserByUsername(username);

            // Attempting to get Premium recommendation for Basic user
            assert user != null;
            if (user.getSubscriptionType().equals("BASIC")) {
                return "FavoriteRecommendation cannot be applied!";
            }

            Comparator<Video> favoriteVideoComp = Comparator
                                                .comparingInt(Video::getFavCount)
                                                .reversed();

            String result = videosDB.getVideoList().stream()
                                            // Filters out videos that were not favved
                                            .filter(video -> video.getFavCount() > 0)
                                            // Filters out videos that were already watched
                                            .filter(video -> !user
                                                            .getHistory()
                                                            .containsKey(video.getTitle()))
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

        /**
         * search recommendation strategy
         * @param username username
         * @param genreName genreName
         * @param usersDB usersDB
         * @param videosDB videosDB
         * @return recommendation result
         */
        public static String search(final String username, final String genreName,
                                    final UsersDB usersDB, final VideosDB videosDB) {
            User user = usersDB.findUserByUsername(username);

            // Attempting to get Premium recommendation for Basic user
            assert user != null;
            if (user.getSubscriptionType().equals("BASIC")) {
                return "SearchRecommendation cannot be applied!";
            }

            Comparator<Video> searchVideoComp = Comparator
                                            .comparingDouble(Video::getAverageRating)
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
