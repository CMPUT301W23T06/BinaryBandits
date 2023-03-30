package com.example.binarybandits.controllers;

import java.util.ArrayList;

/**
 *
 */
public class LeaderboardController {

    /**
     * Given a sorted list of scores, calculate the percentile of a score
     * @param allScores
     * @param score
     * @return
     */
    public int getPercentile(ArrayList<Integer> allScores, int score) {
        //Referenced: https://www.geeksforgeeks.org/program-to-calculate-percentile-of-students/

        int percentile;
        int count = 0;
        int n = allScores.size();
        for(int i = 0; i < n; i++) {
            if(allScores.get(i) < score) {
                count++;
            }
        }
        percentile = (int) Math.ceil((double)(count * 100) / (n - 1));
        return percentile;
    }
}
