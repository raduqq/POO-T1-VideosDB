package database;

import entertainment.Video;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideosDB {
    private List<Video> videoList;

    public VideosDB() { videoList = new ArrayList<>(); }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void addToDB(Video video) {
        videoList.add(video);
    }

    public Video findVideoByName(String videoName) {
        for (Video video : videoList) {
            if (video.getTitle().equals(videoName)) {
                return video;
            }
        }

        return null;
    }

    public void computeInitialFavCount(UsersDB usersDB) {
        for (User user : usersDB.getUserList()) {
            // Incrementing fav count for each favorite video of user
            for (String videoName : user.getFavoriteVideos()) {
                Video currentVideo = this.findVideoByName(videoName);
                currentVideo.incFavCount();
            }
        }
    }

    public void computeInitialViewCount(UsersDB usersDB) {
        for (User user : usersDB.getUserList()) {
            // Incrementing view count for each video in user's history
            for (Map.Entry<String, Integer> historyData : user.getHistory().entrySet()) {
                Video currentVideo = this.findVideoByName(historyData.getKey());
                currentVideo.incViewCount(historyData.getValue());
            }
        }
    }

    @Override
    public String toString() {
        return "VideosDB{" +
                "videoList=" + videoList +
                '}';
    }
}
