package com.mobileproto.jovanduy.scavengerhunt;


import java.util.ArrayList;

/**
 * Class for keeping track of the progress of the scavenger hunt
 */
public class HuntProgress {

    private int currStage;
    private int stageFinal;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<String> videos;
    private String urlBase = "https://s3.amazonaws.com/olin-mobile-proto/";
    private boolean onLastStage;

    public HuntProgress() {
        this.onLastStage = false;
        this.latitudes = new ArrayList<>();
        this.longitudes = new ArrayList<>();
        this.videos = new ArrayList<>();
    }

    /**
     * checks if user has access to the last stage of the scavenger hunt
     * @return boolean onLastStage
     */
    public boolean isOnLastStage() {
        return onLastStage;
    }

    /**
     * updates if the user has access to the last stage
     * @param bool value onLastStage will be set to
     */
    public void setOnLastStage(boolean bool) {
        onLastStage = bool;
    }

    /**
     * returns the current stage that the user wants access to
     * @return int currStage
     */
    public int getCurrStage() {
        return currStage;
    }

    /**
     * updates the current stage that the user wants access to by num
     * @param num amount by which currStage should change
     */
    public void updateCurrStage(int num) {
        currStage += num;
    }

    /**
     * returns the most advanced stage that the user has access to
     * @return int stageFinal
     */
    public int getStageFinal() {
        return stageFinal;
    }

    /**
     * updates the most advanced stage that the user has access to by num
     * @param num amount by which stageFinal should change
     */
    public void updateStageFinal(int num) {
        stageFinal += num;
    }

    /**
     * returns the url for the video of a given stage
     * @param stage the stage from which to get the video
     * @return String url
     */
    public String getUrl(int stage) {
        return urlBase + videos.get(stage);
    }

    /**
     * add a video to the ArrayList of videos
     * @param url String for the part of the url representing a video
     */
    public void addUrl(String url) {
        videos.add(url);
    }

    /**
     * return the latitude of a given stage
     * @param stage stage from which to get the latitude
     * @return double latitude of stage
     */
    public double getLatitude(int stage) {
        return latitudes.get(stage);
    }

    /**
     * return the longitude of a given stage
     * @param stage stage from which to get the longitude
     * @return double longitude of stage
     */
    public double getLongitude(int stage) {
        return longitudes.get(stage);
    }

    /**
     * append a latitude to the ArrayList of latitudes
     * @param latitude
     */
    public void addLatitude(double latitude) {
        latitudes.add(latitude);
    }

    /**
     * append a longitude to the ArrayList of longitudes
     * @param longitude
     */
    public void addLongitude(double longitude) {
        longitudes.add(longitude);
    }


    /**
     * resets all of HuntProgress's fields to restart the scavenger hunt
     */
    public void reset(){
        currStage = 0;
        stageFinal = 0;
        onLastStage = false;
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        videos = new ArrayList<>();
    }
}
