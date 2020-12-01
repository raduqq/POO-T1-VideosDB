package database;

import actor.Actor;

import java.util.ArrayList;
import java.util.List;

public final class ActorsDB {
    private final List<Actor> actorList;

    public ActorsDB() {
        actorList = new ArrayList<>();
    }

    public List<Actor> getActorList() {
        return actorList;
    }

    /**
     *
     * @param actor to be added into the database
     */
    public void addToDB(final Actor actor) {
        actorList.add(actor);
    }
}
