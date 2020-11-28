package fileio;

import database.ActorsDB;
import database.GenreDB;
import database.UsersDB;
import database.VideosDB;

import java.util.List;

/**
 * The class contains information about input
 * <p>
 * DO NOT MODIFY
 */
public final class Input {
    /**
     * List of actors
     */
    private final List<ActorInputData> actorsData;
    /**
     * List of users
     */
    private final List<UserInputData> usersData;
    /**
     * List of commands
     */
    private final List<ActionInputData> commandsData;
    /**
     * List of movies
     */
    private final List<MovieInputData> moviesData;
    /**
     * List of serials aka tv shows
     */
    private final List<SerialInputData> serialsData;

    // My custom databases

    /**
     * User database
     */
    private final UsersDB myUserDB;
    /**
     * Actors database
     */
    private final ActorsDB myActorDB;
    /**
     * Genre database
     */
    private final GenreDB myGenreDB;
    /**
     * Videos database
     */
    private final VideosDB myVideoDB;

    public Input() {
        this.actorsData = null;
        this.usersData = null;
        this.commandsData = null;
        this.moviesData = null;
        this.serialsData = null;

        // My cusotm databases
        this.myUserDB = null;
        this.myActorDB = null;
        this.myGenreDB = null;
        this.myVideoDB = null;
    }

    public Input(final List<ActorInputData> actors, final List<UserInputData> users,
                 final List<ActionInputData> commands,
                 final List<MovieInputData> movies,
                 final List<SerialInputData> serials,
                 final UsersDB myUserDB, final ActorsDB myActorDB,
                 final GenreDB myGenreDB, final VideosDB myVideoDB) {
        this.actorsData = actors;
        this.usersData = users;
        this.commandsData = commands;
        this.moviesData = movies;
        this.serialsData = serials;

        // My custom databases
        this.myUserDB = myUserDB;
        this.myActorDB = myActorDB;
        this.myGenreDB = myGenreDB;
        this.myVideoDB = myVideoDB;
    }

    public List<ActorInputData> getActors() {
        return actorsData;
    }

    public List<UserInputData> getUsers() {
        return usersData;
    }

    public List<ActionInputData> getCommands() {
        return commandsData;
    }

    public List<MovieInputData> getMovies() {
        return moviesData;
    }

    public List<SerialInputData> getSerials() {
        return serialsData;
    }

    public UsersDB getMyUserDB() {
        return myUserDB;
    }

    public ActorsDB getMyActorDB() {
        return myActorDB;
    }

    public GenreDB getMyGenreDB() {
        return myGenreDB;
    }

    public VideosDB getMyVideoDB() {
        return myVideoDB;
    }

    @Override
    public String toString() {
        return "Input{" +
                "myUserDB=" + myUserDB +
                ", myActorDB=" + myActorDB +
                ", myGenreDB=" + myGenreDB +
                ", myVideoDB=" + myVideoDB +
                '}';
    }
}
