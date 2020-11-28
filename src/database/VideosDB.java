package database;

import entertainment.Video;

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

    @Override
    public String toString() {
        return "VideosDB{" +
                "videoList=" + videoList +
                '}';
    }
}
