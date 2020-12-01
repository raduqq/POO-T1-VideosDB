package database;

import entertainment.Video;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class VideosDB {
    private final List<Video> videoList;

    private int noMovies;

    private int noShows;

    public VideosDB() {
        videoList = new ArrayList<>();
        noMovies = 0;
        noShows = 0;
    }

    public int getNoMovies() {
        return noMovies;
    }

    public int getNoShows() {
        return noShows;
    }

    /**
     * increments no of movies present in the database
     */
    public void incNoMovies() {
        noMovies++;
    }

    /**
     * increments no of shows present in the database
     */
    public void incNoShows() {
        noShows++;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    /**
     *
     * @param video to be added into the database
     */
    public void addToDB(final Video video) {
        videoList.add(video);
    }

    /**
     *
     * @param videoName to match with video in the database
     * @return video with given videoName
     */
    public Video findVideoByName(final String videoName) {
        for (Video video : videoList) {
            if (video.getTitle().equals(videoName)) {
                return video;
            }
        }

        return null;
    }

    /**
     * computes fav count of all videos based on inital users' history
     */
    public void computeInitialFavCount(final UsersDB usersDB) {
        for (User user : usersDB.getUserList()) {
            // Incrementing fav count for each favorite video of user
            for (String videoName : user.getFavoriteVideos()) {
                Video currentVideo = this.findVideoByName(videoName);
                assert currentVideo != null;
                currentVideo.incFavCount();
            }
        }
    }

    /**
     * computes view count of all videos based on initial users' history
     */
    public void computeInitialViewCount(final UsersDB usersDB) {
        for (User user : usersDB.getUserList()) {
            // Incrementing view count for each video in user's history
            for (Map.Entry<String, Integer> historyData : user.getHistory().entrySet()) {
                Video currentVideo = this.findVideoByName(historyData.getKey());
                assert currentVideo != null;
                currentVideo.incViewCount(historyData.getValue());
            }
        }
    }
}
