package action;

import actor.Actor;
import database.ActorsDB;
import database.UsersDB;
import database.VideosDB;
import entertainment.Video;
import user.User;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Query {
    public static class Actors {
        /**
         * checks if actor has all awards from awardsList
         * @param actor actor
         * @param awardsList awardsList
         * @return true or false
         */
        public static boolean hasAllAwards(final Actor actor,
                                           final List<String> awardsList) {
            List<String> requiredAwardsList = awardsList.stream()
                    // Converting ActorsAwards type to lower-case String
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            List<String> actorAwardsList = actor.getAwards().keySet().stream()
                    // Converting ActorsAwards type to lower-case String
                    .map(award -> String.valueOf(award).toLowerCase())
                    .collect(Collectors.toList());

            for (String requiredAward : requiredAwardsList) {
                if (!actorAwardsList.contains(requiredAward)) {
                    return false;
                }
            }

            return true;
        }

        /**
         * checks if actor's description contains all keywords
         * @param description of actor
         * @param keywords to be matched with
         * @return true or false
         */
        public static boolean hasAllKeywords(final String description,
                                             final List<String> keywords) {
            for (String keyword : keywords) {
                String regex = " " + keyword + "[ ,.!?']";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(description);

                if (!matcher.find()) {
                    return false;
                }
            }

            return true;
        }

        /**
         * ranks actors based on their average rating
         * @param noActors to be returned
         * @param sortType sortType
         * @param videosDB videosDB
         * @param actorsDB actorsDB
         * @return query result
         */
        public static List<String> average(final int noActors, final String sortType,
                                           final VideosDB videosDB, final ActorsDB actorsDB) {
            Comparator<Actor> avgActorComp = Comparator
                                            // First sorting criteria -> averageRating of actor
                                            .comparingDouble((Actor actor) -> actor
                                                                    .getAverageRating(videosDB))
                                            // Second sorting criteria -> alphabetical order
                                            .thenComparing(Actor::getName);

            if (sortType.equals("desc")) {
                avgActorComp = avgActorComp.reversed();
            }

             return actorsDB.getActorList().stream()
                     // Filter out those with 0 rating
                     .filter(actor -> actor.getAverageRating(videosDB) > 0)
                     // Sorting with comaprator
                    .sorted(avgActorComp)
                     // Reduce Actor objects to their "name" field
                    .map(Actor::getName)
                     // Return first "noActors" elem in list
                    .limit(noActors)
                     // Convert Stream to List
                    .collect(Collectors.toList());
        }

        /**
         * queries actors by awards
         * @param sortType sortType
         * @param awardsList to be matched with actors' awards
         * @param actorsDB actorsDB
         * @return list of actors who have all awards in awardsList
         */
        public static List<String> awards(final String sortType,
                                          final List<String> awardsList,
                                          final ActorsDB actorsDB) {
            // Sorting by:
            Comparator<Actor> awardsActorComp = Comparator
                                                // Total no. awards
                                                .comparingInt(Actor::getTotalNoAwards)
                                                // Name
                                                .thenComparing(Actor::getName)
                                                // Index in database
                                                .thenComparing(actorsDB.getActorList()::indexOf);

            if (sortType.equals("desc")) {
                awardsActorComp = awardsActorComp.reversed();
            }

            return actorsDB.getActorList().stream()
                    .filter(actor -> hasAllAwards(actor, awardsList))
                    .sorted(awardsActorComp)
                    .map(Actor::getName)
                    .collect(Collectors.toList());
        }

        /**
         * queries actors by description
         * @param sortType sortType
         * @param keywords to ba matched with actors description
         * @param actorsDB actorsDB
         * @return list of actors whose description matches the keywords
         */
        public static List<String> description(final String sortType,
                                               final List<String> keywords,
                                               final ActorsDB actorsDB) {
            Comparator<Actor> descrActorComp = Comparator
                                                .comparing(Actor::getName)
                                                .thenComparing(actorsDB.getActorList()::indexOf);

            if (sortType.equals("desc")) {
                descrActorComp = descrActorComp.reversed();
            }

            return actorsDB.getActorList().stream()
                    .filter(actor -> hasAllKeywords(actor.getCareerDescription(), keywords))
                    .sorted(descrActorComp)
                    .map(Actor::getName)
                    .collect(Collectors.toList());
        }
    }

    public static class Videos {
        /**
         * restricts video list according to
         * the type of objects requested (movies or shows)
         * @param objectType movie or show
         * @param videosDB videosDB
         * @return restricted list with only movies or shows
         */
        public static List<Video> restrictVideoList(final String objectType,
                                                    final VideosDB videosDB) {
            if (objectType.equals("movies")) {
                return videosDB.getVideoList().subList(0, videosDB.getNoMovies());
            }

            return videosDB
                    .getVideoList()
                    .subList(videosDB.getNoMovies(),
                            videosDB.getVideoList().size());
        }

        /**
         * query by ratings
         * @param noVideos to be returned
         * @param sortType asc or desc
         * @param objectType movie or show
         * @param years years
         * @param genres genres
         * @param videosDB videosDB
         * @return query result
         */
        public static List<String> ratings(final int noVideos, final String sortType,
                                           final String objectType, final List<String> years,
                                           final List<String> genres, final VideosDB videosDB) {
            Comparator<Video> ratingVideoComp = Comparator
                                                .comparingDouble(Video::getAverageRating)
                                                .thenComparing(Video::getTitle);

            if (sortType.equals("desc")) {
                ratingVideoComp = ratingVideoComp.reversed();
            }

            Stream<Video> result = restrictVideoList(objectType, videosDB).stream()
                                    .filter(video -> video.getAverageRating() > 0);

            // Applying genre filter, if applicable
            if (genres.get(0) != null) {
                for (String genre : genres) {
                    result = result.filter((video -> video.getGenres().contains(genre)));
                }
            }

            // Applying year filter, if applicable
            if (years.get(0) != null) {
                for (String year : years) {
                    result = result.filter(video -> Integer.parseInt(year) == video.getYear());
                }
            }

            return result
                    .sorted(ratingVideoComp)
                    .map(Video::getTitle)
                    .limit(noVideos)
                    .collect(Collectors.toList());
        }

        /**
         * query by favorites
         * @param noVideos to be returned
         * @param sortType asc or desc
         * @param objectType movies or shwos
         * @param years years
         * @param genres genres
         * @param videosDB videosDB
         * @return query result
         */
        public static List<String> favorite(final int noVideos, final String sortType,
                                            final String objectType, final List<String> years,
                                            final List<String> genres, final VideosDB videosDB) {
            Comparator<Video> favVideoComp = Comparator
                                            .comparingInt(Video::getFavCount)
                                            .thenComparing(Video::getTitle);

            if (sortType.equals("desc")) {
                favVideoComp = favVideoComp.reversed();
            }

            Stream<Video> result = restrictVideoList(objectType, videosDB).stream()
                                    .filter(video -> video.getViewCount() > 0);

            // Applying genre filter, if applicable
            if (genres.get(0) != null) {
                for (String genre : genres) {
                    result = result.filter((video -> video.getGenres().contains(genre)));
                }
            }

            // Applying year filter, if applicable
            if (years.get(0) != null) {
                for (String year : years) {
                    result = result.filter(video -> Integer.parseInt(year) == video.getYear());
                }
            }

            return result
                    .sorted(favVideoComp)
                    .map(Video::getTitle)
                    .limit(noVideos)
                    .collect(Collectors.toList());
        }

        /**
         * query by longest videos
         * @param noVideos to be returned
         * @param sortType asc or desc
         * @param objectType movies or shows
         * @param years years
         * @param genres genres
         * @param videosDB videosDB
         * @return query result
         */
        public static List<String> longest(final int noVideos, final String sortType,
                                           final String objectType, final List<String> years,
                                           final List<String> genres, final VideosDB videosDB) {
            Comparator<Video> longestVideoComp = Comparator
                                                .comparingInt(Video::getDuration)
                                                .thenComparing(Video::getTitle);

            if (sortType.equals("desc")) {
                longestVideoComp = longestVideoComp.reversed();
            }

            Stream<Video> result = restrictVideoList(objectType, videosDB).stream();

            // Applying genre filter, if applicable
            if (genres.get(0) != null) {
                for (String genre : genres) {
                    result = result.filter((video -> video.getGenres().contains(genre)));
                }
            }

            // Applying year filter, if applicable
            if (years.get(0) != null) {
                for (String year : years) {
                    result = result.filter(video -> Integer.parseInt(year) == video.getYear());
                }
            }

            return result
                    .sorted(longestVideoComp)
                    .map(Video::getTitle)
                    .limit(noVideos)
                    .collect(Collectors.toList());
        }

        /**
         * query by most viewed videos
         * @param noVideos to be returned
         * @param sortType asc or desc
         * @param objectType movies or shows
         * @param years years
         * @param genres genres
         * @param videosDB videosDB
         * @return query result
         */
        public static List<String> mostViewed(final int noVideos,
                                              final String sortType,
                                              final String objectType,
                                              final List<String> years,
                                              final List<String> genres,
                                              final VideosDB videosDB) {
            Comparator<Video> mostViewedVideoComp = Comparator
                                                    .comparingInt(Video::getViewCount)
                                                    .thenComparing(Video::getTitle);

            if (sortType.equals("desc")) {
                mostViewedVideoComp = mostViewedVideoComp.reversed();
            }

            Stream<Video> result = restrictVideoList(objectType, videosDB).stream()
                    .filter(video -> video.getViewCount() > 0);

            // Applying year filter, if applicable
            if (years.get(0) != null) {
                for (String year : years) {
                    result = result.filter(video -> Integer.parseInt(year) == video.getYear());
                }
            }

            // Applying genre filter, if applicable
            if (genres.get(0) != null) {
                for (String genre : genres) {
                    result = result.filter((video -> video.getGenres().contains(genre)));
                }
            }

            return result
                    .sorted(mostViewedVideoComp)
                    .map(Video::getTitle)
                    .limit(noVideos)
                    .collect(Collectors.toList());
        }

    }

    public static class Users {
        /**
         * query by most active users
         * @param noUsers to be shown
         * @param sortType asc or desc
         * @param usersDB usersDB
         * @return query result
         */
        public static List<String> noRatings(final int noUsers,
                                             final String sortType,
                                             final UsersDB usersDB) {
            Comparator<User> noRatingsUserComp = Comparator
                                                .comparingInt(User::getNoRatingsGiven)
                                                .thenComparing(User::getUsername);

            if (sortType.equals("desc")) {
                noRatingsUserComp = noRatingsUserComp.reversed();
            }

            return usersDB.getUserList().stream()
                    .filter(user -> user.getNoRatingsGiven() > 0)
                    .sorted(noRatingsUserComp)
                    .map(User::getUsername)
                    .limit(noUsers)
                    .collect(Collectors.toList());
        }
    }

}
