package action;

import actor.Actor;
import actor.ActorsAwards;
import database.ActorsDB;
import database.UsersDB;
import database.VideosDB;
import entertainment.Video;
import user.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Query {
    public static class Actors {
        public static List<String> average(int noActors, String sortType, VideosDB videosDB, ActorsDB actorsDB) {
            Comparator<Actor> avgActorComp = Comparator
                                            // First sorting criteria -> averageRating of actor
                                            .comparingDouble((Actor actor) -> actor.getAverageRating(videosDB))
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

        public static boolean hasAllAwards(Actor actor, List <String> awardsList) {
            List <String> requiredAwardsList = awardsList.stream()
                                            // Converting ActorsAwards type to lower-case String
                                            .map(String::toLowerCase)
                                            .collect(Collectors.toList());
            List <String> actorAwardsList = actor.getAwards().keySet().stream()
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

        public static List<String> awards(String sortType, List<String> awardsList, ActorsDB actorsDB) {
            // Sorting by:
            Comparator<Actor> awardsActorComp = Comparator
                                                // Total no. awards
                                                .comparingInt(Actor::getTotalNoAwards)
                                                // Name
                                                .thenComparing(Actor::getName)
                                                // Index in database
                                                .thenComparing(actor -> actorsDB.getActorList().indexOf(actor));

            if (sortType.equals("desc")) {
                awardsActorComp = awardsActorComp.reversed();
            }

            return actorsDB.getActorList().stream()
                    .filter(actor -> hasAllAwards(actor, awardsList))
                    .sorted(awardsActorComp)
                    .map(Actor::getName)
                    .collect(Collectors.toList());
        }

        public static boolean hasAllKeywords(String description, List<String> keywords) {
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

        public static List<String> description(String sortType, List<String> keywords, ActorsDB actorsDB) {
            Comparator<Actor> descrActorComp = Comparator
                                                .comparing(Actor::getName)
                                                .thenComparing(actor -> actorsDB.getActorList().indexOf(actor));

            if(sortType.equals("desc")) {
                descrActorComp = descrActorComp.reversed();
            }

            return actorsDB.getActorList().stream()
                    .filter(actor -> hasAllKeywords(actor.getCareerDescription(), keywords))
                    .sorted(descrActorComp)
                    .map(Actor::getName)
                    .collect(Collectors.toList());
        }
    }

    public static class Users {
        public static List<String> noRatings(int noUsers, String sortType, UsersDB usersDB) {
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
