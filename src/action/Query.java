package action;

import actor.Actor;
import database.ActorsDB;
import database.VideosDB;
import entertainment.Video;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Query {
    public static class Actors {
        public static List<String> average(int noActors, VideosDB videosDB, ActorsDB actorsDB, String sort_type) {
            Comparator<Actor> averageActorComparator = Comparator
                                                    // First sorting criteria -> averageRating of actor
                                                    .comparingDouble((Actor actor) -> actor.getAverageRating(videosDB))
                                                    // Second sorting criteria -> alphabetical order
                                                    .thenComparing(Actor::getName);

            if (sort_type.equals("desc")) {
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

        public static List<String> awards() {
            return null;
        }

        public static List<String> description() { return null; }
    }

}
