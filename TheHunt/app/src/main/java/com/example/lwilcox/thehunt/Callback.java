package com.example.lwilcox.thehunt;

import java.util.ArrayList;

/**
 * Created by lwilcox on 10/21/2015.
 */
public interface Callback {
    public void callback(ArrayList<Integer> clueIds, ArrayList<Integer> clueLats, ArrayList<Integer> clueLongs, ArrayList<String> clueS3ids);
}
