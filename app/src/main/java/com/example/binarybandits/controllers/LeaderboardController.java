package com.example.binarybandits.controllers;

import java.util.ArrayList;

/**
 * Controller class for LeaderboardFragment
 */
public class LeaderboardController {

    /**
     * Given a sorted list of scores, calculate the percentile of a score
     * @param allScores list of all scores for all players in the game
     * @param score score to find percentile of
     * @return Return the percentile of the given score
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
        return 100 - percentile;
    }
}
