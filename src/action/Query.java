package action;

import actor.Actor;
import actor.ActorsAwards;
import database.ActorsDB;
import database.VideosDB;
import entertainment.Video;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Query {
    public static class Actors {
        public static List<String> average(int noActors, String sortType, VideosDB videosDB, ActorsDB actorsDB) {
            Comparator<Actor> averageActorComparator = Comparator
                                                    // First sorting criteria -> averageRating of actor
                                                    .comparingDouble((Actor actor) -> actor.getAverageRating(videosDB))
                                                    // Second sorting criteria -> alphabetical order
                                                    .thenComparing(Actor::getName);

            if (sortType.equals("desc")) {
                averageActorComparator = averageActorComparator.reversed();
            }

             return actorsDB.getActorList().stream()
                     // Filter out those with 0 rating
                     .filter(actor -> actor.getAverageRating(videosDB) > 0)
                     // Sorting with comaprator
                    .sorted(averageActorComparator)
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
            Comparator<Actor> awardsActorComparator = Comparator
                                                    .comparingInt(Actor::getTotalNoAwards)
                                                    .thenComparing(Actor::getName);

            if (sortType.equals("desc")) {
                awardsActorComparator = awardsActorComparator.reversed();
            }

            return actorsDB.getActorList().stream()
                    .filter(actor -> hasAllAwards(actor, awardsList))
                    .sorted(awardsActorComparator)
                    .map(Actor::getName)
                    .collect(Collectors.toList());
        }

        public static List<String> description() { return null; }
    }

}
