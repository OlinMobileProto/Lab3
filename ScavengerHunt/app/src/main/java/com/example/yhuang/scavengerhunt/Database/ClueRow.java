package com.example.yhuang.scavengerhunt.Database;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Contains the Row object class and
 * maps it to the clue id which can also be index
 */
public class ClueRow {

    public static class Row{
        //All info about a place (latitude, longitude, S3 id) in a row
        public Integer id;
        public Double lat;
        public Double lon;
        public String s3video;
        public Row(Integer id, Double lat, Double lon, String s3video)
        {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.s3video = s3video;
        }
    }

    public static Map<Integer, Row> Map(ArrayList<Integer> id, ArrayList<Double> lati,
                                        ArrayList<Double> longi, ArrayList<String> s3vid){
        //Takes in all the columns and creates a Map with int and Row
        Map<Integer, Row> map = new TreeMap<>();
        //Gets all the clue info and puts each row in a map
        for (int i=0; i<id.size(); i++){
            Row r = new Row(id.get(i),lati.get(i),longi.get(i),s3vid.get(i));
            map.put(r.id,r);
        }
        return map;
    }
}
