package database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public final class GenreDB {
    private Map<String, List<String>> genreMap;

    public GenreDB() {
        genreMap = new HashMap<>();
    }

    public Map<String, List<String>> getGenreMap() {
        return genreMap;
    }

    /**
     *
     * @param genreName to be added into the database
     * @param videoName of videos correspondent to the given genre
     */
    public void addToMap(final String genreName, final String videoName) {
        // if map already contains genre name, append to list
        if (genreMap.containsKey(genreName)) {
            genreMap.get(genreName).add(videoName);
        } else {
            // else new entry in dict
            genreMap.put(genreName, new ArrayList<>(Collections.singletonList(videoName)));
        }
    }

    /**
     *
     * @param genreName whose popularity we get here
     * @param videosDB to search in
     * @return popularity of given genre
     */
    public int getPopularity(final String genreName, final VideosDB videosDB) {
        int popularity = 0;

        for (String videoName : genreMap.get(genreName)) {
            popularity += Objects.requireNonNull(videosDB
                                                .findVideoByName(videoName))
                                                .getViewCount();
        }

        return popularity;
    }
}
