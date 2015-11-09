package com.example.jong.savangerhunt_1;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by root on 10/27/15.
 */
public class StageData {
    public static int currstage=1;
    public static int visiblestage=1;
    public static ArrayList<String> ImageURIs=new ArrayList<String>();
    S3service s3service;

    public StageData(Context context){
        this.s3service=new S3service(context);
    }

    public int getCurrstage(){
        Log.d("currentstage",String.valueOf(currstage));
        return currstage;
    }

    public void finishedcurrstage(){
        currstage++;
        visiblestage++;
    }

    public int getVisiblestage(){
        return visiblestage;
    }
    public int changevisiblestage(int stage){
        visiblestage=stage;
        return this.getVisiblestage();
    }
    public void setImageURIs(String imageuri){
        ImageURIs.add(imageuri);
    }
    public String getImageURI(int stage){
        if(ImageURIs.size()<stage){
            return "nothing";
        }
        else{
            return ImageURIs.get(stage-1);
        }
    }
    public String getVideoURI(int stage){
        String videouri=s3service.getVideoURLForStage(stage);
        if(videouri!=null){
            return videouri;
        }
        else{
            return "Stage Out of Range";
        }
    }
    public String[] getStageLocation(int stage){
        String[] location = {s3service.getLatitudeForStage(stage),s3service.getLongitudeForStage(stage)};
        return location;
    }
}
