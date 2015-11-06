package com.example.yhuang.scavengerhunt.Database;

import java.util.Map;


/**
 * Interface for the JSON information transfer from the server
 */

public interface CallbackInterface {
    //Maps id to the row of the data
    void resultsCallback(Map<Integer, ClueRow.Row> urlMap);
}