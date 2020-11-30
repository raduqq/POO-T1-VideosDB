package database;

import entertainment.Video;
import user.User;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public String toString() {
        return "VideosDB{" +
                "videoList=" + videoList +
                '}';
    }
}
